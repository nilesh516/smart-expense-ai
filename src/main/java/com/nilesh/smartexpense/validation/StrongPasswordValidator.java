package com.nilesh.smartexpense.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for the @StrongPassword annotation.
 *
 * Validates password against the following rules:
 * - Minimum 8 characters
 * - At least one uppercase letter
 * - At least one lowercase letter
 * - At least one digit
 * - At least one special character from: !@#$%^&*
 *
 * Each rule is checked individually to provide
 * precise feedback on which requirement is not met.
 */
public class StrongPasswordValidator
        implements ConstraintValidator<StrongPassword, String> {

    // Minimum required password length
    private static final int MIN_LENGTH = 8;

    // Regex patterns for each password rule
    private static final String UPPERCASE_PATTERN = ".*[A-Z].*";
    private static final String LOWERCASE_PATTERN = ".*[a-z].*";
    private static final String DIGIT_PATTERN = ".*[0-9].*";
    private static final String SPECIAL_CHAR_PATTERN = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*";

    /**
     * Validates the password against all defined rules.
     * Returns false with a custom message for the first rule that fails.
     *
     * @param password the password string to validate
     * @param context  the constraint validator context for custom messages
     * @return true if all rules pass, false otherwise
     */
    @Override
    public boolean isValid(String password,
                           ConstraintValidatorContext context) {

        // Skip validation if password is null or empty
        // @NotBlank should handle the null/empty check separately
        if (password == null || password.isEmpty()) {
            return true;
        }

        // Disable default constraint message so we can set custom ones
        context.disableDefaultConstraintViolation();

        // Check minimum length
        if (password.length() < MIN_LENGTH) {
            context.buildConstraintViolationWithTemplate(
                    "Password must be at least " + MIN_LENGTH + " characters."
            ).addConstraintViolation();
            return false;
        }

        // Check for at least one uppercase letter
        if (!password.matches(UPPERCASE_PATTERN)) {
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one uppercase letter."
            ).addConstraintViolation();
            return false;
        }

        // Check for at least one lowercase letter
        if (!password.matches(LOWERCASE_PATTERN)) {
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one lowercase letter."
            ).addConstraintViolation();
            return false;
        }

        // Check for at least one digit
        if (!password.matches(DIGIT_PATTERN)) {
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one number."
            ).addConstraintViolation();
            return false;
        }

        // Check for at least one special character
        if (!password.matches(SPECIAL_CHAR_PATTERN)) {
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one special character (!@#$%^&*)."
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}