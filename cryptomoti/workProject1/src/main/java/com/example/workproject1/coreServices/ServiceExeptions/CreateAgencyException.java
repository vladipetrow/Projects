package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when agency creation fails.
 */
public class CreateAgencyException extends BaseServiceException {
    
    public CreateAgencyException() {
        super("Failed to create agency");
    }
    
    public CreateAgencyException(String message) {
        super(message);
    }
    
    public CreateAgencyException(String message, Throwable cause) {
        super(message, cause);
    }
}