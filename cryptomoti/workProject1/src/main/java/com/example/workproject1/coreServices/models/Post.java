package com.example.workproject1.coreServices.models;

import java.util.Objects;

public class Post {
    private final Integer postId;
    private final ApartmentType type;
    private final int userId;
    private final int agencyId;
    private String location;
    private int price;
    private int area;
    private String description;

    public Post(Integer postId, String location, int price, int area, String description, int userId, int agencyId, ApartmentType type) {
        this.postId = postId;
        this.location = location;
        this.price = price;
        this.area = area;
        this.description = description;
        this.type = type;
        this.userId = userId;
        this.agencyId = agencyId;
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

    public ApartmentType getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    public int getAgencyId() {
        return agencyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return price == post.price && area == post.area && userId == post.userId && agencyId == post.agencyId
                && Objects.equals(postId, post.postId) && Objects.equals(location, post.location)
                && Objects.equals(description, post.description) && type == post.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, location, price, area, description, type, userId, agencyId);
    }

    @Override
    public String toString() {
        return "Post{" +
                "post_id=" + postId +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", area=" + area +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", user_id=" + userId +
                ", agency_id=" + agencyId +
                '}';
    }
}
