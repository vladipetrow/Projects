package com.example.workproject1.coreServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
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
        String normalizedEmail = email.toLowerCase().trim();
        
        redisTemplate.opsForSet().add(USER_EMAILS_KEY, normalizedEmail);
        redisTemplate.opsForSet().add(ALL_EMAILS_KEY, normalizedEmail);
        
        // Set expiration for 30 days
        redisTemplate.expire(USER_EMAILS_KEY, 30, TimeUnit.DAYS);
        redisTemplate.expire(ALL_EMAILS_KEY, 30, TimeUnit.DAYS);
        
        log.debug("Added user email to cache: {}", normalizedEmail);
    }
    
    /**
     * Add agency email to cache
     */
    public void addAgencyEmail(String email) {
        String normalizedEmail = email.toLowerCase().trim();
        
        redisTemplate.opsForSet().add(AGENCY_EMAILS_KEY, normalizedEmail);
        redisTemplate.opsForSet().add(ALL_EMAILS_KEY, normalizedEmail);
        
        // Set expiration for 30 days
        redisTemplate.expire(AGENCY_EMAILS_KEY, 30, TimeUnit.DAYS);
        redisTemplate.expire(ALL_EMAILS_KEY, 30, TimeUnit.DAYS);
        
        log.debug("Added agency email to cache: {}", normalizedEmail);
    }
    
    /**
     * Remove user email from cache
     */
    public void removeUserEmail(String email) {
        String normalizedEmail = email.toLowerCase().trim();
        
        redisTemplate.opsForSet().remove(USER_EMAILS_KEY, normalizedEmail);
        redisTemplate.opsForSet().remove(ALL_EMAILS_KEY, normalizedEmail);
        
        log.debug("Removed user email from cache: {}", normalizedEmail);
    }
    
    /**
     * Remove agency email from cache
     */
    public void removeAgencyEmail(String email) {
        String normalizedEmail = email.toLowerCase().trim();
        
        redisTemplate.opsForSet().remove(AGENCY_EMAILS_KEY, normalizedEmail);
        redisTemplate.opsForSet().remove(ALL_EMAILS_KEY, normalizedEmail);
        
        log.debug("Removed agency email from cache: {}", normalizedEmail);
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

