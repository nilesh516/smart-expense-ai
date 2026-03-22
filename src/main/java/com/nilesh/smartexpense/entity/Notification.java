package com.nilesh.smartexpense.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a notification sent to a user.
 * Designed as a generic notification system that can reference
 * any entity in the system using referenceType and referenceId.
 *
 * type values:
 * BUDGET_EXCEEDED  - user has gone over their budget limit
 * BUDGET_WARNING   - user has reached 80% of their budget
 * RECURRING_DUE    - a recurring expense is due today
 * AI_SUGGESTION    - Claude has a new suggestion for the user
 * REPORT_READY     - a generated report is ready to download
 *
 * referenceType + referenceId form a polymorphic relation:
 * e.g. referenceType="BUDGET", referenceId=5 points to budget_goals.id=5
 */
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // BUDGET_EXCEEDED, BUDGET_WARNING, RECURRING_DUE, AI_SUGGESTION, REPORT_READY
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    // False until user clicks/dismisses the notification
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    // Polymorphic reference to the entity that triggered this notification
    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

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

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }

    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}