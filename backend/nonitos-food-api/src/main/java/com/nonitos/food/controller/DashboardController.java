package com.nonitos.food.controller;

import com.nonitos.food.dto.ApiResponse;
import com.nonitos.food.dto.dashboard.DashboardMetricsResponse;
import com.nonitos.food.dto.dashboard.UpdateUserRequest;
import com.nonitos.food.dto.dashboard.UserManagementResponse;
import com.nonitos.food.service.DashboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for admin dashboard operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Gets dashboard metrics.
     *
     * @return dashboard metrics
     */
    @GetMapping("/dashboard/metrics")
    public ResponseEntity<ApiResponse<DashboardMetricsResponse>> getMetrics() {
        DashboardMetricsResponse metrics = dashboardService.getMetrics();
        return ResponseEntity.ok(ApiResponse.success(metrics));
    }

    /**
     * Gets all users with pagination.
     *
     * @param page page number
     * @param size page size
     * @param sortBy sort field
     * @param sortDir sort direction
     * @return page of users
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserManagementResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserManagementResponse> users = dashboardService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * Gets a user by ID.
     *
     * @param id the user ID
     * @return user information
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserManagementResponse>> getUserById(@PathVariable Long id) {
        UserManagementResponse user = dashboardService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /**
     * Updates a user.
     *
     * @param id the user ID
     * @param request the update request
     * @return updated user
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserManagementResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        UserManagementResponse user = dashboardService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", user));
    }

    /**
     * Deletes a user.
     *
     * @param id the user ID
     * @return success response
     */
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        dashboardService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
}
