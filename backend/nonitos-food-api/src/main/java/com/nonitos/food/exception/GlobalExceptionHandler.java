package com.nonitos.food.exception;

import com.nonitos.food.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * 
 * <p>Centralizes exception handling across all controllers, converting exceptions
 * into consistent API responses. This ensures clients always receive predictable
 * error responses regardless of where the exception occurred.</p>
 * 
 * <h2>Why centralized exception handling?</h2>
 * <ul>
 *   <li><b>Consistency:</b> All errors return same ApiResponse structure</li>
 *   <li><b>DRY principle:</b> No try-catch blocks in controllers</li>
 *   <li><b>Security:</b> Prevents stack traces from leaking to clients</li>
 *   <li><b>Maintainability:</b> Single place to modify error handling</li>
 * </ul>
 * 
 * <h2>HTTP status code mapping:</h2>
 * <ul>
 *   <li>400 Bad Request: Validation errors, BadRequestException</li>
 *   <li>401 Unauthorized: Authentication failures, UnauthorizedException</li>
 *   <li>404 Not Found: ResourceNotFoundException</li>
 *   <li>500 Internal Server Error: Unexpected exceptions</li>
 * </ul>
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handles ResourceNotFoundException.
     * Returns 404 Not Found with error message.
     *
     * @param ex the exception
     * @return 404 response with error message
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }
    
    /**
     * Handles BadRequestException.
     * Returns 400 Bad Request with error message.
     *
     * @param ex the exception
     * @return 400 response with error message
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }
    
    /**
     * Handles UnauthorizedException.
     * Returns 401 Unauthorized with error message.
     *
     * @param ex the exception
     * @return 401 response with error message
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ex.getMessage()));
    }
    
    /**
     * Handles BadCredentialsException from Spring Security.
     * Returns 401 Unauthorized with generic message (security best practice).
     *
     * @param ex the exception
     * @return 401 response with generic error message
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid credentials"));
    }
    
    /**
     * Handles validation errors from @Valid annotations.
     * Returns 400 Bad Request with field-specific error messages.
     * 
     * <p>Extracts all validation errors and returns them as a map where keys are
     * field names and values are error messages. This allows clients to display
     * errors next to the appropriate form fields.</p>
     *
     * @param ex the validation exception
     * @return 400 response with map of field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Validation failed", errors));
    }
    
    /**
     * Handles all other unexpected exceptions.
     * Returns 500 Internal Server Error with generic message.
     * 
     * <p>This is the catch-all handler for any exception not handled by more
     * specific handlers. Logs the full stack trace for debugging but returns
     * a safe error message to the client.</p>
     *
     * @param ex the exception
     * @return 500 response with error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred: " + ex.getMessage()));
    }
}
