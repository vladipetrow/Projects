package com.example.workproject1.web.api.models;

import com.example.workproject1.coreServices.models.ApartmentType;

public class PostInput {
    public Integer post_id;
    public String location;
    public int price;
    public int area;
    public String description;

    public ApartmentType type;
    public int user_id;

    public int agency_id;

    public PostInput(Integer post_id, String location, int price, int area, String description,int user_id ,int agency_id, ApartmentType type) {
        this.post_id = post_id;
        this.location = location;
        this.price = price;
        this.area = area;
        this.description = description;
        this.type = type;
        this.user_id = user_id;
        this.agency_id = agency_id;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
        this.agency_id = agency_id;
    }
}
