package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.models.*;
import com.example.workproject1.repositories.models.*;

public class Mappers {
    public static User fromUserDAO(UserDAO users) {
        return new User(
                users.id, users.first_name, users.last_name, users.email,users.passwordHash,
                users.salt);
    }
    public static Post fromPostDAO(PostDAO post) {
        return new Post(post.post_id, post.location, post.price,
                 post.area, post.description,post.user_id, post.agency_id, post.type);
    }
    public static Agency fromAgencyDAO(AgencyDAO agency) {
        return new Agency(
                agency.id, agency.name_of_agency, agency.email, agency.passwordHash,
                agency.salt, agency.phone_number, agency.address);
    }

    public static Subscription fromSubscriptionDAO(SubscriptionDAO subscription) {
        return new Subscription(
                subscription.id, subscription.user_id, subscription.agency_id, subscription.expiration_date);
    }
}
