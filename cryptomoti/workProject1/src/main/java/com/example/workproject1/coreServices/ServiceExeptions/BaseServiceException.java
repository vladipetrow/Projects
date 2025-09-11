package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Base exception class for all service exceptions.
 * Provides consistent error handling and meaningful messages.
 */
public abstract class BaseServiceException extends RuntimeException {
    
    private final String errorCode;
    
    protected BaseServiceException(String message) {
        super(message);
        this.errorCode = this.getClass().getSimpleName();
    }
    
    protected BaseServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = this.getClass().getSimpleName();
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}




