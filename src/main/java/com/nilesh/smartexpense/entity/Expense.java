package com.nilesh.smartexpense.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a single expense record.
 * Each expense belongs to exactly one user and optionally
 * one category. Supports both image-scanned and text-described expenses.
 *
 * scanType tracks how the expense was created:
 * IMAGE - scanned from receipt photo
 * TEXT  - described in plain text
 * MANUAL - entered manually (future)
 * RECURRING - auto-generated from recurring schedule (future)
 */
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key to users table — every expense must belong to a user
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Optional — null until categories feature is built in Phase 3
    @Column(name = "category_id")
    private Long categoryId;

    private String vendor;

    private Double amount;

    // ISO 4217 currency code: INR, USD, EUR etc.
    private String currency;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "payment_method")
    private String paymentMethod;

    private String date;

    // Tracks whether expense was created via image scan or text description
    @Column(name = "scan_type")
    private String scanType;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // URL to stored receipt image — populated when scan_type = IMAGE
    @Column(name = "receipt_image_url")
    private String receiptImageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Double taxAmount) { this.taxAmount = taxAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getScanType() { return scanType; }
    public void setScanType(String scanType) { this.scanType = scanType; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getReceiptImageUrl() { return receiptImageUrl; }
    public void setReceiptImageUrl(String receiptImageUrl) { this.receiptImageUrl = receiptImageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}