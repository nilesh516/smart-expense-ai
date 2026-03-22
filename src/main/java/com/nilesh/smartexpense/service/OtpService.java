package com.nilesh.smartexpense.service;

import com.nilesh.smartexpense.entity.OtpVerification;
import com.nilesh.smartexpense.repository.OtpVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * Service responsible for OTP generation, storage, and validation.
 * Uses SecureRandom instead of Random for cryptographically
 * secure OTP generation — important for security.
 */
@Service
public class OtpService {

    @Autowired
    private OtpVerificationRepository otpRepository;

    @Value("${otp.expiry.minutes}")
    private int otpExpiryMinutes;

    @Value("${otp.length}")
    private int otpLength;

    // SecureRandom is used instead of Random for security
    // Regular Random is predictable and should never be used for OTPs
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a secure OTP, saves it to the database,
     * and returns it for sending via email.
     *
     * Any existing unverified OTPs for this email are deleted
     * before creating a new one to prevent confusion.
     *
     * @param email     user's email address
     * @param name      user's name
     * @param password  BCrypt-encrypted password (stored temporarily)
     * @return the generated OTP string
     */
    public String generateAndSaveOtp(String email, String name, String password) {

        // Delete any existing unverified OTPs for this email
        // Prevents multiple valid OTPs existing at the same time
        otpRepository.deleteAllByEmail(email);

        // Generate a cryptographically secure numeric OTP
        String otp = generateSecureOtp();

        // Build and save OTP record with expiry time
        OtpVerification otpRecord = new OtpVerification();
        otpRecord.setEmail(email);
        otpRecord.setOtp(otp);
        otpRecord.setName(name);
        otpRecord.setPassword(password);
        otpRecord.setExpiresAt(LocalDateTime.now().plusMinutes(otpExpiryMinutes));
        otpRecord.setVerified(false);

        otpRepository.save(otpRecord);

        return otp;
    }

    /**
     * Validates the OTP entered by the user.
     * Checks existence, expiry, and correctness of the OTP.
     *
     * @param email user's email address
     * @param otp   OTP entered by the user
     * @return OtpVerification record if valid
     * @throws RuntimeException if OTP is invalid, expired, or not found
     */
    public OtpVerification validateOtp(String email, String otp) {

        // Find the most recent unverified OTP for this email
        OtpVerification otpRecord = otpRepository
                .findTopByEmailAndVerifiedFalseOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new RuntimeException(
                        "No verification request found for this email. Please request a new OTP."
                ));

        // Check if OTP has expired
        if (LocalDateTime.now().isAfter(otpRecord.getExpiresAt())) {
            throw new RuntimeException(
                    "OTP has expired. Please request a new verification code."
            );
        }

        // Check if OTP matches what was sent
        if (!otpRecord.getOtp().equals(otp)) {
            throw new RuntimeException(
                    "Invalid OTP. Please check your email and try again."
            );
        }

        // Mark OTP as verified to prevent reuse
        otpRecord.setVerified(true);
        otpRepository.save(otpRecord);

        return otpRecord;
    }

    /**
     * Generates a cryptographically secure numeric OTP
     * of the configured length (default 6 digits).
     *
     * Uses SecureRandom to ensure unpredictability.
     *
     * @return numeric OTP string padded with leading zeros if needed
     */
    private String generateSecureOtp() {
        // Calculate the range based on OTP length
        // For 6 digits: min=100000, max=999999
        int min = (int) Math.pow(10, otpLength - 1);
        int max = (int) Math.pow(10, otpLength) - 1;

        int otpValue = min + secureRandom.nextInt(max - min + 1);
        return String.valueOf(otpValue);
    }
}