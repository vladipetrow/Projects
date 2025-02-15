package com.example.workproject1.web.api.models;

import com.example.workproject1.coreServices.models.ApartmentType;

import java.util.Objects;

public class PostInput {
    private final Integer postId;
    private String location;
    private int price;
    private int area;
    private String description;
    private ApartmentType type;
    private int userId;
    private int agencyId;

    public PostInput(Integer postId, String location, int price, int area, String description,int userId ,int agencyId, ApartmentType type) {
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

    public void setType(ApartmentType type) {
        this.type = type;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostInput postInput = (PostInput) o;
        return price == postInput.price && area == postInput.area && userId == postInput.userId
                && agencyId == postInput.agencyId && Objects.equals(postId, postInput.postId)
                && Objects.equals(location, postInput.location) && Objects.equals(description, postInput.description) && type == postInput.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, location, price, area, description, type, userId, agencyId);
    }

    @Override
    public String toString() {
        return "PostInput{" +
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
