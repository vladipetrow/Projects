package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.AgencyService;
import com.example.workproject1.coreServices.SubscriptionService;
import com.example.workproject1.coreServices.UserService;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.coreServices.models.Subscription;
import com.example.workproject1.coreServices.models.User;
import com.example.workproject1.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subscriptions")
@CrossOrigin
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AgencyService agencyService;

    public SubscriptionController(SubscriptionService subscriptionService, UserService userService, AgencyService agencyService, JwtUtil jwtUtil, UserService userService1, AgencyService agencyService1) {
        this.subscriptionService = subscriptionService;
        this.jwtUtil = jwtUtil;
        this.userService = userService1;
        this.agencyService = agencyService1;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@CookieValue(value = "Authorization", required = false) String token) {
        try {
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token missing"));
            }

            String role = jwtUtil.getRoleFromToken(token);
            int id = jwtUtil.getIdFromToken(token);

            int userId = "[ROLE_USER]".equals(role) ? id : 0;
            int agencyId = "[ROLE_AGENCY]".equals(role) ? id : 0;

            String email = userId != 0 ? userService.getEmail(userId) : agencyService.getEmail(agencyId);
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

    @GetMapping("/get/user/expiration_date/{userId}")
    public ResponseEntity<?> getUserExpirationData(@PathVariable int userId) {
        try {
            Timestamp date = subscriptionService.getUserExpirationDate(userId);
            return ResponseEntity.ok(date);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/get/agency/expiration_date/{agencyId}")
    public ResponseEntity<?> getAgencyExpirationData(@PathVariable int agencyId) {
        try {
            Timestamp date = subscriptionService.getAgencyExpirationDate(agencyId);
            return ResponseEntity.ok(date);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}


