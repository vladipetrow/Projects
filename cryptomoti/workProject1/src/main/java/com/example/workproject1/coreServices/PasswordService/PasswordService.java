package com.example.workproject1.coreServices.PasswordService;

import com.example.workproject1.security.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * Service for password-related operations.
 * Centralizes password hashing and verification logic.
 * 
 * Security Note: Peppers are loaded from application.properties to avoid
 * hardcoding sensitive values in the source code.
 */
@Service
public class PasswordService {
    
    private static final Logger logger = LoggerFactory.getLogger(PasswordService.class);
    
    @Value("${app.security.user-pepper}")
    private String userPepper;
    
    @Value("${app.security.agency-pepper}")
    private String agencyPepper;
    
    /**
     * Validates that peppers are properly loaded from configuration.
     * This ensures the application fails fast if security configuration is missing.
     */
    @PostConstruct
    public void validateConfiguration() {
        if (userPepper == null || userPepper.trim().isEmpty()) {
            throw new IllegalStateException("User pepper not configured in application.properties");
        }
        if (agencyPepper == null || agencyPepper.trim().isEmpty()) {
            throw new IllegalStateException("Agency pepper not configured in application.properties");
        }
        if (userPepper.length() < 16) {
            logger.warn("User pepper is shorter than recommended (16+ characters)");
        }
        if (agencyPepper.length() < 16) {
            logger.warn("Agency pepper is shorter than recommended (16+ characters)");
        }
        logger.info("Password service initialized with configured peppers");
    }
    
    /**
     * Hashes a password for a user.
     * 
     * @param password plain text password
     * @return PasswordHash containing hash and salt
     */
    public PasswordHash hashUserPassword(String password) {
        String salt = generateSalt();
        String hash = PasswordUtil.sha256(salt + password + userPepper);
        return new PasswordHash(hash, salt);
    }
    
    /**
     * Hashes a password for an agency.
     * 
     * @param password plain text password
     * @return PasswordHash containing hash and salt
     */
    public PasswordHash hashAgencyPassword(String password) {
        String salt = generateSalt();
        String hash = PasswordUtil.sha256(salt + password + agencyPepper);
        return new PasswordHash(hash, salt);
    }
    
    /**
     * Verifies a user password.
     * 
     * @param password plain text password
     * @param hash stored hash
     * @param salt stored salt
     * @return true if password matches
     */
    public boolean verifyUserPassword(String password, String hash, String salt) {
        String computedHash = PasswordUtil.sha256(salt + password + userPepper);
        return computedHash.equals(hash);
    }
    
    /**
     * Verifies an agency password.
     * 
     * @param password plain text password
     * @param hash stored hash
     * @param salt stored salt
     * @return true if password matches
     */
    public boolean verifyAgencyPassword(String password, String hash, String salt) {
        String computedHash = PasswordUtil.sha256(salt + password + agencyPepper);
        return computedHash.equals(hash);
    }
    
    private String generateSalt() {
        return UUID.randomUUID().toString();
    }

    /**
         * Immutable value object for password hash and salt.
         */
        public record PasswordHash(String hash, String salt) {
    }
}
