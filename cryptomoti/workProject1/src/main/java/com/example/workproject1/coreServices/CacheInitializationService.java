package com.example.workproject1.coreServices;

import com.example.workproject1.repositories.AgencyRepository;
import com.example.workproject1.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to initialize email cache with existing emails from database
 * Runs on application startup to warm up the cache
 */
@Service
public class CacheInitializationService implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(CacheInitializationService.class);
    
    private final EmailCacheService emailCacheService;
    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;
    
    public CacheInitializationService(EmailCacheService emailCacheService, 
                                    UserRepository userRepository, 
                                    AgencyRepository agencyRepository) {
        this.emailCacheService = emailCacheService;
        this.userRepository = userRepository;
        this.agencyRepository = agencyRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting email cache initialization...");
        
        try {
            // Load all existing user emails
            List<String> userEmails = userRepository.getAllUserEmails();
            for (String email : userEmails) {
                emailCacheService.addUserEmail(email);
            }
            log.info("Loaded {} user emails into cache", userEmails.size());
            
            // Load all existing agency emails
            List<String> agencyEmails = agencyRepository.getAllAgencyEmails();
            for (String email : agencyEmails) {
                emailCacheService.addAgencyEmail(email);
            }
            log.info("Loaded {} agency emails into cache", agencyEmails.size());
            
            log.info("Email cache initialization completed successfully");
            
        } catch (Exception e) {
            log.error("Failed to initialize email cache: {}", e.getMessage(), e);
            // Don't fail application startup if cache initialization fails
        }
    }
}

