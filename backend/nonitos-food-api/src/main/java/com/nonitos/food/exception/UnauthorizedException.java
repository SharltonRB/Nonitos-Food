package com.nonitos.food.exception;

/**
 * Exception thrown when authentication or authorization fails.
 * Results in HTTP 401 Unauthorized response.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
public class UnauthorizedException extends RuntimeException {
    
    /**
     * Constructs a new UnauthorizedException with the specified message.
     *
     * @param message the detail message explaining the authentication failure
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
