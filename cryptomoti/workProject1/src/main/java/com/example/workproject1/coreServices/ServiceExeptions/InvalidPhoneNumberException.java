package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when phone number validation fails.
 */
public class InvalidPhoneNumberException extends BaseServiceException {
    
    public InvalidPhoneNumberException() {
        super("Invalid phone number format");
    }
    
    public InvalidPhoneNumberException(String phoneNumber) {
        super("Invalid phone number format: " + phoneNumber);
    }
    
    public InvalidPhoneNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}






