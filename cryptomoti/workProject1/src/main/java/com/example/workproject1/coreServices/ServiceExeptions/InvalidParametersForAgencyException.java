package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when invalid parameters are provided for agency operations.
 */
public class InvalidParametersForAgencyException extends BaseServiceException {
    
    public InvalidParametersForAgencyException() {
        super("Invalid parameters provided for agency operation");
    }
    
    public InvalidParametersForAgencyException(String parameter) {
        super("Invalid parameter for agency operation: " + parameter);
    }
    
    public InvalidParametersForAgencyException(String message, Throwable cause) {
        super(message, cause);
    }
}






