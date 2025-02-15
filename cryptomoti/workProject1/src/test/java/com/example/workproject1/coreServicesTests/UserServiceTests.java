package com.example.workproject1.coreServicesTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.workproject1.coreServices.ServiceExeptions.InvalidEmail;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidParametersForUser;
import com.example.workproject1.coreServices.ServiceExeptions.MinimumLengthOfPasswordIs6;
import com.example.workproject1.coreServices.ServiceExeptions.UserNotExist;
import com.example.workproject1.coreServices.UserService;
import com.example.workproject1.coreServices.models.User;
import com.example.workproject1.repositories.UserRepository;
import com.example.workproject1.repositories.models.UserDAO;
import com.example.workproject1.security.PasswordUtil;
import com.google.api.client.util.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UserServiceTests {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;
    @Value("${passwordPeperUser}")
    private String PEPPER_USER;

    private static final String VALID_EMAIL = "user@example.com";
    private static final String INVALID_EMAIL = "invalid-email";
    private static final String VALID_PASSWORD = "securePassword123";
    private static final String SHORT_PASSWORD = "123";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        when(repository.createUser(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new UserDAO(1, FIRST_NAME, LAST_NAME, VALID_EMAIL));

        userService.createUser(FIRST_NAME, LAST_NAME, VALID_EMAIL, VALID_PASSWORD);

        verify(repository, times(1)).createUser(eq(FIRST_NAME), eq(LAST_NAME), eq(VALID_EMAIL), anyString(), anyString());
    }


    @Test
    void testCreateUser_InvalidEmail() {
        assertThrows(InvalidEmail.class, () -> userService.createUser(FIRST_NAME, LAST_NAME, INVALID_EMAIL, VALID_PASSWORD));
        verifyNoInteractions(repository);
    }

    @Test
    void testCreateUser_ShortPassword() {
        // Act & Assert
        assertThrows(MinimumLengthOfPasswordIs6.class, () -> userService.createUser(FIRST_NAME, LAST_NAME, VALID_EMAIL, SHORT_PASSWORD));
        verifyNoInteractions(repository);
    }

    @Test
    void testCreateUser_InvalidParametersForUser() {
        doThrow(new DataAccessException("Invalid parameters") {}).when(repository)
                .createUser(anyString(), anyString(), anyString(), anyString(), anyString());

        assertThrows(InvalidParametersForUser.class, () ->
                userService.createUser(FIRST_NAME, LAST_NAME, VALID_EMAIL, VALID_PASSWORD)
        );

        verify(repository, times(1)).createUser(eq(FIRST_NAME), eq(LAST_NAME), eq(VALID_EMAIL), anyString(), anyString());
    }


    @Test
    void testAuthorizeUser_Success() {
        String salt = UUID.randomUUID().toString();
        String hash = PasswordUtil.sha256(salt + VALID_PASSWORD + PEPPER_USER);
        UserDAO mockUserDAO = new UserDAO(1, FIRST_NAME, LAST_NAME, VALID_EMAIL, hash, salt);
        when(repository.getUserByEmail(VALID_EMAIL)).thenReturn(mockUserDAO);

        int userId = userService.authorizeUser(VALID_EMAIL, VALID_PASSWORD);

        assertEquals(1, userId);
        verify(repository, times(1)).getUserByEmail(VALID_EMAIL);
    }


    @Test
    void testAuthorizeUser_InvalidPassword() {
        String salt = UUID.randomUUID().toString();
        String incorrectHash = PasswordUtil.sha256(salt + "wrongPassword" + PEPPER_USER);
        UserDAO mockUserDAO = new UserDAO(1, FIRST_NAME, LAST_NAME, VALID_EMAIL, incorrectHash, salt);
        when(repository.getUserByEmail(VALID_EMAIL)).thenReturn(mockUserDAO);

        assertThrows(UserNotExist.class, () -> userService.authorizeUser(VALID_EMAIL, VALID_PASSWORD));
        verify(repository, times(1)).getUserByEmail(VALID_EMAIL);
    }

    @Test
    void testGetEmail_Success() {
        when(repository.getEmail(1)).thenReturn(VALID_EMAIL);

        String email = userService.getEmail(1);

        assertEquals(VALID_EMAIL, email);
        verify(repository, times(1)).getEmail(1);
    }

    @Test
    void testGetUser_Success() {
        UserDAO mockUserDAO = new UserDAO(1, FIRST_NAME, LAST_NAME, VALID_EMAIL, "hash", "salt");
        when(repository.getUser(1)).thenReturn(mockUserDAO);

        User user = userService.getUser(1);

        assertNotNull(user);
        assertEquals(FIRST_NAME, user.getFirst_name());
        assertEquals(LAST_NAME, user.getLast_name());
        assertEquals(VALID_EMAIL, user.getEmail());
        verify(repository, times(1)).getUser(1);
    }

    @Test
    void testListUsers_Success() {
        UserDAO mockUser1 = new UserDAO(1, FIRST_NAME, LAST_NAME, "user1@example.com", "hash", "salt");
        UserDAO mockUser2 = new UserDAO(2, "Jane", "Doe", "user2@example.com", "hash", "salt");
        when(repository.listUsers(1, 10)).thenReturn(Arrays.asList(mockUser1, mockUser2));

        List<User> users = userService.listUsers(1, 10);

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(repository, times(1)).listUsers(1, 10);
    }

    @Test
    void testDeleteUser_Success() {
        userService.deleteUser(1);

        verify(repository, times(1)).deleteUser(1);
    }
}

