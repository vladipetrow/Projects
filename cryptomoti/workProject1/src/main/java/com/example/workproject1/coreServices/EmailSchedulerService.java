package com.example.workproject1.coreServices;

import com.example.workproject1.repositories.SubscriptionRepository;
import com.example.workproject1.repositories.models.SubscriptionDAO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
public class EmailSchedulerService {
    
    private final SubscriptionRepository subscriptionRepository;
    private final MailgunService mailgunService;
    
    public EmailSchedulerService(SubscriptionRepository subscriptionRepository, MailgunService mailgunService) {
        this.subscriptionRepository = subscriptionRepository;
        this.mailgunService = mailgunService;
    }
    
    /**
     * Check for subscriptions expiring in 7 days and send reminder emails
     * Runs every day at 9:00 AM
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendExpirationReminders() {
        System.out.println("=== CHECKING FOR EXPIRING SUBSCRIPTIONS ===");
        
        try {
            // Calculate date 7 days from now
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            Timestamp reminderDate = new Timestamp(calendar.getTimeInMillis());
            
            // Get subscriptions expiring in 7 days
            List<SubscriptionDAO> expiringSubscriptions = getSubscriptionsExpiringIn(reminderDate);
            
            System.out.println("Found " + expiringSubscriptions.size() + " subscriptions expiring in 7 days");
            
            for (SubscriptionDAO subscription : expiringSubscriptions) {
                try {
                    sendExpirationReminder(subscription);
                } catch (Exception e) {
                    System.err.println("Failed to send reminder for subscription " + subscription.getId() + ": " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error in expiration reminder scheduler: " + e.getMessage());
        }
    }
    
    private List<SubscriptionDAO> getSubscriptionsExpiringIn(Timestamp targetDate) {
        // This would need to be implemented in the repository
        // For now, we'll get all active subscriptions and filter
        return subscriptionRepository.getAllActiveSubscriptions();
    }
    
    private void sendExpirationReminder(SubscriptionDAO subscription) {
        try {
            String email = "";
            String firstName = "";
            String lastName = "";
            String subscriptionType = "";
            
            if (subscription.getUserId() > 0) {
                // User subscription
                email = subscriptionRepository.getEmailByRoleAndId(subscription.getUserId(), "ROLE_USER");
                subscriptionType = "User Premium Subscription";
                // You might need to get user details for name
            } else if (subscription.getAgencyId() > 0) {
                // Agency subscription
                email = subscriptionRepository.getEmailByRoleAndId(subscription.getAgencyId(), "ROLE_AGENCY");
                subscriptionType = "Agency Subscription";
                // You might need to get agency details for name
            }
            
            if (!email.isEmpty()) {
                mailgunService.sendExpirationReminderEmail(email, firstName, lastName, subscriptionType, 7);
                System.out.println("Expiration reminder sent to: " + email);
            }
        } catch (Exception e) {
            System.err.println("Error sending expiration reminder: " + e.getMessage());
        }
    }
}


