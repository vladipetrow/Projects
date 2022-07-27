package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.MailgunService;
import com.example.workproject1.coreServices.UserService;
import com.example.workproject1.coreServices.models.User;
import com.example.workproject1.security.EmailWithPassword;
import com.example.workproject1.web.api.models.UserInput;
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
public class UserController {
    private final UserService userService;
    public  UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("user/login")
    public EmailWithPassword login(@RequestBody UserInput user) throws SQLException {

        int userID = userService.authorizeUser(user.email,user.password);

        String token = getJWTToken(String.valueOf(userID));

        return new EmailWithPassword(user.email, token);
    }

    @PostMapping("user/register")
    public User registration(@RequestBody UserInput registration) throws SQLException{

        MailgunService.sendMail("Successfull registration!","Welcome to Cryptomoti, your registration is" +
                " succesfull!","vladi.petrow@abv.bg");
        return userService.createUser(registration.first_name, registration.last_name,
                registration.email, registration.password);
    }
    @GetMapping("list/users")
    public List<User> listUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return userService.listUsers(page, pageSize);
    }

    @GetMapping(value = "get/user/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }


    @DeleteMapping(value = "delete/user/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    private String getJWTToken(String id) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

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
