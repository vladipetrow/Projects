package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.SubscriptionService.SubscriptionTierService;
import com.example.workproject1.coreServices.models.SubscriptionTier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subscription-tiers")
@CrossOrigin
public class SubscriptionTierController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionTierController.class);
    private final SubscriptionTierService subscriptionTierService;

    public SubscriptionTierController(SubscriptionTierService subscriptionTierService) {
        this.subscriptionTierService = subscriptionTierService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTiers() {
        try {
            logger.info("Fetching all subscription tiers");
            List<SubscriptionTier> tiers = subscriptionTierService.getAllTiers();
            logger.debug("Found {} subscription tiers", tiers.size());
            return ResponseEntity.ok(tiers);
        } catch (Exception e) {
            logger.error("Error fetching all subscription tiers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch subscription tiers"));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserTiers() {
        try {
            logger.info("Fetching user subscription tiers");
            List<SubscriptionTier> tiers = subscriptionTierService.getTiersByType("USER");
            logger.debug("Found {} user subscription tiers", tiers.size());
            return ResponseEntity.ok(tiers);
        } catch (Exception e) {
            logger.error("Error fetching user subscription tiers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch user subscription tiers"));
        }
    }

    @GetMapping("/agency")
    public ResponseEntity<?> getAgencyTiers() {
        try {
            logger.info("Fetching agency subscription tiers");
            List<SubscriptionTier> tiers = subscriptionTierService.getTiersByType("AGENCY");
            logger.debug("Found {} agency subscription tiers", tiers.size());
            return ResponseEntity.ok(tiers);
        } catch (Exception e) {
            logger.error("Error fetching agency subscription tiers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch agency subscription tiers"));
        }
    }

    @GetMapping("/{tierName}")
    public ResponseEntity<?> getTierByName(@PathVariable String tierName) {
        try {
            logger.info("Fetching subscription tier by name: {}", tierName);
            SubscriptionTier tier = subscriptionTierService.getTierByName(tierName);
            return ResponseEntity.ok(tier);
        } catch (Exception e) {
            logger.error("Error fetching subscription tier '{}': {}", tierName, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Subscription tier not found: " + tierName));
        }
    }
}
