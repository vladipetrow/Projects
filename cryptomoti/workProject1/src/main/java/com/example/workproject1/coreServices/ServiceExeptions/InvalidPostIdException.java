package com.example.workproject1.coreServices.ServiceExeptions;

public class InvalidPostIdException extends RuntimeException {
    public InvalidPostIdException(String message) {
        super(message);
    }
    
    public InvalidPostIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
