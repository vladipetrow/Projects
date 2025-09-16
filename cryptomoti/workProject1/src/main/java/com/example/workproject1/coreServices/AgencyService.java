package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.EmailService.EmailCacheService;
import com.example.workproject1.coreServices.PasswordService.PasswordService;
import com.example.workproject1.coreServices.ServiceExeptions.*;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.repositories.AgencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.stream.Collectors;

public class AgencyService {
    private static final Logger log = LoggerFactory.getLogger(AgencyService.class);
    private final AgencyRepository repository;
    private final EmailCacheService emailCacheService;
    private final MailgunService mailgunService;
    private final ValidationService validationService;
    private final PasswordService passwordService;

    public AgencyService(AgencyRepository repository, EmailCacheService emailCacheService, MailgunService mailgunService,
                        ValidationService validationService, PasswordService passwordService) {
        this.repository = repository;
        this.emailCacheService = emailCacheService;
        this.mailgunService = mailgunService;
        this.validationService = validationService;
        this.passwordService = passwordService;
    }

    public Agency createAgency(String agencyName, String email, String password, String phoneNumber, String address) {
        log.info("Creating agency: {} with email: {}", agencyName, email);
        
        // Validate inputs
        validationService.validateNotEmpty(agencyName, "Agency name");
        validationService.validateEmail(email);
        validationService.validatePassword(password);
        validationService.validatePhoneNumber(phoneNumber);
        validationService.validateNotEmpty(address, "Address");
        
        // Check if email already exists in cache
        if (emailCacheService.isEmailExists(email)) {
            throw new EmailAlreadyExistsException("Email already exists: " + email);
        }
        
        // Hash password
        PasswordService.PasswordHash passwordHash = passwordService.hashAgencyPassword(password);
        try {
            Agency agency = Mappers.fromAgencyDAO(
                    repository.createAgency(agencyName, email, passwordHash.hash(), passwordHash.salt(), phoneNumber, address));
            
            // Add email to cache
            emailCacheService.addAgencyEmail(email);
            
            // Send welcome email
            sendWelcomeEmailSafely(email, agencyName);
            
            return agency;
        } catch (DataAccessException e) {
            log.error("DataAccessException creating agency {}: {}", email, e.getMessage(), e);
            
            // Check if it's a duplicate key error or trigger violation
            if (e.getMessage() != null && 
                (e.getMessage().toLowerCase().contains("duplicate") || 
                 e.getMessage().contains("Email already registered as User"))) {
                throw new EmailAlreadyExistsException("Email already exists: " + email);
            }
            throw new RuntimeException("Failed to create agency: " + e.getMessage(), e);
        }
    }

    public int authorizeAgency(String email, String password) {
        Agency agency = Mappers.fromAgencyDAO(repository.getAgencyByEmail(email));
        if (agency == null) {
            throw new AgencyNotFound(email);
        }

        if (!passwordService.verifyAgencyPassword(password, agency.getPasswordHash(), agency.getSalt())) {
            throw new AgencyNotFound(email);
        }

        return agency.getId();
    }
    
    private void sendWelcomeEmailSafely(String email, String agencyName) {
        try {
            mailgunService.sendWelcomeEmail(email, agencyName, ""); // Agency name as first name
        } catch (Exception e) {
            log.error("Failed to send welcome email to agency {}: {}", email, e.getMessage(), e);
            // Don't fail agency creation if email fails
        }
    }


    public Agency getAgency(int id) {
        return Mappers.fromAgencyDAO(repository.getAgency(id));
    }

    public String getEmail(int id) {
        return repository.getEmail(id);
    }

    public List<Agency> listAgency(int page, int pageSize) {
        return repository.listAgency(page, pageSize)
                .stream()
                .map(Mappers::fromAgencyDAO)
                .collect(Collectors.toList());
    }
    public void deleteAgency(int id) {
        try {
            // Get email before deleting to remove from cache
            String email = repository.getEmail(id);
            if (email != null) {
                repository.deleteAgency(id);
                // Remove email from cache after successful deletion
                emailCacheService.removeAgencyEmail(email);
                log.info("Agency deleted and email removed from cache: {}", email);
            } else {
                repository.deleteAgency(id);
                log.warn("Agency deleted but email not found for cache cleanup: {}", id);
            }
        } catch (DataAccessException e) {
            throw new InvalidAgencyIdException();
        }
    }
}
