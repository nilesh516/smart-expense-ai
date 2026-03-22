package com.nilesh.smartexpense.repository;

import com.nilesh.smartexpense.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository for OTP verification records.
 * Provides methods to find, validate, and clean up OTP records.
 */
@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    /**
     * Finds the most recent unverified OTP for a given email.
     * Used during OTP verification to validate user input.
     */
    Optional<OtpVerification> findTopByEmailAndVerifiedFalseOrderByCreatedAtDesc(String email);

    /**
     * Deletes all OTP records for a given email.
     * Called after successful verification to clean up the database.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM OtpVerification o WHERE o.email = :email")
    void deleteAllByEmail(String email);

    /**
     * Deletes all expired OTP records across all users.
     * Should be called periodically to keep the table clean.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM OtpVerification o WHERE o.expiresAt < :now")
    void deleteAllExpired(LocalDateTime now);
}