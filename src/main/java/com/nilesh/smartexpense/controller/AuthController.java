package com.nilesh.smartexpense.controller;

import com.nilesh.smartexpense.model.auth.AuthResponse;
import com.nilesh.smartexpense.model.auth.LoginRequest;
import com.nilesh.smartexpense.model.auth.SendOtpRequest;
import com.nilesh.smartexpense.model.auth.VerifyOtpRequest;
import com.nilesh.smartexpense.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations.
 * Registration is a 2-step process:
 *   POST /api/auth/send-otp    - Step 1: send OTP to email
 *   POST /api/auth/verify-otp  - Step 2: verify OTP and create account
 *
 * All endpoints are publicly accessible (no JWT required).
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "User registration and login")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Step 1: Validates registration details and sends OTP to email.
     * Returns 200 OK if OTP was sent successfully.
     *
     * @param request validated registration details
     */
    @Operation(
            summary = "Send OTP for registration",
            description = "Validates user details and sends a 6-digit OTP to the provided email"
    )
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(
            @Valid @RequestBody SendOtpRequest request) {
        authService.sendOtp(request);
        return ResponseEntity.ok(
                "Verification code sent to " + request.getEmail() +
                        ". Please check your inbox."
        );
    }

    /**
     * Step 2: Verifies the OTP and creates the user account.
     * Returns JWT token on successful verification.
     *
     * @param request contains email and OTP entered by user
     * @return JWT token and user details
     */
    @Operation(
            summary = "Verify OTP and complete registration",
            description = "Verifies the OTP sent to email and creates the user account"
    )
    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtpAndRegister(request));
    }

    /**
     * Authenticates an existing user and returns a JWT token.
     *
     * @param request validated login credentials
     * @return JWT token and user info
     */
    @Operation(
            summary = "Login user",
            description = "Authenticates credentials and returns a JWT token"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}