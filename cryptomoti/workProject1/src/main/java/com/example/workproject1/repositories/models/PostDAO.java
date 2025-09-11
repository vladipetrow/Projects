package com.example.workproject1.repositories.models;

import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.coreServices.models.TransactionType;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Data Access Object for Post entities.
 * Immutable data transfer object with Builder pattern.
 */
public final class PostDAO {
    private final Integer postId;
    private final String location;
    private final int price;
    private final int area;
    private final String description;
    private final int typeOfApartId;  // Foreign key to type_of_apart table
    private final TransactionType transactionType;  // BUY or RENT
    private final int userId;
    private final int agencyId;
    private final Timestamp postDate;
    private final List<String> imageUrls;
    private final boolean promoted;
    private final int viewCount;

    private PostDAO(Builder builder) {
        this.postId = builder.postId;
        this.location = builder.location;
        this.price = builder.price;
        this.area = builder.area;
        this.description = builder.description;
        this.typeOfApartId = builder.typeOfApartId;
        this.transactionType = builder.transactionType;
        this.userId = builder.userId;
        this.agencyId = builder.agencyId;
        this.postDate = builder.postDate;
        this.imageUrls = builder.imageUrls != null ? Collections.unmodifiableList(builder.imageUrls) : Collections.emptyList();
        this.promoted = builder.promoted;
        this.viewCount = builder.viewCount;
    }

    // Getters only - no setters for immutability
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

    public int getTypeOfApartId() {
        return typeOfApartId;
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

    public Timestamp getPostDate() {
        return postDate;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public int getViewCount() {
        return viewCount;
    }

    /**
     * Checks if the post has images.
     * 
     * @return true if post has at least one image
     */
    public boolean hasImages() {
        return imageUrls != null && !imageUrls.isEmpty();
    }

    /**
     * Gets the price per square meter.
     * 
     * @return price divided by area, or 0 if area is 0
     */
    public double getPricePerSquareMeter() {
        return area > 0 ? (double) price / area : 0.0;
    }

    /**
     * Checks if the post is recent (within last 7 days).
     * 
     * @return true if post is within last 7 days
     */
    public boolean isRecent() {
        if (postDate == null) return false;
        long sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);
        return postDate.getTime() > sevenDaysAgo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostDAO)) return false;
        PostDAO postDAO = (PostDAO) o;
        return price == postDAO.price && 
               area == postDAO.area && 
               typeOfApartId == postDAO.typeOfApartId &&
               userId == postDAO.userId && 
               agencyId == postDAO.agencyId &&
               promoted == postDAO.promoted &&
               viewCount == postDAO.viewCount &&
               Objects.equals(postId, postDAO.postId) && 
               Objects.equals(location, postDAO.location) &&
               Objects.equals(description, postDAO.description) && 
               transactionType == postDAO.transactionType &&
               Objects.equals(imageUrls, postDAO.imageUrls) &&
               Objects.equals(postDate, postDAO.postDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, location, price, area, description, typeOfApartId, transactionType, userId, agencyId, imageUrls, postDate, promoted, viewCount);
    }

    @Override
    public String toString() {
        return "PostDAO{" +
                "postId=" + postId +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", area=" + area +
                ", description='" + description + '\'' +
                ", typeOfApartId=" + typeOfApartId +
                ", transactionType=" + transactionType +
                ", userId=" + userId +
                ", agencyId=" + agencyId +
                ", postDate=" + postDate +
                ", imageUrls=" + imageUrls +
                ", promoted=" + promoted +
                ", viewCount=" + viewCount +
                '}';
    }

    /**
     * Builder pattern for creating PostDAO instances.
     * Follows Effective Java Item 2 - Builder pattern for many parameters.
     */
    public static class Builder {
        private Integer postId;
        private String location;
        private int price;
        private int area;
        private String description;
        private int typeOfApartId;
        private TransactionType transactionType;
        private int userId;
        private int agencyId;
        private Timestamp postDate;
        private List<String> imageUrls;
        private boolean promoted = false;
        private int viewCount = 0;

        public Builder() {}

        public Builder postId(Integer postId) {
            this.postId = postId;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder price(int price) {
            this.price = price;
            return this;
        }

        public Builder area(int area) {
            this.area = area;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder typeOfApartId(int typeOfApartId) {
            this.typeOfApartId = typeOfApartId;
            return this;
        }

        public Builder transactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder agencyId(int agencyId) {
            this.agencyId = agencyId;
            return this;
        }

        public Builder postDate(Timestamp postDate) {
            this.postDate = postDate;
            return this;
        }

        public Builder imageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
            return this;
        }

        public Builder promoted(boolean promoted) {
            this.promoted = promoted;
            return this;
        }

        public Builder viewCount(int viewCount) {
            this.viewCount = viewCount;
            return this;
        }

        public PostDAO build() {
            return new PostDAO(this);
        }
    }
}
