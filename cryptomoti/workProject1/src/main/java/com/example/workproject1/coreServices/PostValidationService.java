package com.example.workproject1.coreServices;

import com.example.workproject1.web.api.models.PostInput;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidPostIdException;
import com.example.workproject1.repositories.PostRepository;
import com.example.workproject1.repositories.models.PostDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for post validation operations
 */
@Service
public class PostValidationService {
    private static final Logger log = LoggerFactory.getLogger(PostValidationService.class);
    
    private final ValidationService validationService;
    private final SubscriptionChecker subscriptionChecker;
    private final PostRepository repository;
    
    public PostValidationService(ValidationService validationService, 
                                SubscriptionChecker subscriptionChecker,
                                PostRepository repository) {
        this.validationService = validationService;
        this.subscriptionChecker = subscriptionChecker;
        this.repository = repository;
    }
    
    /**
     * Validate post creation
     */
    public void validatePostCreation(PostInput postInput, int userId, int agencyId) {
        // Validate input
        validationService.validateNotEmpty(postInput.getLocation(), "Location");
        validationService.validatePositive(postInput.getPrice(), "Price");
        validationService.validatePositive(postInput.getArea(), "Area");
        validationService.validateNotEmpty(postInput.getDescription(), "Description");
        
        // Check subscription limits
        subscriptionChecker.checkPostCreationLimits(userId, agencyId);
        
        log.info("Post creation validation passed for user: {}, agency: {}", userId, agencyId);
    }
    
    /**
     * Validate post update
     */
    public void validatePostUpdate(PostInput postInput, int postId) {
        // Validate input
        validationService.validateNotEmpty(postInput.getLocation(), "Location");
        validationService.validatePositive(postInput.getPrice(), "Price");
        validationService.validatePositive(postInput.getArea(), "Area");
        validationService.validateNotEmpty(postInput.getDescription(), "Description");
        
        // Check if post exists
        PostDAO existingPost = repository.getPost(postId);
        if (existingPost == null) {
            throw new InvalidPostIdException("Post with ID " + postId + " not found");
        }
        
        log.info("Post update validation passed for post: {}", postId);
    }
    
    /**
     * Validate post deletion
     */
    public void validatePostDeletion(int postId, int userId) {
        // Check if post exists
        PostDAO existingPost = repository.getPost(postId);
        if (existingPost == null) {
            throw new InvalidPostIdException("Post with ID " + postId + " not found");
        }
        
        // Check ownership
        if (existingPost.getUserId() != userId) {
            throw new InvalidPostIdException("User " + userId + " is not authorized to delete post " + postId);
        }
        
        log.info("Post deletion validation passed for post: {}, user: {}", postId, userId);
    }
}
