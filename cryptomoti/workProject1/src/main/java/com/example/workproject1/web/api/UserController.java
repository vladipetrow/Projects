package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.UserService;
import com.example.workproject1.coreServices.models.User;
import com.example.workproject1.security.CookieUtil;
import com.example.workproject1.security.JwtUtil;
import com.example.workproject1.web.api.models.UserInput;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            int userId = userService.authorizeUser(user.email, user.password);

            // Generate tokens
            String accessToken = jwtUtil.generateToken(String.valueOf(userId), List.of("ROLE_USER"), ACCESS_TOKEN_VALIDITY_MS);
            String refreshToken = jwtUtil.generateToken(String.valueOf(userId), List.of("ROLE_USER"), REFRESH_TOKEN_VALIDITY_MS);

            // Create cookies using CookieUtil
            ResponseCookie accessCookie = cookieUtil.createCookie("Authorization", accessToken, ACCESS_TOKEN_VALIDITY_MS / 1000, true);
            ResponseCookie refreshCookie = cookieUtil.createCookie("RefreshToken", refreshToken, REFRESH_TOKEN_VALIDITY_MS / 1000, true);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(Map.of("message", "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid login credentials"));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        try {
            // Extract refresh token from cookies
            String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                    .filter(cookie -> "RefreshToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Refresh token missing"));

            // Validate the token
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new IllegalArgumentException("Invalid refresh token");
            }

            // Extract user ID (subject) from the token
            Claims claims = jwtUtil.getClaimsFromToken(refreshToken);
            String userId = claims.getSubject();

            // Generate a new access token
            String newAccessToken = jwtUtil.generateToken(userId, List.of("ROLE_USER"), ACCESS_TOKEN_VALIDITY_MS);

            // Create a new access cookie using CookieUtil
            ResponseCookie newAccessCookie = cookieUtil.createCookie("Authorization", newAccessToken, ACCESS_TOKEN_VALIDITY_MS / 1000, true);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, newAccessCookie.toString())
                    .body(Map.of("message", "Token refreshed successfully"));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Refresh token expired"));
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
            // Create user
            userService.createUser(registration.first_name, registration.last_name, registration.email, registration.password);

            return ResponseEntity.ok(Map.of("message", "Registration successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Registration failed"));
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
}
