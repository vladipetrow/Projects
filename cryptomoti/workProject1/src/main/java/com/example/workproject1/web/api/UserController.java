package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.MailgunService;
import com.example.workproject1.coreServices.ServiceExeptions.EmailAlreadyExistsException;
import com.example.workproject1.coreServices.UserService;
import com.example.workproject1.coreServices.models.User;
import com.example.workproject1.security.CookieUtil;
import com.example.workproject1.security.JwtUtil;
import com.example.workproject1.web.api.models.UserInput;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.workproject1.AppConstants.ACCESS_TOKEN_VALIDITY_MS;
import static com.example.workproject1.AppConstants.REFRESH_TOKEN_VALIDITY_MS;

@RestController
@RequestMapping
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    public UserController(UserService userService, JwtUtil jwtUtil, CookieUtil cookieUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    @PostMapping("user/login")
    public ResponseEntity<?> login(@RequestBody UserInput user) {
        try {
            int userId = userService.authorizeUser(user.getEmail(), user.getPassword());

            String accessToken = jwtUtil.generateToken(String.valueOf(userId), List.of("ROLE_USER"), ACCESS_TOKEN_VALIDITY_MS);
            String refreshToken = jwtUtil.generateToken(String.valueOf(userId), List.of("ROLE_USER"), REFRESH_TOKEN_VALIDITY_MS);

            ResponseCookie accessCookie = cookieUtil.createCookie("Authorization", accessToken, ACCESS_TOKEN_VALIDITY_MS / 1000, true);
            ResponseCookie refreshCookie = cookieUtil.createCookie("RefreshToken", refreshToken, REFRESH_TOKEN_VALIDITY_MS / 1000, true);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(Map.of("message", "Login successful", "role", "ROLE_USER"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid login credentials"));
        }
    }

    @PostMapping("user/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "RefreshToken", required = false) String refreshToken) {
        try {
            // Validate Refresh Token
            if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
                throw new IllegalArgumentException("Invalid or missing refresh token");
            }

            Claims claims = jwtUtil.getClaimsFromToken(refreshToken);
            String userId = claims.getSubject();

            String newAccessToken = jwtUtil.generateToken(userId, List.of("ROLE_USER"), ACCESS_TOKEN_VALIDITY_MS);

            ResponseCookie newAccessCookie = cookieUtil.createCookie("Authorization", newAccessToken, ACCESS_TOKEN_VALIDITY_MS / 1000, true);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, newAccessCookie.toString())
                    .body(Map.of("message", "Token refreshed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Create empty cookies to clear tokens
        ResponseCookie accessCookie = cookieUtil.createEmptyCookie("Authorization", true);
        ResponseCookie refreshCookie = cookieUtil.createEmptyCookie("RefreshToken", true);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("user/register")
    public ResponseEntity<?> register(@RequestBody UserInput registration) {
        try {
            userService.createUser(registration.getFirstName(), registration.getLastName(), registration.getEmail(), registration.getPassword());

            return ResponseEntity.ok(Map.of("message", "Registration successful"));
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already exists. Please use a different email address."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    @GetMapping("list/users")
    public List<User> listUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return userService.listUsers(page, pageSize);
    }

    @GetMapping("get/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        try {
            User user = userService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
    }

    @DeleteMapping("delete/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User deletion failed"));
        }
    }

    @GetMapping("auth/check")
    public ResponseEntity<?> checkAuth(@CookieValue(value = "Authorization", required = false) String token) {
        try {
            if (token == null || !jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("authenticated", false));
            }
            
            int userId = jwtUtil.getIdFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);
            
            return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "userId", userId,
                "role", role
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("authenticated", false));
        }
    }
}
