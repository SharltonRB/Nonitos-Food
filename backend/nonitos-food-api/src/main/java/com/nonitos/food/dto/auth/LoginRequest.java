package com.nonitos.food.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for user login.
 * 
 * <p>Contains credentials required for authentication. Both fields are validated
 * before reaching the service layer.</p>
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User login request")
public class LoginRequest {

    /** User email address (must be valid email format) */
    @Schema(
        description = "User email address",
        example = "user@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    /** User password (plaintext, will be hashed for comparison) */
    @Schema(
        description = "User password",
        example = "SecurePass123",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Password is required")
    private String password;
}
