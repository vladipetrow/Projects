package com.example.workproject1.web.api.models;

import java.sql.Timestamp;

public class SubscriptionInput {

    public int user_id;

    public int agency_id;
    public Timestamp expiration_date;

    public SubscriptionInput(int user_id, int agency_id, Timestamp expiration_date) {
        this.user_id = user_id;
        this.agency_id = agency_id;
        this.expiration_date = expiration_date;
    }
}
