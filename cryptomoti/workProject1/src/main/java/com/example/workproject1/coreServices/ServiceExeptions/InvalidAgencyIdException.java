package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when an invalid agency ID is provided.
 */
public class InvalidAgencyIdException extends BaseServiceException {
    
    public InvalidAgencyIdException() {
        super("Invalid agency ID");
    }
    
    public InvalidAgencyIdException(int agencyId) {
        super("Invalid agency ID: " + agencyId);
    }
    
    public InvalidAgencyIdException(String message) {
        super(message);
    }
}






