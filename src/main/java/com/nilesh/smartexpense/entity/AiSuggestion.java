package com.nilesh.smartexpense.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity storing AI-generated suggestions from Claude.
 * Each suggestion is linked to a specific expense and user.
 * Suggestions are generated when an expense is scanned and
 * can be acted upon, dismissed, or saved for later.
 *
 * suggestionType values:
 * SAVING_TIP      - Claude suggests a cheaper alternative
 * BUDGET_ALERT    - spending is approaching category budget limit
 * CATEGORY_HINT   - Claude suggests a better category for the expense
 * ANOMALY         - expense seems unusual compared to past patterns
 * GOAL_PROGRESS   - update on progress towards a financial goal
 *
 * status values:
 * PENDING   - user has not acted on this suggestion yet
 * ACCEPTED  - user found the suggestion helpful
 * DISMISSED - user dismissed the suggestion
 */
@Entity
@Table(name = "ai_suggestions")
public class AiSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // The expense that triggered this suggestion — null for general suggestions
    @Column(name = "expense_id")
    private Long expenseId;

    // SAVING_TIP, BUDGET_ALERT, CATEGORY_HINT, ANOMALY, GOAL_PROGRESS
    @Column(name = "suggestion_type", nullable = false)
    private String suggestionType;

    // The actual suggestion text from Claude
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // PENDING, ACCEPTED, DISMISSED
    @Column(nullable = false)
    private String status = "PENDING";

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

    public Long getExpenseId() { return expenseId; }
    public void setExpenseId(Long expenseId) { this.expenseId = expenseId; }

    public String getSuggestionType() { return suggestionType; }
    public void setSuggestionType(String suggestionType) { this.suggestionType = suggestionType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}