package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when invalid parameters are provided for user operations.
 */
public class InvalidParametersForUserException extends BaseServiceException {
    
    public InvalidParametersForUserException() {
        super("Invalid parameters provided for user operation");
    }
    
    public InvalidParametersForUserException(String parameter) {
        super("Invalid parameter for user operation: " + parameter);
    }
    
    public InvalidParametersForUserException(String message, Throwable cause) {
        super(message, cause);
    }
}






