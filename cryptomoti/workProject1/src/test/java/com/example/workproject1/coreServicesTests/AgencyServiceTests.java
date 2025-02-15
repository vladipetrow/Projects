package com.example.workproject1.coreServicesTests;

import com.example.workproject1.coreServices.AgencyService;
import com.example.workproject1.coreServices.ServiceExeptions.*;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.repositories.AgencyRepository;
import com.example.workproject1.repositories.models.AgencyDAO;
import com.example.workproject1.security.PasswordUtil;
import com.example.workproject1.coreServices.Mappers;
import com.google.api.client.util.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AgencyServiceTests {

    @Mock
    private AgencyRepository repository;

    @InjectMocks
    private AgencyService agencyService;
    @Value("${passwordPeperAgency}")
    private String PEPPER_AGENCY;

    private final String agencyName = "Test Agency";
    private final String validEmail = "test@example.com";
    private final String validPassword = "password123";
    private final String validPhoneNumber = "1234567890";
    private final String validAddress = "123 Test St";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAgency_Success() {
        when(repository.createAgency(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new AgencyDAO(1, agencyName, validEmail, "hashedPassword", "salt", validPhoneNumber, validAddress));

        agencyService.createAgency(agencyName, validEmail, validPassword, validPhoneNumber, validAddress);

        verify(repository, times(1)).createAgency(eq(agencyName), eq(validEmail), anyString(), anyString(), eq(validPhoneNumber), eq(validAddress));
    }

    @Test
    public void testCreateAgency_InvalidEmail() {
        String name = "Test Agency";
        String invalidEmail = "invalid-email";

        assertThrows(InvalidEmail.class, () ->
                agencyService.createAgency(name, invalidEmail, validPassword, validPhoneNumber, validAddress));
    }

    @Test
    public void testCreateAgency_ShortPassword() {
        String name = "Test Agency";
        String shortPassword = "123";

        assertThrows(MinimumLengthOfPasswordIs6.class, () ->
                agencyService.createAgency(name, validEmail, shortPassword, validPhoneNumber, validAddress));
    }

    @Test
    public void testCreateAgency_InvalidPhoneNumber() {
        String name = "Test Agency";
        String invalidPhoneNumber = "123";

        assertThrows(InvalidPhoneNumber.class, () ->
                agencyService.createAgency(name, validEmail, validPassword, invalidPhoneNumber, validAddress));
    }

    @Test
    public void testCreateAgency_RepositoryThrowsException() {
        String name = "Test Agency";
        String salt = UUID.randomUUID().toString();
        String hash = PasswordUtil.sha256(salt + validPassword + PEPPER_AGENCY);

        when(repository.createAgency(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new DataAccessException("Database error") {});

        assertThrows(CreateAgencyException.class, () ->
                agencyService.createAgency(name, validEmail, validPassword, validPhoneNumber, validAddress));
    }

    @Test
    public void testAuthorizeAgency_ValidCredentials() {
        String salt = UUID.randomUUID().toString();
        String hash = PasswordUtil.sha256(salt + validPassword + PEPPER_AGENCY);

        AgencyDAO mockAgencyDAO = new AgencyDAO(1, "Test Agency", validEmail, hash, salt, validPhoneNumber, validAddress);
        when(repository.getAgencyByEmail(validEmail)).thenReturn(mockAgencyDAO);

        int result = agencyService.authorizeAgency(validEmail, validPassword);

        assertEquals(1, result);
        verify(repository, times(1)).getAgencyByEmail(validEmail);
    }


    @Test
    public void testAuthorizeAgency_InvalidPassword() {
        String salt = UUID.randomUUID().toString();
        String incorrectPasswordHash = PasswordUtil.sha256(salt + "wrongPassword" + PEPPER_AGENCY);

        AgencyDAO mockAgencyDAO = new AgencyDAO(1, "Test Agency", validEmail, incorrectPasswordHash, salt, validPhoneNumber, validAddress);
        when(repository.getAgencyByEmail(validEmail)).thenReturn(mockAgencyDAO);

        assertThrows(UserNotExist.class, () ->
                agencyService.authorizeAgency(validEmail, validPassword));
    }

    @Test
    public void testGetAgency_ValidId() {
        int agencyId = 1;
        AgencyDAO mockAgencyDAO = new AgencyDAO(agencyId, "Test Agency", validEmail, "hash", "salt", validPhoneNumber, validAddress);
        Agency expectedAgency = Mappers.fromAgencyDAO(mockAgencyDAO);

        when(repository.getAgency(agencyId)).thenReturn(mockAgencyDAO);

        Agency result = agencyService.getAgency(agencyId);

        assertNotNull(result);
        assertEquals(expectedAgency.getNameOfAgency(), result.getNameOfAgency());
        verify(repository, times(1)).getAgency(agencyId);
    }

    @Test
    public void testGetEmail_ValidId() {
        int agencyId = 1;
        when(repository.getEmail(agencyId)).thenReturn(validEmail);

        String email = agencyService.getEmail(agencyId);

        assertEquals(validEmail, email);
        verify(repository, times(1)).getEmail(agencyId);
    }

    @Test
    public void testListAgency() {
        List<AgencyDAO> mockAgencies = new ArrayList<>();
        mockAgencies.add(new AgencyDAO(1, "Test Agency", validEmail, "hash", "salt", validPhoneNumber, validAddress));
        mockAgencies.add(new AgencyDAO(2, "Another Agency", "another@example.com", "hash", "salt", validPhoneNumber, validAddress));

        when(repository.listAgency(0, 10)).thenReturn(mockAgencies);

        List<Agency> result = agencyService.listAgency(0, 10);

        assertEquals(2, result.size());
        verify(repository, times(1)).listAgency(0, 10);
    }

    @Test
    public void testDeleteAgency_ValidId() {
        int agencyId = 1;

        agencyService.deleteAgency(agencyId);

        verify(repository, times(1)).deleteAgency(agencyId);
    }

    @Test
    public void testDeleteAgency_InvalidId() {
        int invalidId = 999;
        doThrow(new DataAccessException("Invalid ID") {}).when(repository).deleteAgency(invalidId);

        assertThrows(InvalidAgencyId.class, () ->
                agencyService.deleteAgency(invalidId));
    }
}

