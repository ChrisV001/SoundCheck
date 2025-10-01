package com.project.soundcheck.service.impl;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.soundcheck.dto.Response;
import com.project.soundcheck.exceptions.CustomException;
import com.project.soundcheck.model.EmailVerificationCode;
import com.project.soundcheck.model.User;
import com.project.soundcheck.model.VerificationType;
import com.project.soundcheck.repo.EmailVerificationCodeRepository;
import com.project.soundcheck.repo.UserRepository;
import com.project.soundcheck.service.UserService;
import com.project.soundcheck.service.VerificationService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;

    private final JavaMailSender mailSender;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final UserRepository userRepository;

    @Value("${app.verification.ttl-minutes:15}")
    private long ttlMinutes;

    @Value("${app.verification.resend-seconds:60}")
    private long resendSeconds;

    @Value("${app.verification.max-attempts:5}")
    private long maxAttempts;

    private static final SecureRandom RANDOM = new SecureRandom();

    public void sendEmailVerification(Long userId, String email) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        var existing = emailVerificationCodeRepository
            .findTopByUserIdAndVerificationTypeAndConsumedAtIsNullOrderByIdDesc(userId, VerificationType.EMAIL_VERIFY)
            .orElse(null);

        if (existing != null && existing.getSentAt() != null) {

            long secondsSinceLast = Duration.between(existing.getSentAt(), now).getSeconds();
            
            if (secondsSinceLast < resendSeconds) {
                return;
            }
        }

        String code = String.format("%06d", RANDOM.nextInt(1_000_000));
        String hash = passwordEncoder.encode(code);

        var evc = new EmailVerificationCode();
        evc.setUserId(userId);
        evc.setEmail(email);
        evc.setVerificationType(VerificationType.EMAIL_VERIFY);
        evc.setCodeHash(hash);
        evc.setExpiresAt(now.plusMinutes(ttlMinutes));
        evc.setSentAt(now);
        evc.setAttempts(0);
        emailVerificationCodeRepository.save(evc);

        sendEmail(email, "Your confirmation code",
                "Your verification code is: " + code + "\n" +
                "It expires in " + ttlMinutes + " minutes.\n\n" +
                "If you didn't request this, you can ignore this email.");

    }

    @Transactional
    public boolean verifyEmailCode(Long userId, String email, String code) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        var evc = emailVerificationCodeRepository
            .findTopByUserIdAndVerificationTypeAndConsumedAtIsNullOrderByIdDesc(userId, VerificationType.EMAIL_VERIFY)
            .orElse(null);

        if (evc == null)
            return false;
        if (!Objects.equals(evc.getEmail(), email))
            return false;
        if (evc.getConsumedAt() != null)
            return false;
        if (now.isAfter(evc.getExpiresAt()))
            return false;
        if (evc.getAttempts() >= maxAttempts)
            return false;

        evc.setAttempts(evc.getAttempts() + 1);

        boolean ok = passwordEncoder.matches(code, evc.getCodeHash());

        if (ok) {
            evc.setConsumedAt(now);
        }
        
        return ok;
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            var msg = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(msg, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);
            mailSender.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email ", e);
        }
    }

    @Override
    public Response sendVerificationCode(String email) {
        Response response = new Response();

        try {
            Response userResponse = userService.getMyInfo(email);

            if (userResponse.getStatusCode() != 200 || userResponse.getUserDTO() == null) {
                response.setStatusCode(404);
                response.setMessage("User not found.");
                return response;
            }

            Long userId = userResponse.getUserDTO().getId();
            sendEmailVerification(userId, email);

            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Failed to send verification code: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response verifyCode(String email, String code) {
        Response response = new Response();

        try {
            Response userResponse = userService.getMyInfo(email);
            if (userResponse.getStatusCode() != 200 || userResponse.getUserDTO() == null) {
                response.setStatusCode(404);
                response.setMessage("User not found");
                return response;
            }

            Long userId = userResponse.getUserDTO().getId();
            boolean isValid = verifyEmailCode(userId, email, code);

            if (isValid) {
                markUserAsVerified(userId);
                response.setStatusCode(200);
                response.setMessage("Email verified successfully");
            } else {
                response.setStatusCode(400);
                response.setMessage("Invalid or expired code");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Verification failed: " + e.getMessage());
        }
        return response;
    }

    private void markUserAsVerified(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException("User not found"));
        
        user.setIsEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
