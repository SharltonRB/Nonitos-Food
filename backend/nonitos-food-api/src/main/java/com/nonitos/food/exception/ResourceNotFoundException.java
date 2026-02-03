package com.nonitos.food.exception;

/**
 * Exception thrown when a requested resource is not found.
 * Results in HTTP 404 Not Found response.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new ResourceNotFoundException with the specified message.
     *
     * @param message the detail message explaining which resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
