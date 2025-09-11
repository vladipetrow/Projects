package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when post delete operations fail
 */
public class PostDeleteException extends RuntimeException {
    public PostDeleteException(String message) {
        super(message);
    }
    
    public PostDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
