package com.example.workproject1.repositories.models;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Data Access Object for Subscription entities.
 * Immutable data transfer object with Builder pattern.
 */
public final class SubscriptionDAO {
    private final int id;
    private final String chargeId;
    private final Integer userId;
    private final Integer agencyId;
    private final Timestamp expirationDate;
    private final String paymentStatus;
    private final double price;
    private final String subscriptionTier;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;

    private SubscriptionDAO(Builder builder) {
        this.id = builder.id;
        this.chargeId = builder.chargeId;
        this.userId = builder.userId;
        this.agencyId = builder.agencyId;
        this.expirationDate = builder.expirationDate;
        this.paymentStatus = builder.paymentStatus;
        this.price = builder.price;
        this.subscriptionTier = builder.subscriptionTier;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    // Getters only - no setters for immutability
    public int getId() {
        return id;
    }

    public String getChargeId() {
        return chargeId;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public double getPrice() {
        return price;
    }

    public String getSubscriptionTier() {
        return subscriptionTier;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Checks if the subscription is currently active.
     * 
     * @return true if subscription is active and not expired
     */
    public boolean isActive() {
        return "ACTIVE".equals(paymentStatus) && 
               expirationDate != null && 
               expirationDate.after(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Checks if the subscription is expired.
     * 
     * @return true if subscription is expired
     */
    public boolean isExpired() {
        return expirationDate != null && 
               expirationDate.before(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Checks if the subscription is pending payment.
     * 
     * @return true if subscription is pending
     */
    public boolean isPending() {
        return "PENDING".equals(paymentStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionDAO)) return false;
        SubscriptionDAO that = (SubscriptionDAO) o;
        return id == that.id &&
               Double.compare(that.price, price) == 0 &&
               Objects.equals(chargeId, that.chargeId) &&
               Objects.equals(userId, that.userId) &&
               Objects.equals(agencyId, that.agencyId) &&
               Objects.equals(expirationDate, that.expirationDate) &&
               Objects.equals(paymentStatus, that.paymentStatus) &&
               Objects.equals(subscriptionTier, that.subscriptionTier) &&
               Objects.equals(createdAt, that.createdAt) &&
               Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chargeId, userId, agencyId, expirationDate, paymentStatus, price, subscriptionTier, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "SubscriptionDAO{" +
                "id=" + id +
                ", chargeId='" + chargeId + '\'' +
                ", userId=" + userId +
                ", agencyId=" + agencyId +
                ", expirationDate=" + expirationDate +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", price=" + price +
                ", subscriptionTier='" + subscriptionTier + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    /**
     * Builder pattern for creating SubscriptionDAO instances.
     * Follows Effective Java Item 2 - Builder pattern for many parameters.
     */
    public static class Builder {
        private int id;
        private String chargeId;
        private Integer userId;
        private Integer agencyId;
        private Timestamp expirationDate;
        private String paymentStatus;
        private double price;
        private String subscriptionTier;
        private Timestamp createdAt;
        private Timestamp updatedAt;

        public Builder() {}

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder chargeId(String chargeId) {
            this.chargeId = chargeId;
            return this;
        }

        public Builder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder agencyId(Integer agencyId) {
            this.agencyId = agencyId;
            return this;
        }

        public Builder expirationDate(Timestamp expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder paymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder subscriptionTier(String subscriptionTier) {
            this.subscriptionTier = subscriptionTier;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Timestamp updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public SubscriptionDAO build() {
            return new SubscriptionDAO(this);
        }
    }
}

