package com.example.workproject1.Coinbase;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class SimpleCoinbaseTest {
    
    @Test
    public void testCoinbaseWithMinimalRequest() {
        // Get API key from environment variable for security
        String apiKey = System.getenv("COINBASE_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("❌ COINBASE_API_KEY environment variable not set. Skipping test.");
            return;
        }
        String url = "https://api.commerce.coinbase.com/charges";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CC-Api-Key", apiKey);
        headers.set("X-CC-Version", "2018-03-22");
        headers.set("Content-Type", "application/json");
        
        // Create minimal request body
        Map<String, Object> pricing = new HashMap<>();
        pricing.put("amount", "1.00"); // Try with $1.00
        pricing.put("currency", "USD");
        
        Map<String, Object> body = new HashMap<>();
        body.put("name", "Test Charge");
        body.put("description", "Test Description");
        body.put("pricing_type", "fixed_price");
        body.put("pricing", pricing);
        
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            System.out.println("Testing with API Key: " + apiKey.substring(0, 8) + "...");
            System.out.println("Request body: " + objectMapper.writeValueAsString(body));
            
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class
            );
            
            System.out.println("SUCCESS - Status: " + response.getStatusCode());
            System.out.println("SUCCESS - Response: " + response.getBody());
            
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            if (e.getMessage().contains("400")) {
                System.out.println("This is a 400 Bad Request - likely API key or request format issue");
            }
            e.printStackTrace();
        }
    }
}


