package com.example.workproject1.Coinbase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class CoinbaseService {
    private static final String COINBASE_BASE_URL = "https://api.commerce.coinbase.com";

    @Value("${COINBASE_API_KEY}")
    private String coinbaseApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CoinbaseService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> createCharge(double amount, String currency, String buyerEmail, String name, String description) throws JsonProcessingException {
        String url = COINBASE_BASE_URL + "/charges";
        
        System.out.println("=== COINBASE DEBUG ===");
        System.out.println("Amount: " + amount);
        System.out.println("Currency: " + currency);
        System.out.println("Buyer Email: " + buyerEmail);
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CC-Api-Key", coinbaseApiKey);
        headers.set("X-CC-Version", "2018-03-22");
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        Map<String, Object> pricing = new HashMap<>();
        // Force US locale to ensure dot as decimal separator
        pricing.put("amount", String.format(Locale.US, "%.2f", amount));
        pricing.put("currency", currency);

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("description", description);
        body.put("pricing_type", "fixed_price");
        body.put("pricing", pricing);
        body.put("metadata", Map.of("customer_email", buyerEmail));
        
        // Add local_price for better compatibility
        Map<String, Object> localPrice = new HashMap<>();
        localPrice.put("amount", String.format(Locale.US, "%.2f", amount));
        localPrice.put("currency", currency);
        body.put("local_price", localPrice);

        System.out.println("Request body: " + objectMapper.writeValueAsString(body));
        System.out.println("API Key: " + coinbaseApiKey.substring(0, 8) + "...");
        System.out.println("API Key length: " + coinbaseApiKey.length());
        System.out.println("API Key format check: " + (coinbaseApiKey.matches("^[a-f0-9-]+$") ? "Valid format" : "Invalid format"));

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class
        );

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        if (!response.getStatusCode().is2xxSuccessful()) {
            System.out.println("ERROR: Coinbase API returned error status: " + response.getStatusCode());
            System.out.println("ERROR: Response body: " + response.getBody());
            
            // Try to parse the error response for more details
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
                System.out.println("ERROR: Parsed error response: " + errorResponse);
            } catch (Exception e) {
                System.out.println("ERROR: Could not parse error response: " + e.getMessage());
            }
            
            throw new RuntimeException("Coinbase API error: " + response.getStatusCode() + " - " + response.getBody());
        }

        // Parse the response
        @SuppressWarnings("unchecked")
        Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) responseMap.get("data");

        // Extract and return charge details
        String chargeId = (String) data.get("id");
        String checkoutUrl = (String) data.get("hosted_url");
        String status = (String) data.get("status");

        Map<String, Object> result = new HashMap<>();
        result.put("chargeId", chargeId);
        result.put("checkoutUrl", checkoutUrl);
        result.put("status", status);

        return result;
    }

    public Map<String, Object> getCharge(String chargeId) throws JsonProcessingException {
        String url = COINBASE_BASE_URL + "/charges/" + chargeId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CC-Api-Key", coinbaseApiKey);
        headers.set("X-CC-Version", "2018-03-22");

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        @SuppressWarnings("unchecked")
        Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
        return data;
    }
}
