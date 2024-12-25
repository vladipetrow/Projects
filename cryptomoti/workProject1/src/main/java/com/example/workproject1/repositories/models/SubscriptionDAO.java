package com.example.workproject1.repositories.models;

import java.sql.Timestamp;

public class SubscriptionDAO {
    public int id;
    public int user_id;
    public int agency_id;
    public Timestamp expiration_date;
    private String btcStatus;
    private String invoiceId;

    public SubscriptionDAO(int id, Integer user_id, Integer agency_id, Timestamp expiration_date, String invoiceId, String btcStatus) {
        this.id = id;
        this.user_id = user_id;
        this.agency_id = agency_id;
        this.expiration_date = expiration_date;
        this.invoiceId = invoiceId;
        this.btcStatus = btcStatus;
    }
}
