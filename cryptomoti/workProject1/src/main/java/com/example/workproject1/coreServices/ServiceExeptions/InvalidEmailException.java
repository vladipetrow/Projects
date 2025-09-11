package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when email validation fails.
 */
public class InvalidEmailException extends BaseServiceException {
    
    public InvalidEmailException() {
        super("Invalid email format");
    }
    
    public InvalidEmailException(String email) {
        super("Invalid email format: " + email);
    }
}






