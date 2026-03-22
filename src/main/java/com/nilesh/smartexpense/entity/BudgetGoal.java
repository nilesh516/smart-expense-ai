package com.nilesh.smartexpense.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a user's budget goal for a specific category and period.
 *
 * periodType defines the budget cycle:
 * MONTHLY - resets every month
 * WEEKLY  - resets every week
 * YEARLY  - resets every year
 * CUSTOM  - user-defined start and end dates
 *
 * amountSpent is updated every time an expense is added or deleted
 * in the matching category and period.
 *
 * status values:
 * ACTIVE   - budget is currently active
 * EXCEEDED - spending has gone over the limit
 * COMPLETED - period has ended
 */
@Entity
@Table(name = "budget_goals")
public class BudgetGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Null means this budget applies to all categories
    @Column(name = "category_id")
    private Long categoryId;

    // MONTHLY, WEEKLY, YEARLY, CUSTOM
    @Column(name = "period_type", nullable = false)
    private String periodType;

    // Maximum amount allowed for this period
    @Column(name = "amount_limit", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountLimit;

    // Running total of expenses in this period — updated automatically
    @Column(name = "amount_spent", precision = 10, scale = 2)
    private BigDecimal amountSpent = BigDecimal.ZERO;

    // ISO 4217 currency code
    @Column(nullable = false)
    private String currency;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    // ACTIVE, EXCEEDED, COMPLETED
    @Column(nullable = false)
    private String status = "ACTIVE";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getPeriodType() { return periodType; }
    public void setPeriodType(String periodType) { this.periodType = periodType; }

    public BigDecimal getAmountLimit() { return amountLimit; }
    public void setAmountLimit(BigDecimal amountLimit) { this.amountLimit = amountLimit; }

    public BigDecimal getAmountSpent() { return amountSpent; }
    public void setAmountSpent(BigDecimal amountSpent) { this.amountSpent = amountSpent; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}