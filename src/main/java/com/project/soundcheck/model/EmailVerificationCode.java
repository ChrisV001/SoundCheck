package com.project.soundcheck.model;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "email_verification_codes", indexes = {
    @Index(columnList = "userId,verificationType,consumedAt"),
    @Index(columnList = "email,verificationType,consumedAt")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailVerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long userId;

    private String email;

    @Enumerated(EnumType.STRING)
    private VerificationType verificationType;

    private String codeHash;

    private LocalDateTime expiresAt;

    private LocalDateTime sentAt;

    private LocalDateTime consumedAt;

    private int attempts;
}
