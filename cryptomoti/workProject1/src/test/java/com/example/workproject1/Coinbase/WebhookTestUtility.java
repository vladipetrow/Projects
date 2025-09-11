package com.example.workproject1.Coinbase;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for testing Coinbase webhooks locally
 * This simulates the webhook payload that Coinbase would send
 */
@SpringBootTest
@TestPropertySource(properties = {
    "COINBASE_WEBHOOK_SECRET=${COINBASE_WEBHOOK_SECRET:test-webhook-secret}"
})
public class WebhookTestUtility {

    private final String webhookSecret = System.getenv("COINBASE_WEBHOOK_SECRET");

    @Test
    public void testWebhookPayload() throws Exception {
        if (webhookSecret == null || webhookSecret.isEmpty()) {
            System.out.println("❌ COINBASE_WEBHOOK_SECRET environment variable not set. Skipping test.");
            return;
        }
        
        // Create a test webhook payload
        Map<String, Object> testPayload = createTestWebhookPayload();
        
        // Convert to JSON
        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(testPayload);
        
        // Generate signature
        String signature = generateSignature(payloadJson);
        
        System.out.println("=== Test Webhook Payload ===");
        System.out.println("Payload: " + payloadJson);
        System.out.println("Signature: " + signature);
        System.out.println("=============================");
        
        // You can use this payload to test your webhook endpoint
        // POST to: http://localhost:8080/coinbase/webhook
        // Header: X-CC-Webhook-Signature: sha256=" + signature
        // Body: " + payloadJson
    }

    private Map<String, Object> createTestWebhookPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", "test-webhook-id");
        payload.put("type", "charge:confirmed");
        payload.put("created_at", "2024-01-01T00:00:00Z");
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", "test-charge-id-123");
        data.put("status", "CONFIRMED");
        data.put("pricing", Map.of(
            "local", Map.of("amount", "2.00", "currency", "USD"),
            "bitcoin", Map.of("amount", "0.000123", "currency", "BTC")
        ));
        data.put("metadata", Map.of("customer_email", "test@example.com"));
        
        payload.put("data", data);
        
        return payload;
    }

    private String generateSignature(String payload) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac.init(secretKeySpec);
        byte[] hmacBytes = hmac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        
        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return "sha256=" + result.toString();
    }
}
