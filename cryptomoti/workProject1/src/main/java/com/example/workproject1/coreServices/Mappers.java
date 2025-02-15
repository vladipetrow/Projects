package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.models.*;
import com.example.workproject1.repositories.models.*;

public class Mappers {
    public static User fromUserDAO(UserDAO users) {
        return new User(
                users.getId(), users.getFirstName(), users.getLastName(), users.getEmail(), users.getPasswordHash(),
                users.getSalt());
    }
    public static Post fromPostDAO(PostDAO post) {
        return new Post(post.getPostId(), post.getLocation(), post.getPrice(),
                post.getArea(), post.getDescription(), post.getUserId(), post.getAgencyId(), post.getType());
    }
    public static Agency fromAgencyDAO(AgencyDAO agency) {
        return new Agency(
                agency.getId(), agency.getNameOfAgency(), agency.getEmail(), agency.getPasswordHash(),
                agency.getSalt(), agency.getPhoneNumber(), agency.getAddress());
    }

    public static Subscription fromSubscriptionDAO(SubscriptionDAO subscription) {
        return new Subscription(
                subscription.getId(), subscription.getUserId(), subscription.getAgencyId(), subscription.getExpirationDate());
    }
}
