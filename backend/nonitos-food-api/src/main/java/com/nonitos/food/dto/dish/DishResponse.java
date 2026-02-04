package com.nonitos.food.dto.dish;

import com.nonitos.food.model.Dish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO for dish information.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishResponse {
    private Long id;
    private String name;
    private String description;
    private Dish.DishCategory category;
    private BigDecimal price;
    private Integer calories;
    private Integer protein;
    private Integer carbs;
    private Integer fats;
    private Boolean isActive;
    private List<String> images;
    private List<String> tags;
    private List<String> allergens;
}
