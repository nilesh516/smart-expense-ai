package com.nilesh.smartexpense.repository;

import com.nilesh.smartexpense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Expense entity.
 * Provides data access methods for expense records.
 * All queries are scoped to a specific user via userId
 * to ensure data isolation between users.
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Find all expenses belonging to a specific user
    List<Expense> findByUserId(Long userId);

    // Find expenses by category ID for a specific user
    List<Expense> findByUserIdAndCategoryId(Long userId, Long categoryId);
}