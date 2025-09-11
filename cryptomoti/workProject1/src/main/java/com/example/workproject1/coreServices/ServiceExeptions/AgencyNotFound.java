package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when an agency is not found.
 */
public class AgencyNotFound extends BaseServiceException {
    
    public AgencyNotFound() {
        super("Agency not found");
    }
    
    public AgencyNotFound(String email) {
        super("Agency not found with email: " + email);
    }
    
    public AgencyNotFound(int agencyId) {
        super("Agency not found with ID: " + agencyId);
    }
}