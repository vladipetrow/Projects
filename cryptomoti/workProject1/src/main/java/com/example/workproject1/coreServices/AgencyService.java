package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.ServiceExeptions.*;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.repositories.AgencyRepository;
import org.springframework.dao.DataAccessException;

import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AgencyService {
    private final AgencyRepository repository;
    private final String PEPPER = "6668fcd5d652fatyq789v3bf1d1c94abcd";
    private final String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public AgencyService(AgencyRepository repository) {
        this.repository = repository;
    }
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
    public Agency createAgency(String nameOfAgency, String email, String password, String phone_number, String address) {
        if(!patternMatches(email,regexPattern)){
            throw new InvalidEmail();
        }
        if(password.length() < 6){
            throw new MinimumLengthOfPasswordIs6();
        }
        if(phone_number.length() != 10){
            throw new InvalidPhoneNumber();
        }
        String salt = UUID.randomUUID().toString();
        String hash = sha256(salt + password + PEPPER);
        try {
            return Mappers.fromAgencyDAO(
                    repository.createAgency(nameOfAgency, email, hash, salt, phone_number, address));
        } catch (DataAccessException e) {
            throw new CreateAgencyException();
        }
    }

    public int authorizeAgency(String email, String password) {
        Agency agency;
        agency = Mappers.fromAgencyDAO(repository.getAgencyByEmail(email));
        String hash = sha256(agency.salt + password + PEPPER);

        if (!hash.equals(agency.passwordHash))
            throw new InvalidParameterException();
        return agency.id;
    }


    public Agency getAgency(int id) {
        return Mappers.fromAgencyDAO(repository.getAgency(id));
    }

    public List<Agency> listAgency(int page, int pageSize) {
        return repository.listAgency(page, pageSize)
                .stream()
                .map(Mappers::fromAgencyDAO)
                .collect(Collectors.toList());
    }
    public void deleteAgency(int id) {
        try {
            repository.deleteAgency(id);
        } catch (DataAccessException e) {
            throw new InvalidAgencyId();
        }
    }
    private static String sha256(String originalString) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
