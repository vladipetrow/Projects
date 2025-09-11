package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when area validation fails.
 */
public class InvalidAreaException extends BaseServiceException {
    
    public InvalidAreaException() {
        super("Invalid area value");
    }
    
    public InvalidAreaException(int area) {
        super("Invalid area value: " + area + ". Area must be positive.");
    }
    
    public InvalidAreaException(String message) {
        super(message);
    }
}






