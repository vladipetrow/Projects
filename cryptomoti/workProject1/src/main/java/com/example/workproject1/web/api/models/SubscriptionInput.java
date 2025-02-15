package com.example.workproject1.web.api.models;

import java.sql.Timestamp;
import java.util.Objects;

public class SubscriptionInput {

    private final int userId;
    private final int agencyId;
    private Timestamp expiration_date;

    public SubscriptionInput(int userId, int agencyId, Timestamp expiration_date) {
        this.userId = userId;
        this.agencyId = agencyId;
        this.expiration_date = expiration_date;
    }

    public int getUserId() {
        return userId;
    }

    public int getAgencyId() {
        return agencyId;
    }

    public Timestamp getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Timestamp expiration_date) {
        this.expiration_date = expiration_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionInput that = (SubscriptionInput) o;
        return getUserId() == that.getUserId() && getAgencyId() == that.getAgencyId() && Objects.equals(getExpiration_date(), that.getExpiration_date());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getAgencyId(), getExpiration_date());
    }

    @Override
    public String toString() {
        return "SubscriptionInput{" +
                "user_id=" + userId +
                ", agency_id=" + agencyId +
                ", expiration_date=" + expiration_date +
                '}';
    }
}
