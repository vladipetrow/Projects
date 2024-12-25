package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.SubscriptionService;
import com.example.workproject1.coreServices.UserService;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.coreServices.models.Subscription;
import com.example.workproject1.coreServices.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subscriptions")
@CrossOrigin
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    public SubscriptionController(SubscriptionService subscriptionService, UserService userService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@CookieValue(value = "Authorization", required = false) String token) {
        try {
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token missing"));
            }

            // Get user or agency details from token
            String userRole = userService.getRoleFromToken(token);
            int userId = 0;
            int agencyId = 0;
            String email;

            if (userRole.equals("[ROLE_USER]")) {
                userId = userService.getIdFromToken(token);
                email = userService.getEmail(userId);
            } else {
                agencyId = userService.getIdFromToken(token);
                email = userService.getEmail(agencyId);
            }

            // Create subscription and return the response
            Subscription subscription = subscriptionService.createSubscription(userId, agencyId, email);
            return ResponseEntity.ok(subscription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/get/subscription/{id}")
    public ResponseEntity<?> getSubscriptionById(@PathVariable int id) {
        try {
            Subscription subscription = subscriptionService.getSubscriptionById(id);
            return ResponseEntity.ok(subscription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Subscription not found"));
        }
    }

    @DeleteMapping("/delete/subscription/{id}")
    public ResponseEntity<?> deleteSubscription(@PathVariable int id) {
        try {
            subscriptionService.deleteSubscription(id);
            return ResponseEntity.ok(Map.of("message", "Subscription deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/list/agency/{agencyId}")
    public ResponseEntity<?> listAgencySubscriptions(@PathVariable int agencyId) {
        try {
            List<Agency> agencies = subscriptionService.listSubscribedAgenciesById(agencyId);
            return ResponseEntity.ok(agencies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/list/user/{userId}")
    public ResponseEntity<?> listUserSubscriptions(@PathVariable int userId) {
        try {
            List<User> users = subscriptionService.listSubscribedUsersById(userId);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}


