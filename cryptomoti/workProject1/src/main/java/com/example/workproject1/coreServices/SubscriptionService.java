package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.ServiceExeptions.AgencyNotFound;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidSubscriptionId;
import com.example.workproject1.coreServices.ServiceExeptions.UserNotFound;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.coreServices.models.Subscription;
import com.example.workproject1.coreServices.models.User;
import com.example.workproject1.repositories.SubscriptionRepository;
import org.springframework.dao.DataAccessException;


import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Subscription createSubscription(int user_id, int agency_id, Timestamp expiration_date){
        MailgunService.sendMail("Subscription.","You have subscribed succesfully! Your subscription " +
                "will еxpire after 2 months! \n " +
                "A a day before expiration date you will recieve an email to renew it!","vladi.petrow@abv.bg");
        return Mappers.fromSubscriptionDAO(subscriptionRepository.createSubscription(user_id, agency_id, expiration_date));
    }

    public Subscription getSubscriptionId(int id) {
        try {
            return Mappers.fromSubscriptionDAO(subscriptionRepository.getSubscriptionId(id));
        } catch (DataAccessException e) {
            throw new InvalidSubscriptionId();
        }
    }

    public Timestamp getUserExpirationDate(int user_id){
        try {
            return subscriptionRepository.getUserExpirationDate(user_id);
        }catch (DataAccessException e){
            throw new UserNotFound();
        }
    }

    public Timestamp getAgencyExpirationDate(int agency_id){
        try {
            return subscriptionRepository.getAgencyExpirationDate(agency_id);
        } catch (DataAccessException e) {
            throw new AgencyNotFound();
        }
    }

    public  List<Agency> listSubscribedAgencyByID(int agency_id) {
        return  subscriptionRepository.listSubscribedAgencyByID(agency_id)
                .stream()
                .map(Mappers::fromAgencyDAO)
                .collect(Collectors.toList());
    }

    public List<User> listSubscribedUserByID(int user_id){
        return subscriptionRepository.listSubscribedUserByID(user_id)
                .stream()
                .map(Mappers::fromUserDAO)
                .collect(Collectors.toList());
    }

    public void deleteSubscription(int id){
         subscriptionRepository.deleteSubscription(id);
    }

}
