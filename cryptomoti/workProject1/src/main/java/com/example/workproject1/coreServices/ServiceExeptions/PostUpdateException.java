package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when post update operations fail
 */
public class PostUpdateException extends RuntimeException {
    public PostUpdateException(String message) {
        super(message);
    }
    
    public PostUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
