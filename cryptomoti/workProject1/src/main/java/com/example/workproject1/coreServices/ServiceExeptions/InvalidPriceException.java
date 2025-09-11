package com.example.workproject1.coreServices.ServiceExeptions;

/**
 * Exception thrown when price validation fails.
 */
public class InvalidPriceException extends BaseServiceException {
    
    public InvalidPriceException() {
        super("Invalid price value");
    }
    
    public InvalidPriceException(int price) {
        super("Invalid price value: " + price + ". Price must be positive.");
    }
    
    public InvalidPriceException(String message) {
        super(message);
    }
}






