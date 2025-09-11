package com.example.workproject1.coreServices.ServiceExeptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email already exists");
    }
    
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}


