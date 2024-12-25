package com.example.workproject1.security;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    /**
     * Creates an HTTP-only secure cookie.
     *
     * @param name    The name of the cookie.
     * @param value   The value of the cookie.
     * @param maxAge  The maximum age of the cookie in seconds.
     * @param isSecure Whether the cookie should be marked as secure (for HTTPS).
     * @return A ResponseCookie object.
     */
    public ResponseCookie createCookie(String name, String value, long maxAge, boolean isSecure) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(isSecure) // Set this to true for HTTPS environments
                .path("/")
                .sameSite("Strict")
                .maxAge(maxAge)
                .build();
    }

    /**
     * Creates an empty cookie to clear an existing one.
     *
     * @param name    The name of the cookie.
     * @param isSecure Whether the cookie should be marked as secure (for HTTPS).
     * @return A ResponseCookie object with maxAge set to 0.
     */
    public ResponseCookie createEmptyCookie(String name, boolean isSecure) {
        return createCookie(name, "", 0, isSecure);
    }
}

