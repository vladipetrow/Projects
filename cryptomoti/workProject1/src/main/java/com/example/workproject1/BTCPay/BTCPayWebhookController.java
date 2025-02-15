package com.example.workproject1.BTCPay;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.example.workproject1.coreServices.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Value;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BTCPayWebhookController {

    private final SubscriptionService subscriptionService;
    private static final Logger log = LoggerFactory.getLogger(BTCPayWebhookController.class);
    @Value("&{webHookSecret}")
    private static String SECRET;

    public BTCPayWebhookController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/btcpay/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody String payload, @RequestHeader("BTCPay-Sig") String signature) {
        log.info("Received webhook with payload: {}", payload);

        if (!verifySignature(payload, signature)) {
            log.warn("Invalid webhook signature");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid signature");
        }

        try {
            // Parse the payload
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payloadMap = mapper.readValue(payload, Map.class);
            String invoiceId = (String) payloadMap.get("id");
            String status = (String) payloadMap.get("status");

            log.info("Webhook received for invoice ID: {}, Status: {}", invoiceId, status);

            // Handle the status
            switch (status) {
                case "confirmed":
                    subscriptionService.activateSubscription(invoiceId);
                    log.info("Subscription activated for invoice ID: {}", invoiceId);
                    break;
                case "paid":
                    log.info("Invoice {} paid but not yet confirmed", invoiceId);
                    break;
                case "expired":
                    log.warn("Invoice {} expired without payment", invoiceId);
                    break;
                case "invalid":
                    log.error("Invoice {} is invalid", invoiceId);
                    break;
                default:
                    log.warn("Unhandled status: {} for invoice ID: {}", status, invoiceId);
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }

    private boolean verifySignature(String payload, String signature) {
        try {
            String calculatedSignature = "sha256=" + hmacSha256(payload, SECRET);
            return calculatedSignature.equals(signature);
        } catch (Exception e) {
            log.error("Error verifying signature", e);
            return false;
        }
    }

    private String hmacSha256(String payload, String secret) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        hmac.init(secretKeySpec);
        byte[] hmacBytes = hmac.doFinal(payload.getBytes());
        return Hex.encodeHexString(hmacBytes);
    }
}

