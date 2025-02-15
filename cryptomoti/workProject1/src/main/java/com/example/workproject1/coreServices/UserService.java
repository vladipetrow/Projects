package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.ServiceExeptions.InvalidEmail;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidParametersForUser;
import com.example.workproject1.coreServices.ServiceExeptions.MinimumLengthOfPasswordIs6;
import com.example.workproject1.coreServices.ServiceExeptions.UserNotExist;
import com.example.workproject1.coreServices.models.User;
import com.example.workproject1.repositories.UserRepository;
import com.example.workproject1.security.PasswordUtil;
import com.google.api.client.util.Value;
import org.springframework.dao.DataAccessException;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserService {
    private final UserRepository repository;
    private String PEPPER="7778fcc9c652f35d5d4463bf1d1c94abcd";

    private final String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public void createUser(String first_name, String last_name, String email, String password) {
        if(!patternMatches(email,regexPattern)){
            throw new InvalidEmail();
        }
        if(password.length() < 6){
            throw new MinimumLengthOfPasswordIs6();
        }
        String salt = UUID.randomUUID().toString();
        String hash = PasswordUtil.sha256(salt + password + PEPPER);
        try {
            repository.createUser(first_name, last_name, email, hash, salt);
        } catch (DataAccessException e) {
            throw new InvalidParametersForUser();
        }
    }

    public int authorizeUser(String email, String password) {
        User user = Mappers.fromUserDAO(repository.getUserByEmail(email));
        if (user == null) {
            throw new UserNotExist();
        }

        String hashedPassword = PasswordUtil.sha256(user.getSalt() + password + PEPPER);

        if (!hashedPassword.equals(user.getPasswordHash())) {
            throw new UserNotExist();
        }

        return user.getId();
    }

    public String getEmail(int id) {
        return repository.getEmail(id);
    }

    public User getUser(int id) {
        return Mappers.fromUserDAO(repository.getUser(id));
    }

    public List<User> listUsers(int page, int pageSize) {
        return repository.listUsers(page, pageSize)
                .stream()
                .map(Mappers::fromUserDAO)
                .collect(Collectors.toList());
    }

    public void deleteUser(int id) {
        repository.deleteUser(id);
    }
}
