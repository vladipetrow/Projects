package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.ServiceExeptions.InvalidEmailException;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidPasswordException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static com.example.workproject1.AppConstants.*;

/**
 * Centralized validation service to eliminate code duplication.
 */
@Service
public class ValidationService {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    /**
     * Validates email format using regex pattern.
     * 
     * @param email email to validate
     * @throws InvalidEmailException if email format is invalid
     */
    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidEmailException("Email cannot be null or empty");
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailException(email);
        }
    }
    
    /**
     * Validates password meets minimum requirements.
     * 
     * @param password password to validate
     * @throws InvalidPasswordException if password doesn't meet requirements
     */
    public void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidPasswordException("Password cannot be null or empty");
        }
        
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidPasswordException(MIN_PASSWORD_LENGTH);
        }
    }
    
    /**
     * Validates phone number format.
     * 
     * @param phoneNumber phone number to validate
     * @throws IllegalArgumentException if phone number format is invalid
     */
    public void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        
        if (phoneNumber.length() != PHONE_NUMBER_LENGTH) {
            throw new IllegalArgumentException("Phone number must be exactly " + PHONE_NUMBER_LENGTH + " digits");
        }
        
        if (!phoneNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Phone number must contain only digits");
        }
    }
    
    /**
     * Validates that a string is not null or empty.
     * 
     * @param value value to validate
     * @param fieldName name of the field for error message
     * @throws IllegalArgumentException if value is null or empty
     */
    public void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }
    
    /**
     * Validates that a number is positive.
     * 
     * @param value value to validate
     * @param fieldName name of the field for error message
     * @throws IllegalArgumentException if value is not positive
     */
    public void validatePositive(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " must be positive, got: " + value);
        }
    }
}






