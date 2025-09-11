package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.PasswordResetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reset-password")
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);
    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String newPassword = request.get("password");
            
            logger.info("Password reset request for email: {}", email);
            
            // Validate input
            if (email == null || email.trim().isEmpty()) {
                logger.warn("Empty email provided for password reset");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email is required"));
            }
            
            if (newPassword == null || newPassword.trim().isEmpty()) {
                logger.warn("Empty password provided for password reset");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "New password is required"));
            }
            
            if (newPassword.length() < 6) {
                logger.warn("Password too short for user: {}", email);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Password must be at least 6 characters long"));
            }
            
            passwordResetService.resetPassword(email, newPassword);
            logger.info("Password reset successful for email: {}", email);
            
            return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
            
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid argument for password reset: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error during password reset: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred during password reset"));
        }
    }
}

