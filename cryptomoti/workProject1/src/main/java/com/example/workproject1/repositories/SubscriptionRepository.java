package com.example.workproject1.repositories;

import com.example.workproject1.coreServices.models.SubscriptionStatus;
import com.example.workproject1.repositories.models.AgencyDAO;
import com.example.workproject1.repositories.models.SubscriptionDAO;
import com.example.workproject1.repositories.models.UserDAO;

import java.sql.Timestamp;
import java.util.List;

public interface SubscriptionRepository {
    SubscriptionDAO createSubscription(int userId, int agencyId, Timestamp expiration_date, String invoice_id);
    SubscriptionDAO getSubscriptionId(int id);
    SubscriptionDAO findByInvoiceId(String invoiceId);
    Timestamp getUserExpirationDate(int userId);
    Timestamp getAgencyExpirationDate(int agencyId);
    List<AgencyDAO> listSubscribedAgencyByID(int agencyId);
    List<UserDAO> listSubscribedUserByID(int userId);
    String getEmailByRoleAndId(int id, String role);
    void deleteSubscription(int id);
    void updateSubscriptionStatus(String invoiceId, SubscriptionStatus status);
}
