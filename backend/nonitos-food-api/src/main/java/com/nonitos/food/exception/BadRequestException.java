package com.nonitos.food.exception;

/**
 * Exception thrown when a client request is invalid or malformed.
 * Results in HTTP 400 Bad Request response.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
public class BadRequestException extends RuntimeException {
    
    /**
     * Constructs a new BadRequestException with the specified message.
     *
     * @param message the detail message explaining why the request is invalid
     */
    public BadRequestException(String message) {
        super(message);
    }
}
