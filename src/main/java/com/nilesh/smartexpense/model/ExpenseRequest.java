package com.nilesh.smartexpense.model;

public class ExpenseRequest {

    // For image upload - base64 encoded image
    private String imageBase64;
    private String imageType; // jpeg, png, gif, webp

    // For text based expense
    private String description;

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public String getImageType() { return imageType; }
    public void setImageType(String imageType) { this.imageType = imageType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}