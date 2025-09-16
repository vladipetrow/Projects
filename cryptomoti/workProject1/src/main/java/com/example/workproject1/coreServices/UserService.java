package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.EmailService.EmailCacheService;
import com.example.workproject1.coreServices.PasswordService.PasswordService;
import com.example.workproject1.coreServices.ServiceExeptions.EmailAlreadyExistsException;
import com.example.workproject1.coreServices.ServiceExeptions.UserNotFound;
import com.example.workproject1.coreServices.models.User;
import com.example.workproject1.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;
    private final EmailCacheService emailCacheService;
    private final MailgunService mailgunService;
    private final ValidationService validationService;
    private final PasswordService passwordService;

    public UserService(UserRepository repository, EmailCacheService emailCacheService, MailgunService mailgunService, 
                      ValidationService validationService, PasswordService passwordService) {
        this.repository = repository;
        this.emailCacheService = emailCacheService;
        this.mailgunService = mailgunService;
        this.validationService = validationService;
        this.passwordService = passwordService;
        log.info("UserService initialized with MailgunService: {}", mailgunService != null ? "OK" : "NULL");
    }

    public void createUser(String firstName, String lastName, String email, String password) {
        log.info("Creating user: {} {} with email: {}", firstName, lastName, email);
        
        // Validate inputs
        validationService.validateNotEmpty(firstName, "First name");
        validationService.validateNotEmpty(lastName, "Last name");
        validationService.validateEmail(email);
        validationService.validatePassword(password);
        
        // Check if email already exists in cache
        if (emailCacheService.isEmailExists(email)) {
            throw new EmailAlreadyExistsException("Email already exists: " + email);
        }
        
        // Hash password
        PasswordService.PasswordHash passwordHash = passwordService.hashUserPassword(password);
        log.debug("Generated salt and hash for user: {}", email);
        
        try {
            repository.createUser(firstName, lastName, email, passwordHash.hash(), passwordHash.salt());
            log.info("User created successfully: {}", email);
            
            // Add email to cache
            emailCacheService.addUserEmail(email);
            
            // Send welcome email
            sendWelcomeEmailSafely(email, firstName, lastName);
            
        } catch (DataAccessException e) {
            log.error("DataAccessException creating user {}: {}", email, e.getMessage(), e);
            
            // Check if it's a duplicate key error or trigger violation
            if (e.getMessage() != null && 
                (e.getMessage().toLowerCase().contains("duplicate") || 
                 e.getMessage().contains("Email already registered as Agency"))) {
                log.warn("Duplicate email detected: {}", email);
                throw new EmailAlreadyExistsException("Email already exists: " + email);
            }
            
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }
    }

    public int authorizeUser(String email, String password) {
        User user = Mappers.fromUserDAO(repository.getUserByEmail(email));
        if (user == null) {
            throw new UserNotFound(email);
        }

        if (!passwordService.verifyUserPassword(password, user.getPasswordHash(), user.getSalt())) {
            throw new UserNotFound(email);
        }

        return user.getId();
    }
    
    private void sendWelcomeEmailSafely(String email, String firstName, String lastName) {
        try {
            log.info("MailgunService status: {}", mailgunService != null ? "Available" : "NULL");
            if (mailgunService == null) {
                log.error("MailgunService is NULL - cannot send welcome email");
                return;
            }
            log.info("Attempting to send welcome email to: {}", email);
            mailgunService.sendWelcomeEmail(email, firstName, lastName);
            log.info("Welcome email sent successfully to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", email, e.getMessage(), e);
            // Don't fail user creation if email fails
        }
    }

    public String getEmail(int id) {
        return repository.getEmail(id);
    }

    public User getUser(int id) {
        return Mappers.fromUserDAO(repository.getUser(id));
    }

    public List<User> listUsers(int page, int pageSize) {
        return repository.listUsers(page, pageSize)
                .stream()
                .map(Mappers::fromUserDAO)
                .collect(Collectors.toList());
    }

    public void deleteUser(int id) {
        // Get email before deleting to remove from cache
        String email = repository.getEmail(id);
        if (email != null) {
            repository.deleteUser(id);
            // Remove email from cache after successful deletion
            emailCacheService.removeUserEmail(email);
            log.info("User deleted and email removed from cache: {}", email);
        } else {
            repository.deleteUser(id);
            log.warn("User deleted but email not found for cache cleanup: {}", id);
        }
    }
}
