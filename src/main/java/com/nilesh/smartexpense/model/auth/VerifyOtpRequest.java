package com.nilesh.smartexpense.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request model for the second step of OTP-based registration.
 * Verifies the OTP sent to the user's email and completes registration.
 */
public class VerifyOtpRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    /**
     * The 6-digit OTP received in the email.
     */
    @NotBlank(message = "OTP is required.")
    @Size(min = 6, max = 6, message = "OTP must be exactly 6 digits.")
    private String otp;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}