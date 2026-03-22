package com.nilesh.smartexpense.service;

import com.nilesh.smartexpense.entity.OtpVerification;
import com.nilesh.smartexpense.entity.User;
import com.nilesh.smartexpense.model.auth.AuthResponse;
import com.nilesh.smartexpense.model.auth.LoginRequest;
import com.nilesh.smartexpense.model.auth.SendOtpRequest;
import com.nilesh.smartexpense.model.auth.VerifyOtpRequest;
import com.nilesh.smartexpense.repository.OtpVerificationRepository;
import com.nilesh.smartexpense.repository.UserRepository;
import com.nilesh.smartexpense.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class handling all authentication business logic.
 * Registration is now a 2-step OTP-verified process:
 *   Step 1: sendOtp()      - validates details and sends OTP email
 *   Step 2: verifyOtp()    - verifies OTP and creates the account
 *
 * This ensures only users with valid email addresses can register.
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpVerificationRepository otpVerificationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    /**
     * Step 1 of registration: validates user details and sends OTP email.
     *
     * Steps:
     * 1. Check if email is already registered
     * 2. Encrypt password before storing temporarily
     * 3. Generate and save OTP to database
     * 4. Send OTP to user's email via Gmail SMTP
     *
     * @param request contains name, email, and password
     * @throws RuntimeException if email is already registered
     */
    public void sendOtp(SendOtpRequest request) {

        // Reject if email is already registered in the system
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Email is already registered. Please login instead."
            );
        }

        // Encrypt password before storing temporarily in OTP record
        // This ensures the password is never stored as plain text
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        // Generate OTP and save to database with expiry
        String otp = otpService.generateAndSaveOtp(
                request.getEmail(),
                request.getName(),
                encryptedPassword
        );

        // Send OTP to user's email
        emailService.sendOtpEmail(request.getEmail(), request.getName(), otp);
    }

    /**
     * Step 2 of registration: verifies OTP and creates the user account.
     *
     * Steps:
     * 1. Validate OTP against database record
     * 2. Check OTP has not expired
     * 3. Create user account with temporarily stored details
     * 4. Clean up OTP records for this email
     * 5. Return JWT token for immediate login
     *
     * @param request contains email and OTP entered by user
     * @return AuthResponse with JWT token and user details
     * @throws RuntimeException if OTP is invalid or expired
     */
    public AuthResponse verifyOtpAndRegister(VerifyOtpRequest request) {

        // Validate OTP — throws exception if invalid or expired
        OtpVerification otpRecord = otpService.validateOtp(
                request.getEmail(),
                request.getOtp()
        );

        // Create the user account using details stored in OTP record
        User user = new User();
        user.setName(otpRecord.getName());
        user.setEmail(otpRecord.getEmail());

        // Password was already encrypted in Step 1 — use it directly
        user.setPassword(otpRecord.getPassword());

        // Persist the verified user to the database
        userRepository.save(user);

        // Clean up OTP records now that registration is complete
        otpVerificationRepository.deleteAllByEmail(request.getEmail());

        // Generate JWT token for immediate login after registration
        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getName(),
                user.getEmail(),
                "Email verified successfully. Welcome to SmartExpense AI!"
        );
    }

    /**
     * Authenticates an existing user and returns a JWT token.
     *
     * Steps:
     * 1. Delegate credential validation to Spring Security
     * 2. Fetch user details from database
     * 3. Generate and return a fresh JWT token
     *
     * @param request contains email and plain text password
     * @return AuthResponse with JWT token and user details
     * @throws RuntimeException if credentials are invalid
     */
    public AuthResponse login(LoginRequest request) {

        // Validate credentials using Spring Security
        // Automatically checks BCrypt hash — throws exception if wrong
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Fetch full user details after successful authentication
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Generate a fresh JWT token for this session
        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getName(),
                user.getEmail(),
                "Login successful. Welcome back, " + user.getName() + "!"
        );
    }
}