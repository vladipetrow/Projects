package com.example.workproject1.coreServices.models;

import java.util.List;
import java.util.Objects;

public class Post {
    private final Integer postId;
    private final ApartmentType apartmentType;
    private final TransactionType transactionType;
    private final int userId;
    private final int agencyId;
    private String location;
    private int price;
    private int area;
    private String description;
    private List<String> imageUrls;
    private java.sql.Timestamp postDate;
    private boolean isPromoted;
    private int viewCount;

    public Post(Integer postId, String location, int price, int area, String description, int userId, int agencyId, ApartmentType apartmentType, TransactionType transactionType) {
        this.postId = postId;
        this.location = location;
        this.price = price;
        this.area = area;
        this.description = description;
        this.apartmentType = apartmentType;
        this.transactionType = transactionType;
        this.userId = userId;
        this.agencyId = agencyId;
        this.imageUrls = null;
        this.postDate = null;
        this.isPromoted = false;
        this.viewCount = 0;
    }

    public Integer getPostId() {
        return postId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public java.sql.Timestamp getPostDate() {
        return postDate;
    }

    public void setPostDate(java.sql.Timestamp postDate) {
        this.postDate = postDate;
    }

    public boolean isPromoted() {
        return isPromoted;
    }

    public void setPromoted(boolean promoted) {
        isPromoted = promoted;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return price == post.price && area == post.area && userId == post.userId && agencyId == post.agencyId
                && Objects.equals(postId, post.postId) && Objects.equals(location, post.location)
                && Objects.equals(description, post.description) && apartmentType == post.apartmentType
                && transactionType == post.transactionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, location, price, area, description, apartmentType, transactionType, userId, agencyId);
    }

    @Override
    public String toString() {
        return "Post{" +
                "post_id=" + postId +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", area=" + area +
                ", description='" + description + '\'' +
                ", apartmentType=" + apartmentType +
                ", transactionType=" + transactionType +
                ", user_id=" + userId +
                ", agency_id=" + agencyId +
                '}';
    }
}
