package com.nilesh.smartexpense.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a generated expense report.
 * Reports are generated asynchronously — status tracks progress.
 * When complete, fileUrl contains the download link.
 *
 * reportType values:
 * MONTHLY_SUMMARY  - full breakdown of monthly expenses
 * CATEGORY_REPORT  - spending by category over a period
 * ANNUAL_REPORT    - year-end expense summary
 * CUSTOM_RANGE     - user-defined date range report
 *
 * status values:
 * PENDING     - report generation has been requested
 * PROCESSING  - Claude is generating the report
 * COMPLETED   - report is ready to download
 * FAILED      - report generation failed
 */
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // MONTHLY_SUMMARY, CATEGORY_REPORT, ANNUAL_REPORT, CUSTOM_RANGE
    @Column(name = "report_type", nullable = false)
    private String reportType;

    // MONTHLY, WEEKLY, YEARLY, CUSTOM
    @Column(name = "period_type")
    private String periodType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    // URL to the generated PDF or Excel file in storage
    @Column(name = "file_url")
    private String fileUrl;

    // PENDING, PROCESSING, COMPLETED, FAILED
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

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getPeriodType() { return periodType; }
    public void setPeriodType(String periodType) { this.periodType = periodType; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}