package com.project.soundcheck.service;

import com.project.soundcheck.dto.Response;

public interface VerificationService {
    Response sendVerificationCode(String email);
    
    Response verifyCode(String email, String code);

    public void sendEmailVerification(Long userId, String email);

    public boolean verifyEmailCode(Long userId, String email, String code);
}
