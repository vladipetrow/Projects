package com.example.workproject1.repositories.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Access Object for SubscriptionTier.
 * Immutable class following Effective Java principles.
 */
public final class SubscriptionTierDAO {
    private final int id;
    private final String tierName;
    private final String tierType;
    private final double price;
    private final int maxPosts;
    private final boolean has24_7Support;
    private final String description;
    private final LocalDateTime createdAt;

    private SubscriptionTierDAO(Builder builder) {
        this.id = builder.id;
        this.tierName = builder.tierName;
        this.tierType = builder.tierType;
        this.price = builder.price;
        this.maxPosts = builder.maxPosts;
        this.has24_7Support = builder.has24_7Support;
        this.description = builder.description;
        this.createdAt = builder.createdAt;
    }

    // Getters
    public int getId() { return id; }
    public String getTierName() { return tierName; }
    public String getTierType() { return tierType; }
    public double getPrice() { return price; }
    public int getMaxPosts() { return maxPosts; }
    public boolean has24_7Support() { return has24_7Support; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private String tierName;
        private String tierType;
        private double price;
        private int maxPosts;
        private boolean has24_7Support;
        private String description;
        private LocalDateTime createdAt;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder tierName(String tierName) {
            this.tierName = tierName;
            return this;
        }

        public Builder tierType(String tierType) {
            this.tierType = tierType;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder maxPosts(int maxPosts) {
            this.maxPosts = maxPosts;
            return this;
        }

        public Builder has24_7Support(boolean has24_7Support) {
            this.has24_7Support = has24_7Support;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public SubscriptionTierDAO build() {
            return new SubscriptionTierDAO(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionTierDAO that = (SubscriptionTierDAO) o;
        return id == that.id &&
                Double.compare(that.price, price) == 0 &&
                maxPosts == that.maxPosts &&
                has24_7Support == that.has24_7Support &&
                Objects.equals(tierName, that.tierName) &&
                Objects.equals(tierType, that.tierType) &&
                Objects.equals(description, that.description) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tierName, tierType, price, maxPosts, has24_7Support, description, createdAt);
    }

    @Override
    public String toString() {
        return "SubscriptionTierDAO{" +
                "id=" + id +
                ", tierName='" + tierName + '\'' +
                ", tierType='" + tierType + '\'' +
                ", price=" + price +
                ", maxPosts=" + maxPosts +
                ", has24_7Support=" + has24_7Support +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}


