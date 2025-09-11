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
        // Convert database ID back to ApartmentType enum
        ApartmentType apartmentType = ApartmentType.values()[post.getTypeOfApartId() - 1];
        
        Post postModel = new Post(post.getPostId(), post.getLocation(), post.getPrice(),
                post.getArea(), post.getDescription(), post.getUserId(), post.getAgencyId(), 
                apartmentType, post.getTransactionType());
        postModel.setImageUrls(post.getImageUrls());
        postModel.setPostDate(post.getPostDate());
        postModel.setPromoted(post.isPromoted());
        postModel.setViewCount(post.getViewCount());
        return postModel;
    }
    public static Agency fromAgencyDAO(AgencyDAO agency) {
        return new Agency(
                agency.getId(), agency.getAgencyName(), agency.getEmail(), agency.getPasswordHash(),
                agency.getSalt(), agency.getPhoneNumber(), agency.getAddress());
    }

    public static SubscriptionTier fromSubscriptionTierDAO(SubscriptionTierDAO tierDAO) {
        return SubscriptionTier.builder()
                .id(tierDAO.getId())
                .tierName(tierDAO.getTierName())
                .tierType(tierDAO.getTierType())
                .price(tierDAO.getPrice())
                .maxPosts(tierDAO.getMaxPosts())
                .has24_7Support(tierDAO.has24_7Support())
                .description(tierDAO.getDescription())
                .createdAt(tierDAO.getCreatedAt())
                .build();
    }

    public static Subscription fromSubscriptionDAO(SubscriptionDAO subscription) {
        Subscription sub = new Subscription(
                subscription.getId(), subscription.getUserId(), subscription.getAgencyId(), subscription.getExpirationDate());
        sub.setPaymentStatus(subscription.getPaymentStatus());
        sub.setChargeId(subscription.getChargeId());
        sub.setPrice(subscription.getPrice());
        sub.setSubscriptionTier(subscription.getSubscriptionTier());
        sub.setCreatedAt(subscription.getCreatedAt());
        sub.setUpdatedAt(subscription.getUpdatedAt());
        return sub;
    }
}
