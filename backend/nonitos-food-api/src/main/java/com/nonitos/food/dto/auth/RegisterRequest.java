package com.nonitos.food.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for user registration.
 * 
 * <p>Contains all required information to create a new user account. All fields
 * are validated using Jakarta Bean Validation annotations before reaching the
 * service layer.</p>
 * 
 * <h2>Password requirements:</h2>
 * <ul>
 *   <li>Minimum 8 characters</li>
 *   <li>At least one uppercase letter</li>
 *   <li>At least one lowercase letter</li>
 *   <li>At least one number</li>
 * </ul>
 * 
 * <h2>Business rules:</h2>
 * <ul>
 *   <li>Email must be unique (checked at service layer)</li>
 *   <li>New users default to CLIENT role</li>
 *   <li>Email verification required before placing orders</li>
 *   <li>Phone number is optional but recommended for WhatsApp notifications</li>
 * </ul>
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User registration request")
public class RegisterRequest {

    /** User email address (must be unique in system) */
    @Schema(
        description = "User email address (must be unique)",
        example = "newuser@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /** User password (plaintext, will be hashed with BCrypt before storage) */
    @Schema(
        description = "User password (min 8 chars, must contain uppercase, lowercase, and number)",
        example = "SecurePass123",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 8
    )
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, and one number"
    )
    private String password;

    /** User full name */
    @Schema(
        description = "User full name",
        example = "John Doe",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    /** User phone number (optional, international format recommended) */
    @Schema(
        description = "User phone number (international format recommended)",
        example = "+506 8888-8888",
        maxLength = 20
    )
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;
}
