package com.nonitos.food.controller;

import com.nonitos.food.dto.ApiResponse;
import com.nonitos.food.dto.dish.CreateDishRequest;
import com.nonitos.food.dto.dish.DishResponse;
import com.nonitos.food.dto.dish.UpdateDishRequest;
import com.nonitos.food.model.Dish;
import com.nonitos.food.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST controller for dish management.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    /**
     * Creates a new dish (Admin only).
     *
     * @param request the dish creation request
     * @return the created dish
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DishResponse>> createDish(
            @Valid @RequestBody CreateDishRequest request
    ) {
        DishResponse dish = dishService.createDish(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Dish created successfully", dish));
    }

    /**
     * Gets a dish by ID (Public).
     *
     * @param id the dish ID
     * @return the dish
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DishResponse>> getDishById(@PathVariable Long id) {
        DishResponse dish = dishService.getDishById(id);
        return ResponseEntity.ok(ApiResponse.success(dish));
    }

    /**
     * Gets all dishes with filters (Public).
     *
     * @param category optional category filter
     * @param isActive optional active status filter
     * @param minPrice optional minimum price filter
     * @param maxPrice optional maximum price filter
     * @param tagName optional tag name filter
     * @param page page number
     * @param size page size
     * @param sortBy sort field
     * @param sortDir sort direction
     * @return page of dishes
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<DishResponse>>> getAllDishes(
            @RequestParam(required = false) Dish.DishCategory category,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String tagName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DishResponse> dishes = dishService.getAllDishes(
                category, isActive, minPrice, maxPrice, tagName, pageable
        );
        return ResponseEntity.ok(ApiResponse.success(dishes));
    }

    /**
     * Updates a dish (Admin only).
     *
     * @param id the dish ID
     * @param request the update request
     * @return the updated dish
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DishResponse>> updateDish(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDishRequest request
    ) {
        DishResponse dish = dishService.updateDish(id, request);
        return ResponseEntity.ok(ApiResponse.success("Dish updated successfully", dish));
    }

    /**
     * Deletes a dish (Admin only).
     *
     * @param id the dish ID
     * @return success response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.ok(ApiResponse.success("Dish deleted successfully", null));
    }
}
