package com.nilesh.smartexpense.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing an expense category.
 * Categories can be system defaults (is_default = true) or
 * user-created custom categories.
 *
 * Default categories are seeded for every new user on registration:
 * Food and Dining, Travel, Shopping, Utilities, Healthcare, Entertainment
 *
 * colorCode stores hex color for UI display e.g. #FF5733
 * icon stores icon identifier for frontend rendering e.g. "food", "travel"
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Null for system default categories, set for user-created ones
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String name;

    // Hex color code for UI display e.g. #FF5733
    @Column(name = "color_code")
    private String colorCode;

    // Icon identifier for frontend e.g. "food", "travel", "shopping"
    private String icon;

    // True for system-seeded categories, false for user-created
    @Column(name = "is_default", nullable = false)
    private boolean isDefault = false;

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

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColorCode() { return colorCode; }
    public void setColorCode(String colorCode) { this.colorCode = colorCode; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}