package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when user/agency exceeds maximum number of posts.
 */
public class MaxNumberOfPostsException extends BaseServiceException {
    
    public MaxNumberOfPostsException() {
        super("Maximum number of posts exceeded");
    }
    
    public MaxNumberOfPostsException(int currentCount, int maxAllowed) {
        super("Maximum number of posts exceeded. Current: " + currentCount + ", Max allowed: " + maxAllowed);
    }
    
    public MaxNumberOfPostsException(String message) {
        super(message);
    }
}






