package com.nonitos.food.dto.dish;

import com.nonitos.food.model.Dish;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request DTO for updating a dish.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDishRequest {

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private Dish.DishCategory category;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Calories must be non-negative")
    private Integer calories;

    @Min(value = 0, message = "Protein must be non-negative")
    private Integer protein;

    @Min(value = 0, message = "Carbs must be non-negative")
    private Integer carbs;

    @Min(value = 0, message = "Fats must be non-negative")
    private Integer fats;

    private Boolean isActive;
    private List<String> imageUrls;
    private List<Long> tagIds;
    private List<Long> allergenIds;
}
