package com.example.workproject1.web.api.models;

import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.coreServices.models.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Input model for post creation and updates.
 * Simple POJO for JSON deserialization.
 */
public class PostInput {
    private Integer postId;
    private String location;
    private int price;
    private int area;
    private String description;
    private ApartmentType apartmentType;
    private TransactionType transactionType;
    private int userId;
    private int agencyId;

    // Default constructor for JSON deserialization
    public PostInput() {
    }

    public Integer getPostId() {
        return postId;
    }

    public String getLocation() {
        return location;
    }

    public int getPrice() {
        return price;
    }

    public int getArea() {
        return area;
    }

    public String getDescription() {
        return description;
    }

    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public int getUserId() {
        return userId;
    }

    public int getAgencyId() {
        return agencyId;
    }

    // Setters
    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    // Setter for 'type' field from frontend (maps to transactionType)
    @JsonProperty("type")
    public void setType(String type) {
        this.transactionType = TransactionType.valueOf(type);
    }

    // Getter for 'type' field (for frontend compatibility)
    @JsonProperty("type")
    public String getType() {
        return transactionType != null ? transactionType.toString() : null;
    }

    @Override
    public String toString() {
        return "PostInput{" +
                "postId=" + postId +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", area=" + area +
                ", description='" + description + '\'' +
                ", apartmentType=" + apartmentType +
                ", transactionType=" + transactionType +
                ", userId=" + userId +
                ", agencyId=" + agencyId +
                '}';
    }
}

