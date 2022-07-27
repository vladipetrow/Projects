package com.example.workproject1.coreServices.models;

import java.sql.Timestamp;

public class Subscription {
    public Integer id;

    public int user_id;

    public int agency_id;
    public Timestamp expiration_date;
    public Subscription(Integer id, int user_id, int agency_id, Timestamp expiration_date) {
        this.id = id;
        this.user_id = user_id;
        this.agency_id = agency_id;
        this.expiration_date = expiration_date;
    }
}
