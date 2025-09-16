package com.example.workproject1.coreServicesTests;

import com.example.workproject1.coreServices.PasswordService.PasswordResetService;
import com.example.workproject1.repositories.AgencyRepository;
import com.example.workproject1.repositories.UserRepository;
import com.example.workproject1.repositories.models.AgencyDAO;
import com.example.workproject1.repositories.models.UserDAO;
import com.example.workproject1.security.PasswordUtil;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AgencyRepository agencyRepository;

    @InjectMocks
    private PasswordResetService passwordResetService;

    private static final String VALID_USER_EMAIL = "user@example.com";
    private static final String VALID_AGENCY_EMAIL = "agency@example.com";
    private static final String INVALID_EMAIL = "invalid@example.com";
    private static final String NEW_PASSWORD = "NewSecurePassword123";
    private static final String USER_SALT = "userSalt";
    private static final String AGENCY_SALT = "agencySalt";
    private static final String PEPPER_USER = "testPepperUser";
    private static final String PEPPER_AGENCY = "testPepperAgency";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(passwordResetService, "PEPPER_USER", PEPPER_USER);
        ReflectionTestUtils.setField(passwordResetService, "PEPPER_AGENCY", PEPPER_AGENCY);
    }

    @Test
    public void testResetPassword_UserEmail() {
        UserDAO mockUser = new UserDAO.Builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email(VALID_USER_EMAIL)
                .passwordHash("hashedPassword")
                .salt(USER_SALT)
                .build();
        when(userRepository.getUserByEmail(VALID_USER_EMAIL)).thenReturn(mockUser);

        passwordResetService.resetPassword(VALID_USER_EMAIL, NEW_PASSWORD);

        String expectedHashedPassword = PasswordUtil.sha256(USER_SALT + NEW_PASSWORD + PEPPER_USER);
        verify(userRepository, times(1)).updatePassword(mockUser.getId(), expectedHashedPassword);
        verifyNoInteractions(agencyRepository);
    }

    @Test
    public void testResetPassword_AgencyEmail() {
        AgencyDAO mockAgency = new AgencyDAO.Builder()
                .id(1)
                .agencyName("Test Agency")
                .email(VALID_AGENCY_EMAIL)
                .passwordHash("hashedPassword")
                .salt(AGENCY_SALT)
                .phoneNumber("1234567890")
                .address("Test Address")
                .build();
        when(agencyRepository.getAgencyByEmail(VALID_AGENCY_EMAIL)).thenReturn(mockAgency);

        passwordResetService.resetPassword(VALID_AGENCY_EMAIL, NEW_PASSWORD);

        String expectedHashedPassword = PasswordUtil.sha256(AGENCY_SALT + NEW_PASSWORD + PEPPER_AGENCY);
        verify(agencyRepository, times(1)).updatePassword(mockAgency.getId(), expectedHashedPassword);
        verifyNoInteractions(userRepository);
    }

    @Test
    public void testResetPassword_EmailNotFound() {
        when(userRepository.getUserByEmail(INVALID_EMAIL)).thenReturn(null);
        when(agencyRepository.getAgencyByEmail(INVALID_EMAIL)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                passwordResetService.resetPassword(INVALID_EMAIL, NEW_PASSWORD)
        );

        assertEquals("Email not found in users or agencies.", exception.getMessage());
        verify(userRepository, times(1)).getUserByEmail(INVALID_EMAIL);
        verify(agencyRepository, times(1)).getAgencyByEmail(INVALID_EMAIL);
        verifyNoMoreInteractions(userRepository, agencyRepository);
    }
}


