package com.example.workproject1.coreServices.SubscriptionService;

import com.example.workproject1.coreServices.ServiceExeptions.MaxNumberOfPostsException;
import com.example.workproject1.coreServices.ServiceExeptions.YouNeedSubscriptionException;
import com.example.workproject1.web.WebExeptions.SubscriptionExpired;
import com.example.workproject1.repositories.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static com.example.workproject1.AppConstants.*;

/**
 * Service for checking subscription limits and business rules
 */
@Service
public class SubscriptionChecker {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionChecker.class);
    
    private final SubscriptionService subscriptionService;
    private final PostRepository repository;
    
    public SubscriptionChecker(SubscriptionService subscriptionService, PostRepository repository) {
        this.subscriptionService = subscriptionService;
        this.repository = repository;
    }
    
    /**
     * Check if user/agency can create more posts based on subscription limits
     */
    public void checkPostCreationLimits(int userId, int agencyId) {
        Timestamp currDate = new Timestamp(System.currentTimeMillis());

        int userPostCount = repository.getNumberOfPostsForUser(userId);
        int agencyPostCount = repository.getNumberOfPostsForAgency(agencyId);

        // Get tier-based limits
        int userMaxPosts = subscriptionService.getMaxPostsForUser(userId);
        int agencyMaxPosts = subscriptionService.getMaxPostsForAgency(agencyId);

        // Check if user/agency has reached their tier limit
        if (userPostCount >= userMaxPosts || agencyPostCount >= agencyMaxPosts) {
            throw new MaxNumberOfPostsException(userPostCount + agencyPostCount, userMaxPosts + agencyMaxPosts);
        }

        // Check if user/agency needs subscription (basic limits)
        if (userPostCount >= DEFAULT_USER_POST_LIMIT || agencyPostCount >= DEFAULT_AGENCY_POST_LIMIT) {
            throw new YouNeedSubscriptionException("create more posts");
        }

        // Check if subscription has expired
        if (!subscriptionService.listSubscribedUsersById(userId).isEmpty()) {
            if (currDate.after(subscriptionService.getUserExpirationDate(userId))) {
                throw new SubscriptionExpired();
            }
        }

        if (!subscriptionService.listSubscribedAgenciesById(agencyId).isEmpty()) {
            if (currDate.after(subscriptionService.getAgencyExpirationDate(agencyId))) {
                throw new SubscriptionExpired();
            }
        }
        
        log.debug("Post creation limits check passed for user: {}, agency: {}", userId, agencyId);
    }
}
