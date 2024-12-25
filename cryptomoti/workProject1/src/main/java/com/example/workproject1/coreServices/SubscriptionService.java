package com.example.workproject1.coreServices;

import com.example.workproject1.BTCPay.BTCPayService;
import com.example.workproject1.coreServices.ServiceExeptions.AgencyNotFound;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidSubscriptionId;
import com.example.workproject1.coreServices.ServiceExeptions.UserNotFound;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.coreServices.models.Subscription;
import com.example.workproject1.coreServices.models.SubscriptionStatus;
import com.example.workproject1.coreServices.models.User;
import com.example.workproject1.repositories.SubscriptionRepository;
import com.example.workproject1.repositories.models.SubscriptionDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final BTCPayService btcPayService;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, BTCPayService btcPayService) {
        this.subscriptionRepository = subscriptionRepository;
        this.btcPayService = btcPayService;
    }

    public Subscription createSubscription(int userId, int agencyId, String buyerEmail) throws JsonProcessingException {
        // Create a BTC invoice
        String invoiceId = btcPayService.createInvoice(2, "USD", buyerEmail,"1").toString();

        // Send payment link via email
        MailgunService.sendMail(
                "Subscription",
                "You have subscribed successfully! Please complete your payment via the link: " +
                        btcPayService.getInvoiceUrl(invoiceId),
                buyerEmail
        );

        // Save subscription with "PENDING" status
        Timestamp expirationDate = calculateExpirationDate();
        subscriptionRepository.createSubscription(userId, agencyId, expirationDate, invoiceId);

        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setAgencyId(agencyId);
        subscription.setExpirationDate(expirationDate);
        subscription.setInvoiceId(invoiceId);
        subscription.setBtcStatus(SubscriptionStatus.PENDING.name());

        return subscription;
    }

    private Timestamp calculateExpirationDate() {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.DAY_OF_WEEK, 30); // Add 30 days to current date
        return new Timestamp(expiration.getTimeInMillis());
    }

    public void activateSubscription(String invoiceId) {
        subscriptionRepository.updateSubscriptionStatus(invoiceId, SubscriptionStatus.ACTIVE);
    }

    public Subscription getSubscriptionById(int id) {
        SubscriptionDAO subscriptionDAO = subscriptionRepository.getSubscriptionId(id);
        if (subscriptionDAO == null) {
            throw new InvalidSubscriptionId();
        }
        return Mappers.fromSubscriptionDAO(subscriptionDAO);
    }

    public Timestamp getUserExpirationDate(int userId) {
        Timestamp expirationDate = subscriptionRepository.getUserExpirationDate(userId);
        if (expirationDate == null) {
            throw new UserNotFound();
        }
        return expirationDate;
    }

    public Timestamp getAgencyExpirationDate(int agencyId) {
        Timestamp expirationDate = subscriptionRepository.getAgencyExpirationDate(agencyId);
        if (expirationDate == null) {
            throw new AgencyNotFound();
        }

        return expirationDate;
    }


    public List<Agency> listSubscribedAgenciesById(int agencyId) {
        return subscriptionRepository.listSubscribedAgencyByID(agencyId)
                .stream()
                .map(Mappers::fromAgencyDAO)
                .collect(Collectors.toList());
    }

    public List<User> listSubscribedUsersById(int userId) {
        return subscriptionRepository.listSubscribedUserByID(userId)
                .stream()
                .map(Mappers::fromUserDAO)
                .collect(Collectors.toList());
    }

    public void deleteSubscription(int id) {
        subscriptionRepository.deleteSubscription(id);
    }
}
