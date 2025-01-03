package com.example.workproject1.repositories;

import com.example.workproject1.coreServices.models.SubscriptionStatus;
import com.example.workproject1.repositories.models.AgencyDAO;
import com.example.workproject1.repositories.models.SubscriptionDAO;
import com.example.workproject1.repositories.models.UserDAO;

import java.sql.Timestamp;
import java.util.List;

public interface SubscriptionRepository {
    SubscriptionDAO createSubscription(int user_id, int agency_id, Timestamp expiration_date, String invoice_id);
    SubscriptionDAO getSubscriptionId(int id);
    SubscriptionDAO findByInvoiceId(String invoiceId);
    Timestamp getUserExpirationDate(int user_id);
    Timestamp getAgencyExpirationDate(int agency_id);
    List<AgencyDAO> listSubscribedAgencyByID(int agency_id);
    List<UserDAO> listSubscribedUserByID(int user_id);
    void deleteSubscription(int id);
    void updateSubscriptionStatus(String invoiceId, SubscriptionStatus status);
}
