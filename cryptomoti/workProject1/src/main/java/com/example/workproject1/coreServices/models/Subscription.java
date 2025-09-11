package com.example.workproject1.coreServices.models;

import java.sql.Timestamp;
import java.util.Objects;

public class Subscription {
    private Integer id;
    private int userId;
    private int agencyId;
    private Timestamp expirationDate;
    private String paymentStatus; // e.g., "PENDING", "ACTIVE", "EXPIRED"
    private String chargeId; // Coinbase charge ID or BTCPay invoice ID
    private String checkoutUrl; // Coinbase checkout URL
    private double price; // Price for the subscription
    private String subscriptionTier; // Subscription tier (e.g., "USER_PREMIUM", "AGENCY_PREMIUM_ULTRA")
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Subscription(Integer id, int userId, int agencyId, Timestamp expirationDate) {
        this.id = id;
        this.userId = userId;
        this.agencyId = agencyId;
        this.expirationDate = expirationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSubscriptionTier() {
        return subscriptionTier;
    }

    public void setSubscriptionTier(String subscriptionTier) {
        this.subscriptionTier = subscriptionTier;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Overridden Methods
    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", userId=" + userId +
                ", agencyId=" + agencyId +
                ", expirationDate=" + expirationDate +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", chargeId='" + chargeId + '\'' +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return userId == that.userId &&
                agencyId == that.agencyId &&
                Double.compare(that.price, price) == 0 &&
                id.equals(that.id) &&
                expirationDate.equals(that.expirationDate) &&
                paymentStatus.equals(that.paymentStatus) &&
                chargeId.equals(that.chargeId) &&
                createdAt.equals(that.createdAt) &&
                updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, agencyId, expirationDate, paymentStatus, chargeId, price, createdAt, updatedAt);
    }
}

