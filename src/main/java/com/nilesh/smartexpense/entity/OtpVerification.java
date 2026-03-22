package com.nilesh.smartexpense.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity to store OTP verification records in the database.
 * Each record represents a pending email verification request.
 * Records are cleaned up after successful verification or expiry.
 */
@Entity
@Table(name = "otp_verifications")
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Email address for which OTP was generated.
     * Used to look up the OTP during verification.
     */
    @Column(nullable = false)
    private String email;

    /**
     * The 6-digit OTP code sent to the user's email.
     * Stored as plain text since it's short-lived and low risk.
     */
    @Column(nullable = false)
    private String otp;

    /**
     * Name provided during registration.
     * Stored temporarily until OTP is verified and account is created.
     */
    @Column(nullable = false)
    private String name;

    /**
     * BCrypt-encrypted password provided during registration.
     * Stored temporarily until OTP is verified and account is created.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Timestamp when the OTP expires.
     * OTPs are valid for 10 minutes from generation.
     */
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Whether this OTP has already been used.
     * Prevents reuse of valid OTPs.
     */
    @Column(nullable = false)
    private boolean verified = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}