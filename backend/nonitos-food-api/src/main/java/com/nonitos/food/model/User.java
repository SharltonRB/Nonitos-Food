package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a user in the system.
 * 
 * <p>This is the core user entity that stores authentication credentials, profile
 * information, and account metadata. It serves both CLIENT and ADMIN users, with
 * role-based access control determining available features.</p>
 * 
 * <h2>Design Decisions:</h2>
 * 
 * <h3>Why single User table for all roles?</h3>
 * <p>We use a single table with a role enum instead of separate Admin/Client tables:
 * <ul>
 *   <li><b>Simplicity:</b> Easier to manage, query, and maintain one user table</li>
 *   <li><b>Flexibility:</b> Easy to add new roles (SUPER_ADMIN, MODERATOR, etc.)</li>
 *   <li><b>Shared authentication:</b> All users authenticate the same way</li>
 *   <li><b>Performance:</b> No joins needed for basic user operations</li>
 *   <li><b>Role changes:</b> Can promote CLIENT to ADMIN without data migration</li>
 * </ul>
 * If roles become very different with many role-specific fields, we might refactor
 * to separate tables with inheritance.
 * 
 * <h3>Password storage:</h3>
 * <p>Passwords are hashed with BCrypt (strength 12) before storage. We NEVER store
 * plaintext passwords. BCrypt is chosen because:
 * <ul>
 *   <li>Adaptive: Can increase strength as hardware improves</li>
 *   <li>Salted: Each password has unique salt (prevents rainbow tables)</li>
 *   <li>Slow: Intentionally slow to prevent brute-force attacks</li>
 *   <li>Industry standard: Proven security track record</li>
 * </ul>
 * 
 * <h3>Email verification flow:</h3>
 * <p>We track verification with three fields:
 * <ul>
 *   <li><b>isEmailVerified:</b> Boolean flag for quick checks</li>
 *   <li><b>emailVerificationToken:</b> UUID token sent in verification email</li>
 *   <li><b>emailVerificationExpiresAt:</b> 24-hour expiration for security</li>
 * </ul>
 * After verification, token fields are cleared (null) to prevent reuse.
 * 
 * <h3>Phone number storage:</h3>
 * <p>Phone numbers are stored as strings (not normalized) because:
 * <ul>
 *   <li>International formats vary widely</li>
 *   <li>Users may include formatting (spaces, dashes, parentheses)</li>
 *   <li>Used for WhatsApp notifications (needs exact format user provided)</li>
 *   <li>Not used for authentication (email only)</li>
 * </ul>
 * We validate format at API level but store as-is for flexibility.
 * 
 * <h3>Audit fields (from BaseEntity):</h3>
 * <p>Inherits createdAt and updatedAt from BaseEntity for:
 * <ul>
 *   <li>Compliance: Track when accounts were created</li>
 *   <li>Analytics: User registration trends</li>
 *   <li>Debugging: When was account last modified</li>
 *   <li>Security: Detect suspicious account modifications</li>
 * </ul>
 * 
 * <h2>Business Rules:</h2>
 * <ul>
 *   <li>Email must be unique (enforced by database constraint)</li>
 *   <li>New users default to CLIENT role</li>
 *   <li>Email verification required for placing orders</li>
 *   <li>Password reset requires verified email</li>
 *   <li>lastLoginAt used for inactive account cleanup (6+ months)</li>
 * </ul>
 *
 * @author Nonito's Food Team
 * @since 1.0
 * @see BaseEntity for audit fields (id, createdAt, updatedAt)
 * @see UserRole for available user roles
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    /** User email address (unique, used for login) */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /** Encrypted user password (BCrypt) */
    @Column(nullable = false)
    private String password;

    /** User full name */
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    /** User phone number (international format) */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    /** User role (ADMIN or CLIENT) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    /** Whether user email has been verified */
    @Builder.Default
    @Column(name = "is_email_verified", nullable = false)
    private Boolean isEmailVerified = false;

    /** Token for email verification (valid for 24 hours) */
    @Column(name = "email_verification_token", length = 255)
    private String emailVerificationToken;

    /** Expiration timestamp for email verification token */
    @Column(name = "email_verification_expires_at")
    private LocalDateTime emailVerificationExpiresAt;

    /** Token for password reset (valid for 1 hour) */
    @Column(name = "password_reset_token", length = 255)
    private String passwordResetToken;

    /** Expiration timestamp for password reset token */
    @Column(name = "password_reset_expires_at")
    private LocalDateTime passwordResetExpiresAt;

    /** Timestamp of user's last login */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * Enum representing user roles in the system.
     */
    public enum UserRole {
        /** Administrator with full system access */
        ADMIN,
        /** Regular client user */
        CLIENT
    }
}
