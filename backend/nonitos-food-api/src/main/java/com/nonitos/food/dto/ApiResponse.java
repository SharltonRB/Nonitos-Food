package com.nonitos.food.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard wrapper for all API responses.
 * 
 * <p>This generic response wrapper provides a consistent structure for all API
 * endpoints, making it easier for clients to handle responses uniformly.</p>
 * 
 * <h2>Why use a response wrapper?</h2>
 * <ul>
 *   <li><b>Consistency:</b> All endpoints return the same structure</li>
 *   <li><b>Error handling:</b> Success/error responses have same format</li>
 *   <li><b>Client simplicity:</b> Frontend can handle all responses uniformly</li>
 *   <li><b>Metadata:</b> Easy to add fields (timestamp, requestId, etc.) later</li>
 * </ul>
 * 
 * <h2>Usage examples:</h2>
 * <pre>
 * // Success with data
 * return ApiResponse.success(userData);
 * 
 * // Success with custom message
 * return ApiResponse.success("User created successfully", userData);
 * 
 * // Error response
 * return ApiResponse.error("Invalid credentials");
 * </pre>
 *
 * @param <T> the type of data payload
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard API response wrapper")
public class ApiResponse<T> {

    /** Indicates if the operation was successful */
    @Schema(description = "Indicates if the operation was successful", example = "true")
    private boolean success;

    /** Human-readable message about the operation result */
    @Schema(description = "Human-readable message about the operation result", example = "Operation successful")
    private String message;

    /** Response data payload (null for errors) */
    @Schema(description = "Response data payload")
    private T data;
    
    /**
     * Creates a success response with default message.
     *
     * @param <T> the type of data
     * @param data the response data
     * @return success response with data
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operation successful", data);
    }
    
    /**
     * Creates a success response with custom message.
     *
     * @param <T> the type of data
     * @param message custom success message
     * @param data the response data
     * @return success response with custom message and data
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    /**
     * Creates an error response with no data.
     *
     * @param <T> the type of data
     * @param message error message
     * @return error response with message
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
