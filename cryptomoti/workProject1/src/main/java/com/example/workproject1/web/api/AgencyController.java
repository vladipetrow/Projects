package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.AgencyService;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.security.CookieUtil;
import com.example.workproject1.security.JwtUtil;
import com.example.workproject1.web.WebExeptions.InvalidPhoneNumberOrEmail;
import com.example.workproject1.web.api.models.AgencyInput;
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
@RequestMapping()
@CrossOrigin
public class AgencyController {

    private final AgencyService agencyService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    public AgencyController(AgencyService agencyService, JwtUtil jwtUtil, CookieUtil cookieUtil) {
        this.agencyService = agencyService;
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    @PostMapping("agency/login")
    public ResponseEntity<?> login(@RequestBody AgencyInput agency) {
        try {
            int agencyId = agencyService.authorizeAgency(agency.getEmail(), agency.getPasswordHash());

            String accessToken = jwtUtil.generateToken(String.valueOf(agencyId), List.of("ROLE_AGENCY"), ACCESS_TOKEN_VALIDITY_MS);

            String refreshToken = jwtUtil.generateToken(String.valueOf(agencyId), List.of("ROLE_AGENCY"), REFRESH_TOKEN_VALIDITY_MS);

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

    @PostMapping("/agency/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "RefreshToken", required = false) String refreshToken) {
        try {
            if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
                throw new IllegalArgumentException("Invalid or missing refresh token");
            }

            Claims claims = jwtUtil.getClaimsFromToken(refreshToken);
            String agencyId = claims.getSubject();

            String newAccessToken = jwtUtil.generateToken(agencyId, List.of("ROLE_AGENCY"), ACCESS_TOKEN_VALIDITY_MS);

            ResponseCookie newAccessCookie = cookieUtil.createCookie("Authorization", newAccessToken, ACCESS_TOKEN_VALIDITY_MS / 1000, true);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, newAccessCookie.toString())
                    .body(Map.of("message", "Token refreshed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/agency/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie accessCookie = cookieUtil.createEmptyCookie("Authorization", true);
        ResponseCookie refreshCookie = cookieUtil.createEmptyCookie("RefreshToken", true);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(Map.of("message", "Logged out successfully"));
    }


    @PostMapping("agency/register")
    public ResponseEntity<?> registration(@RequestBody AgencyInput registration) {
        try {
            Agency newAgency = agencyService.createAgency(
                    registration.getNameOfAgency(),
                    registration.getEmail(),
                    registration.getPasswordHash(),
                    registration.getPhoneNumber(),
                    registration.getAddress()
            );

            return ResponseEntity.ok(Map.of("message", "Registration successful", "agency", newAgency));
        } catch (InvalidPhoneNumberOrEmail e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid phone number or email"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @GetMapping("list/agency")
    public ResponseEntity<?> listAgency(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            List<Agency> agencies = agencyService.listAgency(page, pageSize);
            return ResponseEntity.ok(agencies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to fetch agencies"));
        }
    }

    @GetMapping(value = "get/agency/{id}")
    public ResponseEntity<?> getAgency(@PathVariable Integer id) {
        try {
            Agency agency = agencyService.getAgency(id);
            return ResponseEntity.ok(agency);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Agency not found"));
        }
    }

    @DeleteMapping(value = "delete/agency/{id}")
    public ResponseEntity<?> deleteAgency(@PathVariable Integer id) {
        try {
            agencyService.deleteAgency(id);
            return ResponseEntity.ok(Map.of("message", "Agency deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to delete agency"));
        }
    }
}

