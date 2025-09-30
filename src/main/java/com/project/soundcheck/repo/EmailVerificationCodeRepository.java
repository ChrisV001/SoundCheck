package com.project.soundcheck.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.soundcheck.model.EmailVerificationCode;
import com.project.soundcheck.model.VerificationType;

@Repository
public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, Long>{
    Optional<EmailVerificationCode> findTopByUserIdAndVerificationTypeAndConsumedAtIsNullOrderByIdDesc(
            Long userId, VerificationType verificationType);

    Optional<EmailVerificationCode> findTopByEmailAndVerificationTypeAndConsumedAtIsNullOrderByIdDesc(
            String email, VerificationType verificationType);
}
