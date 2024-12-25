package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.ServiceExeptions.InvalidEmail;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidParametersForUser;
import com.example.workproject1.coreServices.ServiceExeptions.MinimumLengthOfPasswordIs6;
import com.example.workproject1.coreServices.ServiceExeptions.UserNotExist;
import com.example.workproject1.coreServices.models.User;
import com.example.workproject1.repositories.UserRepository;
import com.example.workproject1.security.PasswordUtil;
import com.google.api.client.util.Value;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.dao.DataAccessException;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserService {
    private final UserRepository repository;

    @Value("${passwordPeperUser}")
    private static String PEPPER;

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

    public User createUser(String first_name, String last_name, String email, String password) {
        if(!patternMatches(email,regexPattern)){
            throw new InvalidEmail();
        }
        if(password.length() < 6){
            throw new MinimumLengthOfPasswordIs6();
        }
        String salt = UUID.randomUUID().toString();
        String hash = PasswordUtil.sha256(salt + password + PEPPER);
        try {
            return Mappers.fromUserDAO(
                    repository.createUser(first_name, last_name, email, hash, salt));
        } catch (DataAccessException e) {
            throw new InvalidParametersForUser();
        }
    }

    public int authorizeUser(String email, String password) {
        User user = Mappers.fromUserDAO(repository.getUserByEmail(email));
        if (user == null) {
            throw new UserNotExist();
        }

        String hashedPassword = PasswordUtil.sha256(user.salt + password + PEPPER);

        if (!hashedPassword.equals(user.passwordHash)) {
            throw new UserNotExist();
        }

        return user.id;
    }

    public String getRoleFromToken(String token2) {
        String bodyEncoded = token2.split("\\.")[1];
        String payloadAsString = new String(Base64.getUrlDecoder().decode(bodyEncoded));

        Map<String, Object> payload = null;
        try {
            payload = new JSONParser(payloadAsString).parseObject();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return payload.get("authorities").toString();
    }

    public  String getEmail(int id) {
        return repository.getEmail(id);
    }

    public int getIdFromToken(String token) {
        String bodyEncoded = token.split("\\.")[1];
        String payloadAsString = new String(Base64.getUrlDecoder().decode(bodyEncoded));

        Map<String, Object> payload = null;
        try {
            payload = new JSONParser(payloadAsString).parseObject();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return Integer.parseInt((String) payload.get("sub"));
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
