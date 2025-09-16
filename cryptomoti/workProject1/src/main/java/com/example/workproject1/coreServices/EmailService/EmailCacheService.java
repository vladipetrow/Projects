package com.example.workproject1.coreServices.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Service for managing email cache to prevent duplicate registrations
 * between users and agencies.
 */
@Service
public class EmailCacheService {
    private static final Logger log = LoggerFactory.getLogger(EmailCacheService.class);
    
    private static final String USER_EMAILS_KEY = "cryptomoti:emails:users";
    private static final String AGENCY_EMAILS_KEY = "cryptomoti:emails:agencies";
    private static final String ALL_EMAILS_KEY = "cryptomoti:emails:all";
    
    private final RedisTemplate<String, String> redisTemplate;
    
    public EmailCacheService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * Check if email exists in either users or agencies
     */
    public boolean isEmailExists(String email) {
        String normalizedEmail = email.toLowerCase().trim();
        
        // Check in all emails set first (fastest)
        Boolean exists = redisTemplate.opsForSet().isMember(ALL_EMAILS_KEY, normalizedEmail);
        if (Boolean.TRUE.equals(exists)) {
            return true;
        }
        
        // Double-check by checking both individual sets
        Boolean userExists = redisTemplate.opsForSet().isMember(USER_EMAILS_KEY, normalizedEmail);
        Boolean agencyExists = redisTemplate.opsForSet().isMember(AGENCY_EMAILS_KEY, normalizedEmail);
        
        return Boolean.TRUE.equals(userExists) || Boolean.TRUE.equals(agencyExists);
    }
    
    /**
     * Add user email to cache
     */
    public void addUserEmail(String email) {
        addEmailToCache(email, USER_EMAILS_KEY, "user");
    }
    
    /**
     * Add agency email to cache
     */
    public void addAgencyEmail(String email) {
        addEmailToCache(email, AGENCY_EMAILS_KEY, "agency");
    }
    
    /**
     * Private method to add email to cache with proper TTL management
     */
    private void addEmailToCache(String email, String specificKey, String type) {
        String normalizedEmail = email.toLowerCase().trim();
        
        try {
            // Check if keys exist before starting transaction
            boolean specificKeyExists = redisTemplate.hasKey(specificKey);
            boolean allKeyExists = redisTemplate.hasKey(ALL_EMAILS_KEY);
            
            // Use atomic operation to add emails and set TTL only if key doesn't exist
            redisTemplate.execute(new SessionCallback<Object>() {
                @Override
                @SuppressWarnings({"unchecked"})
                public Object execute(RedisOperations operations) {
                    operations.multi();
                    
                    // Add to both sets
                    operations.opsForSet().add(specificKey, normalizedEmail);
                    operations.opsForSet().add(ALL_EMAILS_KEY, normalizedEmail);
                    
                    // Set TTL only if the key doesn't exist (first time creation)
                    if (!specificKeyExists) {
                        operations.expire(specificKey, 30, TimeUnit.DAYS);
                    }
                    if (!allKeyExists) {
                        operations.expire(ALL_EMAILS_KEY, 30, TimeUnit.DAYS);
                    }
                    
                    return operations.exec();
                }
            });
            
            log.debug("Added {} email to cache: {}", type, normalizedEmail);
        } catch (Exception e) {
            log.error("Failed to add {} email to cache: {}", type, e.getMessage());
        }
    }
    
    /**
     * Remove user email from cache
     */
    public void removeUserEmail(String email) {
        removeEmailFromCache(email, USER_EMAILS_KEY, "user");
    }
    
    /**
     * Remove agency email from cache
     */
    public void removeAgencyEmail(String email) {
        removeEmailFromCache(email, AGENCY_EMAILS_KEY, "agency");
    }
    
    /**
     * Private method to remove email from cache
     */
    private void removeEmailFromCache(String email, String specificKey, String type) {
        String normalizedEmail = email.toLowerCase().trim();
        
        try {
            // Use atomic operation to remove emails from both sets
            redisTemplate.execute(new SessionCallback<Object>() {
                @Override
                @SuppressWarnings({"unchecked"})
                public Object execute(RedisOperations operations) {
                    operations.multi();
                    
                    // Remove from both sets
                    operations.opsForSet().remove(specificKey, normalizedEmail);
                    operations.opsForSet().remove(ALL_EMAILS_KEY, normalizedEmail);
                    
                    return operations.exec();
                }
            });
            
            log.debug("Removed {} email from cache: {}", type, normalizedEmail);
        } catch (Exception e) {
            log.error("Failed to remove {} email from cache: {}", type, e.getMessage());
        }
    }
    
    /**
     * Get all user emails (for debugging)
     */
    public Set<String> getAllUserEmails() {
        return redisTemplate.opsForSet().members(USER_EMAILS_KEY);
    }
    
    /**
     * Get all agency emails (for debugging)
     */
    public Set<String> getAllAgencyEmails() {
        return redisTemplate.opsForSet().members(AGENCY_EMAILS_KEY);
    }
    
    /**
     * Clear all email caches
     */
    public void clearAllCaches() {
        redisTemplate.delete(USER_EMAILS_KEY);
        redisTemplate.delete(AGENCY_EMAILS_KEY);
        redisTemplate.delete(ALL_EMAILS_KEY);
        
        log.info("Cleared all email caches");
    }
    
    /**
     * Initialize cache with existing emails from database
     * This should be called on application startup
     */
    public void initializeCache() {
        log.info("Email cache initialization not implemented - requires database integration");
        // TODO: Implement cache warming from database
    }
}

