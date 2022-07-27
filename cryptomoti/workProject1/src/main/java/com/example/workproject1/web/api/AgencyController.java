package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.AgencyService;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.repositories.models.AgencyDAO;
import com.example.workproject1.security.EmailWithPassword;
import com.example.workproject1.web.WebExeptions.InvalidEmailOrPassword;
import com.example.workproject1.web.WebExeptions.InvalidPhoneNumberOrEmail;
import com.example.workproject1.web.api.models.AgencyInput;
import com.mysql.cj.exceptions.DataReadException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
@CrossOrigin
public class AgencyController {
    private final AgencyService agencyService;

    public  AgencyController(AgencyService agencyService) {
        this.agencyService = agencyService;
    }

    @PostMapping("agency/login")
    public EmailWithPassword login(@RequestBody AgencyInput agency) throws SQLException {

        int userID = agencyService.authorizeAgency(agency.email,agency.passwordHash);

        String token = getJWTToken(String.valueOf(userID));

        try {
            return new EmailWithPassword(agency.email, token);
        } catch (DataReadException e) {
            throw new InvalidEmailOrPassword();
        }
    }

    @PostMapping("agency/register")
    public Agency registration(@RequestBody AgencyInput registration) throws SQLException{
        try {
            return agencyService.createAgency(registration.name_of_agency, registration.email,
                    registration.passwordHash, registration.phone_number, registration.address);
        } catch (Exception e) {
            throw new InvalidPhoneNumberOrEmail();
        }
    }
    @GetMapping("list/agency")
    public List<Agency> listAgency(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return agencyService.listAgency(page, pageSize);
    }

    @GetMapping(value = "get/agency/{id}")
    public Agency getAgency(@PathVariable Integer id) {
        return agencyService.getAgency(id);
    }

    @DeleteMapping(value = "delete/agency/{id}")
    public void deleteAgency(@PathVariable Integer id) {
        agencyService.deleteAgency(id);
    }

    private String getJWTToken(String id) {
        String secretKey = "mySecretKeymySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_AGENCY");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(id)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60000000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}
