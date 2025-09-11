package com.example.workproject1.security;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for generating secure peppers for password hashing.
 * 
 * Security Note: This class should only be used during application setup
 * to generate initial pepper values. Never use this in production runtime.
 */
public final class PepperGenerator {
    
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int PEPPER_LENGTH = 32; // 32 characters = 256 bits of entropy
    
    /**
     * Generates a cryptographically secure random pepper.
     * 
     * @return a base64-encoded random string suitable for use as a pepper
     */
    public static String generatePepper() {
        byte[] randomBytes = new byte[PEPPER_LENGTH];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getEncoder().withoutPadding().encodeToString(randomBytes);
    }
    
    /**
     * Generates a hex-encoded random pepper (alternative format).
     * 
     * @return a hex-encoded random string suitable for use as a pepper
     */
    public static String generateHexPepper() {
        byte[] randomBytes = new byte[PEPPER_LENGTH / 2]; // 16 bytes = 32 hex chars
        SECURE_RANDOM.nextBytes(randomBytes);
        StringBuilder hexString = new StringBuilder();
        for (byte b : randomBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    /**
     * Main method for generating peppers during application setup.
     * Run this to generate new pepper values for your application.properties.
     */
    public static void main(String[] args) {
        System.out.println("=== Secure Pepper Generator ===");
        System.out.println();
        System.out.println("Base64-encoded peppers (recommended):");
        System.out.println("app.security.user-pepper=" + generatePepper());
        System.out.println("app.security.agency-pepper=" + generatePepper());
        System.out.println();
        System.out.println("Hex-encoded peppers (alternative):");
        System.out.println("app.security.user-pepper=" + generateHexPepper());
        System.out.println("app.security.agency-pepper=" + generateHexPepper());
        System.out.println();
        System.out.println("IMPORTANT:");
        System.out.println("- Keep these values secret!");
        System.out.println("- Use different values for each environment");
        System.out.println("- Store them securely (environment variables, secret management)");
        System.out.println("- Never commit them to version control");
    }
}






