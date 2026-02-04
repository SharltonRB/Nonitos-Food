package com.nonitos.food.dto.dashboard;

import com.nonitos.food.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response containing user information for admin management.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserManagementResponse {

    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private User.UserRole role;
    private Boolean isEmailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}
