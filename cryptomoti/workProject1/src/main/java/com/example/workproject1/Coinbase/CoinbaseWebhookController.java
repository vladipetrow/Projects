package com.example.workproject1.Coinbase;

import com.example.workproject1.coreServices.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class CoinbaseWebhookController {

    private final SubscriptionService subscriptionService;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(CoinbaseWebhookController.class);
    
    @Value("${COINBASE_WEBHOOK_SECRET}")
    private String webhookSecret;

    public CoinbaseWebhookController(SubscriptionService subscriptionService, ObjectMapper objectMapper) {
        this.subscriptionService = subscriptionService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/coinbase/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody String payload, @RequestHeader("X-CC-Webhook-Signature") String signature) {
        log.info("Received Coinbase webhook with payload: {}", payload);

        if (!verifySignature(payload, signature)) {
            log.warn("Invalid Coinbase webhook signature");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid signature");
        }

        try {
            // Parse the payload
            @SuppressWarnings("unchecked")
            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) payloadMap.get("data");
            String chargeId = (String) data.get("id");
            String type = (String) payloadMap.get("type");
            String status = (String) data.get("status");

            log.info("Webhook received for charge ID: {}, Type: {}, Status: {}", chargeId, type, status);

            // Handle different webhook types
            switch (type) {
                case "charge:confirmed":
                    if ("CONFIRMED".equals(status)) {
                        subscriptionService.activateSubscription(chargeId);
                        log.info("Subscription activated for charge ID: {}", chargeId);
                    }
                    break;
                case "charge:failed":
                    log.warn("Charge {} failed", chargeId);
                    break;
                case "charge:delayed":
                    log.info("Charge {} is delayed", chargeId);
                    break;
                case "charge:resolved":
                    if ("RESOLVED".equals(status)) {
                        subscriptionService.activateSubscription(chargeId);
                        log.info("Subscription activated for resolved charge ID: {}", chargeId);
                    }
                    break;
                default:
                    log.warn("Unhandled webhook type: {} for charge ID: {}", type, chargeId);
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error processing Coinbase webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }

    private boolean verifySignature(String payload, String signature) {
        try {
            // Coinbase webhook signature format: sha256=hash
            String[] parts = signature.split("=");
            if (parts.length != 2 || !"sha256".equals(parts[0])) {
                return false;
            }

            String receivedSignature = parts[1];
            String calculatedSignature = calculateHmacSha256(payload, webhookSecret);
            
            return calculatedSignature.equals(receivedSignature);
        } catch (Exception e) {
            log.error("Error verifying Coinbase webhook signature", e);
            return false;
        }
    }

    private String calculateHmacSha256(String payload, String secret) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac.init(secretKeySpec);
        byte[] hmacBytes = hmac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        
        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
