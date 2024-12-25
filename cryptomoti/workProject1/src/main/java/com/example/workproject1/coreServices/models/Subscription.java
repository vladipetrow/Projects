package com.example.workproject1.coreServices.models;

import java.sql.Timestamp;
import java.util.Objects;

public class Subscription {
    private Integer id;
    private int userId;
    private int agencyId;
    private Timestamp expirationDate;
    private String btcStatus; // e.g., "PENDING", "ACTIVE", "EXPIRED"
    private String invoiceId;
    private double price; // Price for the subscription
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default Constructor
    public Subscription() {}

    // Parameterized Constructor
    public Subscription(Integer id, int userId, int agencyId, Timestamp expirationDate) {
        this.id = id;
        this.userId = userId;
        this.agencyId = agencyId;
        this.expirationDate = expirationDate;
    }

    // Getters and Setters
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

    public String getBtcStatus() {
        return btcStatus;
    }

    public void setBtcStatus(String btcStatus) {
        this.btcStatus = btcStatus;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
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

    // Overridden Methods
    @Override
    public String toString() {
        return "Subscription{" +
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
                btcStatus.equals(that.btcStatus) &&
                invoiceId.equals(that.invoiceId) &&
                createdAt.equals(that.createdAt) &&
                updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, agencyId, expirationDate, btcStatus, invoiceId, price, createdAt, updatedAt);
    }
}

