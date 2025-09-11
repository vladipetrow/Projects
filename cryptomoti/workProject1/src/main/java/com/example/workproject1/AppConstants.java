package com.example.workproject1;

/**
 * Application-wide constants 
 * Centralizes all magic numbers and strings to improve maintainability.
 */
public final class AppConstants {
    
    // Security Constants - Moved to application.properties for better security
    
    // Validation Constants
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int PHONE_NUMBER_LENGTH = 10;
    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    
    // Post Limits
    public static final int DEFAULT_USER_POST_LIMIT = 3;
    public static final int DEFAULT_AGENCY_POST_LIMIT = 5;
    
    // Subscription Constants
    public static final int SUBSCRIPTION_DURATION_DAYS = 30;
    public static final String PENDING_STATUS = "PENDING";
    public static final String ACTIVE_STATUS = "ACTIVE";
    public static final String EXPIRED_STATUS = "EXPIRED";
    
    // JWT Constants
    public static final long ACCESS_TOKEN_VALIDITY_MS = 15 * 60 * 1000; // 15 minutes
    public static final long REFRESH_TOKEN_VALIDITY_MS = 7 * 24 * 60 * 60 * 1000; // 7 days
    
    // Roles
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_AGENCY = "ROLE_AGENCY";
    
    // Default Values
    public static final String DEFAULT_TIER = "USER_PREMIUM";
    
    // Private constructor to prevent instantiation
    private AppConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}