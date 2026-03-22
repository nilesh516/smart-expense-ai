package com.nilesh.smartexpense.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request model for user login.
 * Validates that email and password are present and properly formatted
 * before attempting authentication.
 */
public class LoginRequest {

    /**
     * Registered email address of the user.
     * Must be a valid email format.
     */
    @NotBlank(message = "Email is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    /**
     * User's account password.
     * Validated against the stored BCrypt hash in the database.
     */
    @NotBlank(message = "Password is required.")
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}