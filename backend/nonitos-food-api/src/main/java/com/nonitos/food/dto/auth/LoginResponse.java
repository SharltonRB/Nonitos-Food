package com.nonitos.food.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Login response with JWT tokens and user information")
public class LoginResponse {

    @Schema(description = "JWT access token for API authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "JWT refresh token for obtaining new access tokens", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "Token type", example = "Bearer", defaultValue = "Bearer")
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "Access token expiration time in seconds", example = "1800")
    private Long expiresIn;

    @Schema(description = "Authenticated user information")
    private UserInfo user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "User information included in login response")
    public static class UserInfo {
        @Schema(description = "User unique identifier", example = "1")
        private Long id;

        @Schema(description = "User email address", example = "user@example.com")
        private String email;

        @Schema(description = "User full name", example = "John Doe")
        private String fullName;

        @Schema(description = "User role", example = "CLIENT", allowableValues = {"ADMIN", "CLIENT"})
        private String role;

        @Schema(description = "Whether user email is verified", example = "true")
        private Boolean isEmailVerified;
    }
}
