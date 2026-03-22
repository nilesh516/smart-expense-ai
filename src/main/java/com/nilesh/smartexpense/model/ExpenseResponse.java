package com.nilesh.smartexpense.model;

/**
 * Response model for scanned expense data returned by Claude.
 * category field is kept here for display purposes —
 * it will be mapped to a category_id in Phase 3.
 */
public class ExpenseResponse {

    private String vendor;
    private Double amount;
    private String date;
    private String category;
    private String paymentMethod;
    private Double taxAmount;
    private String currency;
    private String notes;

    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Double taxAmount) { this.taxAmount = taxAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}