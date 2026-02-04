package com.nonitos.food.controller;

import com.nonitos.food.dto.ApiResponse;
import com.nonitos.food.dto.menu.CreateWeeklyMenuRequest;
import com.nonitos.food.dto.menu.UpdateWeeklyMenuRequest;
import com.nonitos.food.dto.menu.WeeklyMenuResponse;
import com.nonitos.food.service.WeeklyMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for weekly menu management.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class WeeklyMenuController {

    private final WeeklyMenuService weeklyMenuService;

    /**
     * Creates a new weekly menu (Admin only).
     *
     * @param request the menu creation request
     * @return the created menu
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<WeeklyMenuResponse>> createMenu(
            @Valid @RequestBody CreateWeeklyMenuRequest request
    ) {
        WeeklyMenuResponse menu = weeklyMenuService.createWeeklyMenu(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Menu created successfully", menu));
    }

    /**
     * Gets a menu by ID (Public).
     *
     * @param id the menu ID
     * @return the menu
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WeeklyMenuResponse>> getMenuById(@PathVariable Long id) {
        WeeklyMenuResponse menu = weeklyMenuService.getMenuById(id);
        return ResponseEntity.ok(ApiResponse.success(menu));
    }

    /**
     * Gets all published menus (Public).
     *
     * @return list of published menus
     */
    @GetMapping("/published")
    public ResponseEntity<ApiResponse<List<WeeklyMenuResponse>>> getPublishedMenus() {
        List<WeeklyMenuResponse> menus = weeklyMenuService.getPublishedMenus();
        return ResponseEntity.ok(ApiResponse.success(menus));
    }

    /**
     * Updates a menu (Admin only).
     *
     * @param id the menu ID
     * @param request the update request
     * @return the updated menu
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<WeeklyMenuResponse>> updateMenu(
            @PathVariable Long id,
            @Valid @RequestBody UpdateWeeklyMenuRequest request
    ) {
        WeeklyMenuResponse menu = weeklyMenuService.updateMenu(id, request);
        return ResponseEntity.ok(ApiResponse.success("Menu updated successfully", menu));
    }

    /**
     * Publishes a menu (Admin only).
     *
     * @param id the menu ID
     * @return the published menu
     */
    @PostMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<WeeklyMenuResponse>> publishMenu(@PathVariable Long id) {
        WeeklyMenuResponse menu = weeklyMenuService.publishMenu(id);
        return ResponseEntity.ok(ApiResponse.success("Menu published successfully", menu));
    }

    /**
     * Deletes a menu (Admin only).
     *
     * @param id the menu ID
     * @return success response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable Long id) {
        weeklyMenuService.deleteMenu(id);
        return ResponseEntity.ok(ApiResponse.success("Menu deleted successfully", null));
    }
}
