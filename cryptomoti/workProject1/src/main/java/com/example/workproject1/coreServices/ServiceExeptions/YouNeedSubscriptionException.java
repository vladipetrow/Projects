package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when user/agency needs a subscription to perform an action.
 */
public class YouNeedSubscriptionException extends BaseServiceException {
    
    public YouNeedSubscriptionException() {
        super("A subscription is required to perform this action");
    }
    
    public YouNeedSubscriptionException(String action) {
        super("A subscription is required to " + action);
    }
    
    public YouNeedSubscriptionException(String message, Throwable cause) {
        super(message, cause);
    }
}






