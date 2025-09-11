package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFound extends BaseServiceException {
    
    public UserNotFound() {
        super("User not found");
    }
    
    public UserNotFound(String email) {
        super("User not found with email: " + email);
    }
    
    public UserNotFound(int userId) {
        super("User not found with ID: " + userId);
    }
}
