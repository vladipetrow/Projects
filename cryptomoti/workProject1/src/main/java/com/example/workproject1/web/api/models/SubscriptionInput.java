package com.example.workproject1.web.api.models;

import com.example.workproject1.coreServices.models.SubscriptionStatus;

/**
 * Input model for subscription creation and updates.
 * Simple POJO for JSON deserialization.
 */
public class SubscriptionInput {
    private int userId;
    private int agencyId;
    private int subscriptionTierId;
    private SubscriptionStatus status;
    private String paymentMethod;
    private String transactionId;

    // Default constructor for JSON deserialization
    public SubscriptionInput() {
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public int getAgencyId() {
        return agencyId;
    }

    public int getSubscriptionTierId() {
        return subscriptionTierId;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    public void setSubscriptionTierId(int subscriptionTierId) {
        this.subscriptionTierId = subscriptionTierId;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "SubscriptionInput{" +
                "userId=" + userId +
                ", agencyId=" + agencyId +
                ", subscriptionTierId=" + subscriptionTierId +
                ", status=" + status +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
