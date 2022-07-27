package com.example.workproject1.repositories.models;

import com.example.workproject1.coreServices.models.ApartmentType;

import java.sql.Timestamp;

public class PostDAO {
    public Integer post_id;
    public String location;
    public int price;
    public int area;
    public String description;
    public ApartmentType type;

    public int user_id;

    public int agency_id;
    public Timestamp post_date;

    public PostDAO(Integer post_id, String location, int price, int area, String description,int user_id, int agency_id, ApartmentType type, Timestamp post_date) {
        this.post_id = post_id;
        this.location = location;
        this.price = price;
        this.area = area;
        this.description = description;
        this.type = type;
        this.user_id = user_id;
        this.agency_id = agency_id;
        this.post_date = post_date;
    }
}
