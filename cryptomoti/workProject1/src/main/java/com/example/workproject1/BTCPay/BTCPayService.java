package com.example.workproject1.BTCPay;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class BTCPayService {
    private static final String BTCPAY_BASE_URL = "https://mainnet.demo.btcpayserver.org";

    @Value("${BTC_API_KEY}")
    private static String btcApiKey;

    private final RestTemplate restTemplate;

    public BTCPayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> createInvoice(double amount, String currency, String buyerEmail, String storeId) throws JsonProcessingException {
        String url = BTCPAY_BASE_URL + "/api/v1/stores/" + storeId + "/invoices";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + btcApiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> body = new HashMap<>();
        body.put("price", amount);
        body.put("currency", currency);
        body.put("buyerEmail", buyerEmail);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class
        );

        // Parse the response
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(response.getBody(), Map.class);

        // Extract and return invoice details
        String invoiceId = (String) responseMap.get("id");
        String checkoutLink = (String) responseMap.get("checkoutLink");
        Map<String, Object> result = new HashMap<>();
        result.put("invoiceId", invoiceId);
        result.put("checkoutLink", checkoutLink);

        return result;
    }

    public String getInvoiceUrl(String invoiceId) {
        return BTCPAY_BASE_URL + "/i/" + invoiceId;
    }
}
