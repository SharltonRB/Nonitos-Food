package com.nonitos.food.dto.dish;

import com.nonitos.food.model.Dish;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request DTO for creating a dish.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDishRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Category is required")
    private Dish.DishCategory category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Calories is required")
    @Min(value = 0, message = "Calories must be non-negative")
    private Integer calories;

    @NotNull(message = "Protein is required")
    @Min(value = 0, message = "Protein must be non-negative")
    private Integer protein;

    @NotNull(message = "Carbs is required")
    @Min(value = 0, message = "Carbs must be non-negative")
    private Integer carbs;

    @NotNull(message = "Fats is required")
    @Min(value = 0, message = "Fats must be non-negative")
    private Integer fats;

    private List<String> imageUrls;
    private List<Long> tagIds;
    private List<Long> allergenIds;
}
