package com.nonitos.food.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for refreshing access tokens.
 * 
 * <p>When an access token expires (after 30 minutes), clients can use their
 * refresh token to obtain a new access token without requiring the user to
 * log in again.</p>
 * 
 * <h2>Token refresh flow:</h2>
 * <ol>
 *   <li>Client detects access token is expired (401 response)</li>
 *   <li>Client sends refresh token to /api/auth/refresh</li>
 *   <li>Server validates refresh token (not expired, valid signature)</li>
 *   <li>Server issues new access token (and optionally new refresh token)</li>
 *   <li>Client retries original request with new access token</li>
 * </ol>
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Token refresh request")
public class TokenRefreshRequest {

    /** JWT refresh token (long-lived, 7 day expiration) */
    @Schema(
        description = "JWT refresh token",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
