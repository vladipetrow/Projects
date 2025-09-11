package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when password validation fails.
 */
public class InvalidPasswordException extends BaseServiceException {
    
    public InvalidPasswordException() {
        super("Password does not meet requirements");
    }
    
    public InvalidPasswordException(String message) {
        super(message);
    }
    
    public InvalidPasswordException(int minLength) {
        super("Password must be at least " + minLength + " characters long");
    }
}






