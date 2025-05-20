package com.example.workproject1.repositories.models;

import com.example.workproject1.coreServices.models.ApartmentType;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class PostDAO {
    private final Integer postId;
    private final int userId;
    private final int agencyId;
    private final Timestamp postDate;
    private String location;
    private int price;
    private int area;
    private String description;
    private ApartmentType type;
    private List<String> imageUrls;

    public PostDAO(Integer postId, String location, int price, int area, String description, int userId, int agencyId,
                   ApartmentType type, Timestamp postDate, List<String> imageUrls) {
        this.postId = postId;
        this.location = location;
        this.price = price;
        this.area = area;
        this.description = description;
        this.type = type;
        this.userId = userId;
        this.agencyId = agencyId;
        this.postDate = postDate;
        this.imageUrls = imageUrls;
    }

    public List<String> getImageUrl() {
        return imageUrls;
    }
    public void setImageUrl(List<String> imageUrls) {
        this.imageUrls = imageUrls;
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

    public int getAgencyId() {
        return agencyId;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDAO postDAO = (PostDAO) o;
        return price == postDAO.price && area == postDAO.area && userId == postDAO.userId && agencyId == postDAO.agencyId
                && Objects.equals(postId, postDAO.postId) && Objects.equals(location, postDAO.location)
                && Objects.equals(description, postDAO.description) && type == postDAO.type
                && Objects.equals(postDate, postDAO.postDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, location, price, area, description, type, userId, agencyId, postDate);
    }

    @Override
    public String toString() {
        return "PostDAO{" +
                "post_id=" + postId +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", area=" + area +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", user_id=" + userId +
                ", agency_id=" + agencyId +
                ", post_date=" + postDate +
                '}';
    }
}
