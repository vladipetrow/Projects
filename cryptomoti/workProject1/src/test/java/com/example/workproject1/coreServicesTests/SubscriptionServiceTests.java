package com.example.workproject1.coreServicesTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.workproject1.coreServices.ServiceExeptions.AgencyNotFound;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidSubscriptionIdException;
import com.example.workproject1.coreServices.ServiceExeptions.UserNotFound;
import com.example.workproject1.coreServices.SubscriptionService;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.coreServices.models.Subscription;
import com.example.workproject1.coreServices.models.SubscriptionStatus;
import com.example.workproject1.coreServices.models.User;
import com.example.workproject1.repositories.SubscriptionRepository;
import com.example.workproject1.repositories.models.AgencyDAO;
import com.example.workproject1.repositories.models.SubscriptionDAO;
import com.example.workproject1.repositories.models.UserDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class SubscriptionServiceTests {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private static final int VALID_USER_ID = 1;
    private static final int VALID_AGENCY_ID = 2;
    private static final int INVALID_SUBSCRIPTION_ID = 999;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testCreateSubscription_Success() throws JsonProcessingException {
//        Timestamp expirationDate = new Timestamp(System.currentTimeMillis());
//        SubscriptionDAO subscriptionDAO = new SubscriptionDAO(1, VALID_USER_ID, VALID_AGENCY_ID, expirationDate);
//        when(subscriptionRepository.createSubscription(anyInt(), anyInt(), any(Timestamp.class))).thenReturn(subscriptionDAO);
//
//        Subscription result = subscriptionService.createSubscription(VALID_USER_ID, VALID_AGENCY_ID);
//
//        assertNotNull(result);
//        assertEquals(VALID_USER_ID, result.getUserId());
//        assertEquals(VALID_AGENCY_ID, result.getAgencyId());
//        verify(subscriptionRepository, times(1)).createSubscription(eq(VALID_USER_ID), eq(VALID_AGENCY_ID), any(Timestamp.class));
//    }

    @Test
    void testActivateSubscription_Success() {
        String invoiceId = "invoice123";

        subscriptionService.activateSubscription(invoiceId);

        verify(subscriptionRepository, times(1)).updateSubscriptionStatus(eq(invoiceId), eq(SubscriptionStatus.ACTIVE));
    }

//    @Test
//    void testGetSubscriptionById_Success() {
//        Timestamp expirationDate = new Timestamp(System.currentTimeMillis());
//        SubscriptionDAO subscriptionDAO = new SubscriptionDAO(1, VALID_USER_ID, VALID_AGENCY_ID, expirationDate);
//        when(subscriptionRepository.getSubscriptionId(anyInt())).thenReturn(subscriptionDAO);
//
//        Subscription result = subscriptionService.getSubscriptionById(1);
//
//        assertNotNull(result);
//        assertEquals(VALID_USER_ID, result.getUserId());
//        assertEquals(VALID_AGENCY_ID, result.getAgencyId());
//        verify(subscriptionRepository, times(1)).getSubscriptionId(1);
//    }

    @Test
    void testGetSubscriptionById_InvalidId() {
        when(subscriptionRepository.getSubscriptionId(INVALID_SUBSCRIPTION_ID)).thenReturn(null);

        assertThrows(InvalidSubscriptionIdException.class, () -> subscriptionService.getSubscriptionById(INVALID_SUBSCRIPTION_ID));
        verify(subscriptionRepository, times(1)).getSubscriptionId(INVALID_SUBSCRIPTION_ID);
    }

    @Test
    void testGetUserExpirationDate_Success() {
        Timestamp expirationDate = new Timestamp(System.currentTimeMillis());
        when(subscriptionRepository.getUserExpirationDate(VALID_USER_ID)).thenReturn(expirationDate);

        Timestamp result = subscriptionService.getUserExpirationDate(VALID_USER_ID);

        assertNotNull(result);
        assertEquals(expirationDate, result);
        verify(subscriptionRepository, times(1)).getUserExpirationDate(VALID_USER_ID);
    }

    @Test
    void testGetUserExpirationDate_NotFound() {
        when(subscriptionRepository.getUserExpirationDate(VALID_USER_ID)).thenReturn(null);

        assertThrows(UserNotFound.class, () -> subscriptionService.getUserExpirationDate(VALID_USER_ID));
        verify(subscriptionRepository, times(1)).getUserExpirationDate(VALID_USER_ID);
    }

    @Test
    void testGetAgencyExpirationDate_Success() {
        Timestamp expirationDate = new Timestamp(System.currentTimeMillis());
        when(subscriptionRepository.getAgencyExpirationDate(VALID_AGENCY_ID)).thenReturn(expirationDate);

        Timestamp result = subscriptionService.getAgencyExpirationDate(VALID_AGENCY_ID);

        assertNotNull(result);
        assertEquals(expirationDate, result);
        verify(subscriptionRepository, times(1)).getAgencyExpirationDate(VALID_AGENCY_ID);
    }

    @Test
    void testGetAgencyExpirationDate_NotFound() {
        when(subscriptionRepository.getAgencyExpirationDate(VALID_AGENCY_ID)).thenReturn(null);

        assertThrows(AgencyNotFound.class, () -> subscriptionService.getAgencyExpirationDate(VALID_AGENCY_ID));
        verify(subscriptionRepository, times(1)).getAgencyExpirationDate(VALID_AGENCY_ID);
    }

    @Test
    void testListSubscribedAgenciesById_Success() {
        AgencyDAO agencyDAO = new AgencyDAO.Builder()
                .id(VALID_AGENCY_ID)
                .agencyName("Test Agency")
                .email("agency@example.com")
                .passwordHash("hash")
                .salt("salt")
                .phoneNumber("1234567890")
                .address("Address")
                .build();
        when(subscriptionRepository.listSubscribedAgencyByID(VALID_AGENCY_ID)).thenReturn(Collections.singletonList(agencyDAO));

        List<Agency> result = subscriptionService.listSubscribedAgenciesById(VALID_AGENCY_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionRepository, times(1)).listSubscribedAgencyByID(VALID_AGENCY_ID);
    }

    @Test
    void testListSubscribedUsersById_Success() {
        UserDAO userDAO = new UserDAO.Builder()
                .id(VALID_USER_ID)
                .firstName("John")
                .lastName("Doe")
                .email("user@example.com")
                .passwordHash("hash")
                .salt("salt")
                .build();
        when(subscriptionRepository.listSubscribedUserByID(VALID_USER_ID)).thenReturn(Collections.singletonList(userDAO));

        List<User> result = subscriptionService.listSubscribedUsersById(VALID_USER_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionRepository, times(1)).listSubscribedUserByID(VALID_USER_ID);
    }

    @Test
    void testDeleteSubscription_Success() {
        subscriptionService.deleteSubscription(1);

        verify(subscriptionRepository, times(1)).deleteSubscription(1);
    }
}
