package com.nonitos.food.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for successful login.
 * 
 * <p>Contains JWT tokens for authentication and basic user information. The access
 * token is used for API requests, while the refresh token is used to obtain new
 * access tokens when they expire.</p>
 * 
 * <h2>Token strategy:</h2>
 * <ul>
 *   <li><b>Access token:</b> Short-lived (30 minutes), used for API authentication</li>
 *   <li><b>Refresh token:</b> Long-lived (7 days), used to get new access tokens</li>
 *   <li><b>Token type:</b> Always "Bearer" for HTTP Authorization header</li>
 * </ul>
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Login response with JWT tokens and user information")
public class LoginResponse {

    /** JWT access token for API authentication (30 min expiration) */
    @Schema(description = "JWT access token for API authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    /** JWT refresh token for obtaining new access tokens (7 day expiration) */
    @Schema(description = "JWT refresh token for obtaining new access tokens", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    /** Token type (always "Bearer") */
    @Schema(description = "Token type", example = "Bearer", defaultValue = "Bearer")
    @Builder.Default
    private String tokenType = "Bearer";

    /** Access token expiration time in seconds (1800 = 30 minutes) */
    @Schema(description = "Access token expiration time in seconds", example = "1800")
    private Long expiresIn;

    /** Authenticated user information */
    @Schema(description = "Authenticated user information")
    private UserInfo user;

    /**
     * Nested DTO containing basic user information.
     * 
     * <p>Includes only essential user data needed by the frontend. Sensitive
     * information like password is never included.</p>
     *
     * @author Nonito's Food Team
     * @since 1.0
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "User information included in login response")
    public static class UserInfo {
        /** User unique identifier */
        @Schema(description = "User unique identifier", example = "1")
        private Long id;

        /** User email address */
        @Schema(description = "User email address", example = "user@example.com")
        private String email;

        /** User full name */
        @Schema(description = "User full name", example = "John Doe")
        private String fullName;

        /** User role (ADMIN or CLIENT) */
        @Schema(description = "User role", example = "CLIENT", allowableValues = {"ADMIN", "CLIENT"})
        private String role;

        /** Whether user email is verified */
        @Schema(description = "Whether user email is verified", example = "true")
        private Boolean isEmailVerified;
    }
}
