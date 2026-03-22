package com.nilesh.smartexpense.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom validation annotation to enforce strong password policy.
 *
 * Password must meet ALL of the following criteria:
 * - Minimum 8 characters
 * - At least one uppercase letter (A-Z)
 * - At least one lowercase letter (a-z)
 * - At least one digit (0-9)
 * - At least one special character (!@#$%^&*)
 *
 * Usage:
 *   @StrongPassword
 *   private String password;
 */
@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {

    String message() default "Password must be at least 8 characters and contain " +
            "at least one uppercase letter, one lowercase letter, " +
            "one number, and one special character (!@#$%^&*).";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}