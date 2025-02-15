package com.example.workproject1.repositories.models;

import java.sql.Timestamp;
import java.util.Objects;

public class SubscriptionDAO {
    private final int id;
    private final String invoiceId; // Unique identifier for BTC payments
    private final Integer userId;
    private final Integer agencyId;
    private Timestamp expirationDate;
    private String btcStatus; // e.g., "PENDING", "ACTIVE", "EXPIRED", "CANCELLED"
    private double price; // Subscription price
    private Timestamp createdAt; // Timestamp of subscription creation
    private Timestamp updatedAt; // Timestamp of last update

    public SubscriptionDAO(
            int id,
            Integer userId,
            Integer agencyId,
            Timestamp expirationDate,
            String invoiceId,
            String btcStatus
    ) {
        this.id = id;
        this.userId = userId;
        this.agencyId = agencyId;
        this.expirationDate = expirationDate;
        this.btcStatus = btcStatus;
        this.invoiceId = invoiceId;
    }

    public int getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getAgencyId() {
        return agencyId;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getBtcStatus() {
        return btcStatus;
    }

    public void setBtcStatus(String btcStatus) {
        this.btcStatus = btcStatus;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    // Override toString for better debugging
    @Override
    public String toString() {
        return "SubscriptionDAO{" +
                "id=" + id +
                ", userId=" + userId +
                ", agencyId=" + agencyId +
                ", expirationDate=" + expirationDate +
               ", btcStatus='" + btcStatus + '\'' +
               ", invoiceId='" + invoiceId + '\'' +
               ", price=" + price +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
                '}';
    }

    // Override equals for object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionDAO that = (SubscriptionDAO) o;
        return id == that.id &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(agencyId, that.agencyId) &&
                Objects.equals(expirationDate, that.expirationDate);
    }

    // Override hashCode for collections
    @Override
    public int hashCode() {
        return Objects.hash(id, userId, agencyId, expirationDate);
    }
}

