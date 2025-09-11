package com.example.workproject1.coreServices;

import com.example.workproject1.Coinbase.CoinbaseService;
import com.example.workproject1.coreServices.ServiceExeptions.AgencyNotFound;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidSubscriptionIdException;
import com.example.workproject1.coreServices.ServiceExeptions.UserNotFound;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.coreServices.models.Subscription;
import com.example.workproject1.coreServices.models.SubscriptionStatus;
import com.example.workproject1.coreServices.models.SubscriptionTier;
import com.example.workproject1.coreServices.models.User;
import com.example.workproject1.repositories.SubscriptionRepository;
import com.example.workproject1.repositories.models.SubscriptionDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.workproject1.AppConstants.*;

/**
 * Service for managing subscription operations.
 * Follows Clean Code principles with proper separation of concerns.
 */
@Service
public class SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);
    
    private final SubscriptionRepository subscriptionRepository;
    private final CoinbaseService coinbaseService;
    private final MailgunService mailgunService;
    private final SubscriptionTierService subscriptionTierService;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, 
                             CoinbaseService coinbaseService, 
                             MailgunService mailgunService, 
                             SubscriptionTierService subscriptionTierService) {
        this.subscriptionRepository = subscriptionRepository;
        this.coinbaseService = coinbaseService;
        this.mailgunService = mailgunService;
        this.subscriptionTierService = subscriptionTierService;
    }

    /**
     * Creates a new subscription for a user or agency.
     * 
     * @param userId user ID (0 if agency subscription)
     * @param agencyId agency ID (0 if user subscription)
     * @param buyerEmail email of the buyer
     * @param tierName subscription tier name
     * @return created subscription
     * @throws JsonProcessingException if JSON processing fails
     */
    public Subscription createSubscription(int userId, int agencyId, String buyerEmail, String tierName) 
            throws JsonProcessingException {
        
        logger.info("Creating subscription for userId: {}, agencyId: {}, tier: {}", userId, agencyId, tierName);
        
        SubscriptionTier tier = subscriptionTierService.getTierByName(tierName);
        boolean hasExistingSubscription = checkExistingSubscription(userId, agencyId);
        
        PaymentDetails paymentDetails = createPaymentDetails(tier, buyerEmail);
        Map<String, Object> chargeResult = coinbaseService.createCharge(
            paymentDetails.getAmount(),
            paymentDetails.getCurrency(),
            paymentDetails.getEmail(),
            paymentDetails.getName(),
            paymentDetails.getDescription()
        );
        
        SubscriptionDAO subscriptionDAO = saveOrUpdateSubscription(
            userId, agencyId, chargeResult, paymentDetails.getAmount(), tierName, hasExistingSubscription
        );
        
        return buildSubscriptionResponse(subscriptionDAO, chargeResult);
    }

    /**
     * Activates a subscription after successful payment.
     * 
     * @param chargeId the charge ID from payment processor
     */
    public void activateSubscription(String chargeId) {
        logger.info("Activating subscription for charge ID: {}", chargeId);
        
        Timestamp expirationDate = calculateExpirationDate();
        
        subscriptionRepository.updateSubscriptionStatus(chargeId, SubscriptionStatus.ACTIVE);
        subscriptionRepository.updateSubscriptionExpiration(chargeId, expirationDate);
        
        sendInvoiceEmailSafely(chargeId);
        
        logger.info("Subscription activated successfully for charge ID: {}", chargeId);
    }

    /**
     * Checks if a user has an active subscription.
     * 
     * @param userId user ID to check
     * @return true if user has active subscription
     */
    public boolean isUserSubscriptionActive(int userId) {
        try {
            Timestamp expirationDate = subscriptionRepository.getUserExpirationDate(userId);
            return expirationDate != null && isNotExpired(expirationDate);
        } catch (Exception e) {
            logger.warn("Error checking user subscription for userId: {}", userId, e);
            return false;
        }
    }

    /**
     * Checks if an agency has an active subscription.
     * 
     * @param agencyId agency ID to check
     * @return true if agency has active subscription
     */
    public boolean isAgencySubscriptionActive(int agencyId) {
        try {
            Timestamp expirationDate = subscriptionRepository.getAgencyExpirationDate(agencyId);
            return expirationDate != null && isNotExpired(expirationDate);
        } catch (Exception e) {
            logger.warn("Error checking agency subscription for agencyId: {}", agencyId, e);
            return false;
        }
    }

    /**
     * Gets the maximum number of posts allowed for a user based on their subscription.
     * 
     * @param userId user ID
     * @return maximum number of posts allowed
     */
    public int getMaxPostsForUser(int userId) {
        if (!isUserSubscriptionActive(userId)) {
            return DEFAULT_USER_POST_LIMIT;
        }
        
        return getMaxPostsFromTier(userId, 0);
    }

    /**
     * Gets the maximum number of posts allowed for an agency based on their subscription.
     * 
     * @param agencyId agency ID
     * @return maximum number of posts allowed
     */
    public int getMaxPostsForAgency(int agencyId) {
        if (!isAgencySubscriptionActive(agencyId)) {
            return DEFAULT_AGENCY_POST_LIMIT;
        }
        
        return getMaxPostsFromTier(0, agencyId);
    }

    public Subscription getSubscriptionById(int id) {
        SubscriptionDAO subscriptionDAO = subscriptionRepository.getSubscriptionId(id);
        if (subscriptionDAO == null) {
            throw new InvalidSubscriptionIdException();
        }
        return Mappers.fromSubscriptionDAO(subscriptionDAO);
    }

    public Timestamp getUserExpirationDate(int userId) {
        Timestamp expirationDate = subscriptionRepository.getUserExpirationDate(userId);
        if (expirationDate == null) {
            throw new UserNotFound();
        }
        return expirationDate;
    }

    public Timestamp getAgencyExpirationDate(int agencyId) {
        Timestamp expirationDate = subscriptionRepository.getAgencyExpirationDate(agencyId);
        if (expirationDate == null) {
            throw new AgencyNotFound();
        }
        return expirationDate;
    }

    public String getEmail(int id, String role) {
        return subscriptionRepository.getEmailByRoleAndId(id, role);
    }

    public List<Agency> listSubscribedAgenciesById(int agencyId) {
        return subscriptionRepository.listSubscribedAgencyByID(agencyId)
                .stream()
                .map(Mappers::fromAgencyDAO)
                .collect(Collectors.toList());
    }

    public List<User> listSubscribedUsersById(int userId) {
        return subscriptionRepository.listSubscribedUserByID(userId)
                .stream()
                .map(Mappers::fromUserDAO)
                .collect(Collectors.toList());
    }

    public void deleteSubscription(int id) {
        subscriptionRepository.deleteSubscription(id);
    }

    public List<SubscriptionTier> getTiersByType(String tierType) {
        return subscriptionTierService.getTiersByType(tierType);
    }

    public List<SubscriptionTier> getAllTiers() {
        return subscriptionTierService.getAllTiers();
    }

    public SubscriptionTier getTierByName(String tierName) {
        return subscriptionTierService.getTierByName(tierName);
    }

    // Private helper methods following Clean Code principles
    
    private boolean checkExistingSubscription(int userId, int agencyId) {
        if (userId > 0) {
            return subscriptionRepository.hasUserSubscription(userId);
        }
        if (agencyId > 0) {
            return subscriptionRepository.hasAgencySubscription(agencyId);
        }
        return false;
    }
    
    private PaymentDetails createPaymentDetails(SubscriptionTier tier, String buyerEmail) {
        return new PaymentDetails(
            tier.getPrice(),
            "USD",
            buyerEmail,
            tier.getDescription(),
            tier.has24_7Support() ? tier.getDescription() + " with 24/7 support" : tier.getDescription()
        );
    }
    
    private SubscriptionDAO saveOrUpdateSubscription(int userId, int agencyId, 
                                                   Map<String, Object> chargeResult, 
                                                   double amount, String tierName, 
                                                   boolean hasExistingSubscription) {
        String chargeId = (String) chargeResult.get("chargeId");
        
        if (hasExistingSubscription) {
            subscriptionRepository.updateExistingSubscription(userId, agencyId, null, chargeId, amount);
            return subscriptionRepository.getSubscriptionByUserOrAgency(userId, agencyId);
        } else {
            return subscriptionRepository.createSubscription(userId, agencyId, null, chargeId, amount);
        }
    }
    
    private Subscription buildSubscriptionResponse(SubscriptionDAO subscriptionDAO, Map<String, Object> chargeResult) {
        Subscription subscription = Mappers.fromSubscriptionDAO(subscriptionDAO);
        subscription.setChargeId((String) chargeResult.get("chargeId"));
        subscription.setCheckoutUrl((String) chargeResult.get("checkoutUrl"));
        subscription.setPaymentStatus(PENDING_STATUS);
        return subscription;
    }
    
    private Timestamp calculateExpirationDate() {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.DAY_OF_MONTH, SUBSCRIPTION_DURATION_DAYS);
        return new Timestamp(expiration.getTimeInMillis());
    }
    
    private boolean isNotExpired(Timestamp expirationDate) {
        return expirationDate.after(new Timestamp(System.currentTimeMillis()));
    }
    
    private void sendInvoiceEmailSafely(String chargeId) {
        try {
            sendInvoiceEmail(chargeId);
        } catch (Exception e) {
            logger.error("Failed to send invoice email for charge ID: {}", chargeId, e);
            // Don't fail the activation if email fails
        }
    }
    
    private void sendInvoiceEmail(String chargeId) {
        try {
            // Get subscription details
            SubscriptionDAO subscriptionDAO = subscriptionRepository.findByInvoiceId(chargeId);
            if (subscriptionDAO == null) {
                logger.warn("Subscription not found for charge ID: {}", chargeId);
                return;
            }
            
            // Get user/agency details
            String email = "";
            String firstName = "";
            String lastName = "";
            String subscriptionType = "";
            double amount = 0.0;
            
            if (subscriptionDAO.getUserId() > 0) {
                // User subscription
                email = subscriptionRepository.getEmailByRoleAndId(subscriptionDAO.getUserId(), ROLE_USER);
                subscriptionType = "User Premium Subscription";
                amount = subscriptionDAO.getPrice();
            } else if (subscriptionDAO.getAgencyId() > 0) {
                // Agency subscription
                email = subscriptionRepository.getEmailByRoleAndId(subscriptionDAO.getAgencyId(), ROLE_AGENCY);
                subscriptionType = "Agency Subscription";
                amount = subscriptionDAO.getPrice();
            }
            
            if (!email.isEmpty()) {
                mailgunService.sendInvoiceEmail(email, firstName, lastName, amount, "USD", subscriptionType);
                logger.info("Invoice email sent to: {}", email);
            }
        } catch (Exception e) {
            logger.error("Error sending invoice email: {}", e.getMessage(), e);
        }
    }
    
    private int getMaxPostsFromTier(int userId, int agencyId) {
        SubscriptionDAO subscriptionDAO = subscriptionRepository.getSubscriptionByUserOrAgency(userId, agencyId);
        if (subscriptionDAO != null && subscriptionDAO.getSubscriptionTier() != null) {
            SubscriptionTier tier = subscriptionTierService.getTierByName(subscriptionDAO.getSubscriptionTier());
            return tier.getMaxPosts();
        }
        return userId > 0 ? DEFAULT_USER_POST_LIMIT : DEFAULT_AGENCY_POST_LIMIT;
    }
    
    /**
     * Immutable value object for payment details
     */
    private static final class PaymentDetails {
        private final double amount;
        private final String currency;
        private final String email;
        private final String name;
        private final String description;
        
        public PaymentDetails(double amount, String currency, String email, String name, String description) {
            this.amount = amount;
            this.currency = currency;
            this.email = email;
            this.name = name;
            this.description = description;
        }
        
        // Getters
        public double getAmount() { return amount; }
        public String getCurrency() { return currency; }
        public String getEmail() { return email; }
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
}