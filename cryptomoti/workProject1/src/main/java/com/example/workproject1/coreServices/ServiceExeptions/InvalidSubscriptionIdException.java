package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when an invalid subscription ID is provided.
 */
public class InvalidSubscriptionIdException extends BaseServiceException {
    public InvalidSubscriptionIdException() {
        super("Invalid subscription ID provided");
    }
    
    public InvalidSubscriptionIdException(int subscriptionId) {
        super("Invalid subscription ID: " + subscriptionId);
    }
    
    public InvalidSubscriptionIdException(String message) {
        super(message);
    }
}