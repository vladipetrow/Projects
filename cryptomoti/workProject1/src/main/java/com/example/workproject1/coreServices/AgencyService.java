package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.ServiceExeptions.*;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.repositories.AgencyRepository;
import com.example.workproject1.security.PasswordUtil;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AgencyService {
    private final AgencyRepository repository;
    private static final String PEPPER = "1115gci9b621f36d5d3313bf1d1c55aqws";
    private final String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public AgencyService(AgencyRepository repository) {
        this.repository = repository;
    }

    private static boolean patternMatches(String emailAddress, String regexPattern) {
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
        String hash = PasswordUtil.sha256(salt + password + PEPPER);
        try {
            return Mappers.fromAgencyDAO(
                    repository.createAgency(nameOfAgency, email, hash, salt, phone_number, address));
        } catch (DataAccessException e) {
            throw new CreateAgencyException();
        }
    }

    public int authorizeAgency(String email, String password) {
        Agency agency = Mappers.fromAgencyDAO(repository.getAgencyByEmail(email));
        if (agency == null) {
            throw new AgencyNotFound();
        }

        String hashedPassword = PasswordUtil.sha256(agency.getSalt() + password + PEPPER);

        if (!hashedPassword.equals(agency.getPasswordHash())) {
            throw new UserNotExist();
        }

        return agency.getId();
    }


    public Agency getAgency(int id) {
        return Mappers.fromAgencyDAO(repository.getAgency(id));
    }

    public String getEmail(int id) {
        return repository.getEmail(id);
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
}
