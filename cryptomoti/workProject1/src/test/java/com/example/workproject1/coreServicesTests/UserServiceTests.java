package com.example.workproject1.coreServicesTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.workproject1.coreServices.ServiceExeptions.InvalidEmailException;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidParametersForUserException;
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
                .thenReturn(new UserDAO.Builder()
                        .id(1)
                        .firstName(FIRST_NAME)
                        .lastName(LAST_NAME)
                        .email(VALID_EMAIL)
                        .build());

        userService.createUser(FIRST_NAME, LAST_NAME, VALID_EMAIL, VALID_PASSWORD);

        verify(repository, times(1)).createUser(eq(FIRST_NAME), eq(LAST_NAME), eq(VALID_EMAIL), anyString(), anyString());
    }


    @Test
    void testCreateUser_InvalidEmail() {
        assertThrows(InvalidEmailException.class, () -> userService.createUser(FIRST_NAME, LAST_NAME, INVALID_EMAIL, VALID_PASSWORD));
        verifyNoInteractions(repository);
    }

    // @Test
    // void testCreateUser_ShortPassword() {
    //     // Act & Assert
    //     assertThrows(MinimumLengthOfPasswordIs6.class, () -> userService.createUser(FIRST_NAME, LAST_NAME, VALID_EMAIL, SHORT_PASSWORD));
    //     verifyNoInteractions(repository);
    // }

    @Test
    void testCreateUser_InvalidParametersForUser() {
        doThrow(new DataAccessException("Invalid parameters") {}).when(repository)
                .createUser(anyString(), anyString(), anyString(), anyString(), anyString());

        assertThrows(InvalidParametersForUserException.class, () ->
                userService.createUser(FIRST_NAME, LAST_NAME, VALID_EMAIL, VALID_PASSWORD)
        );

        verify(repository, times(1)).createUser(eq(FIRST_NAME), eq(LAST_NAME), eq(VALID_EMAIL), anyString(), anyString());
    }


    @Test
    void testAuthorizeUser_Success() {
        String salt = UUID.randomUUID().toString();
        String hash = PasswordUtil.sha256(salt + VALID_PASSWORD + PEPPER_USER);
        UserDAO mockUserDAO = new UserDAO.Builder()
                .id(1)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(VALID_EMAIL)
                .passwordHash(hash)
                .salt(salt)
                .build();
        when(repository.getUserByEmail(VALID_EMAIL)).thenReturn(mockUserDAO);

        int userId = userService.authorizeUser(VALID_EMAIL, VALID_PASSWORD);

        assertEquals(1, userId);
        verify(repository, times(1)).getUserByEmail(VALID_EMAIL);
    }


    @Test
    void testAuthorizeUser_InvalidPassword() {
        String salt = UUID.randomUUID().toString();
        String incorrectHash = PasswordUtil.sha256(salt + "wrongPassword" + PEPPER_USER);
        UserDAO mockUserDAO = new UserDAO.Builder()
                .id(1)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(VALID_EMAIL)
                .passwordHash(incorrectHash)
                .salt(salt)
                .build();
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
        UserDAO mockUserDAO = new UserDAO.Builder()
                .id(1)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(VALID_EMAIL)
                .passwordHash("hash")
                .salt("salt")
                .build();
        when(repository.getUser(1)).thenReturn(mockUserDAO);

        User user = userService.getUser(1);

        assertNotNull(user);
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(VALID_EMAIL, user.getEmail());
        verify(repository, times(1)).getUser(1);
    }

    @Test
    void testListUsers_Success() {
        UserDAO mockUser1 = new UserDAO.Builder()
                .id(1)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email("user1@example.com")
                .passwordHash("hash")
                .salt("salt")
                .build();
        UserDAO mockUser2 = new UserDAO.Builder()
                .id(2)
                .firstName("Jane")
                .lastName("Doe")
                .email("user2@example.com")
                .passwordHash("hash")
                .salt("salt")
                .build();
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

