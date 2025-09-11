package com.example.workproject1.Coinbase;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinbaseServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private CoinbaseService coinbaseService;

    @BeforeEach
    void setUp() {
        coinbaseService = new CoinbaseService(restTemplate, objectMapper);
        ReflectionTestUtils.setField(coinbaseService, "coinbaseApiKey", "test-api-key");
    }

    @Test
    void testCreateCharge() throws Exception {
        // Mock response
        String mockResponse = "{\"data\":{\"id\":\"test-charge-id\",\"hosted_url\":\"https://commerce.coinbase.com/charges/test-charge-id\",\"status\":\"NEW\"}}";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(mockResponse);
        
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        // Mock object mapper
        when(objectMapper.readValue(anyString(), eq(Map.class))).thenReturn(Map.of(
                "data", Map.of(
                        "id", "test-charge-id",
                        "hosted_url", "https://commerce.coinbase.com/charges/test-charge-id",
                        "status", "NEW"
                )
        ));

        // Test
        Map<String, Object> result = coinbaseService.createCharge(2.0, "USD", "test@example.com", "Test Charge", "Test Description");

        // Verify
        assertNotNull(result);
        assertEquals("test-charge-id", result.get("chargeId"));
        assertEquals("https://commerce.coinbase.com/charges/test-charge-id", result.get("checkoutUrl"));
        assertEquals("NEW", result.get("status"));
    }
}
