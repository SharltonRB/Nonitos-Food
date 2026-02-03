package com.nonitos.food.dto.profile;

import com.nonitos.food.model.ClientPreferences;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating client preferences.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePreferencesRequest {

    @NotNull(message = "Meal plan type is required")
    private ClientPreferences.MealPlanType mealPlanType;

    @NotNull(message = "Meals per day is required")
    @Min(value = 1, message = "Meals per day must be at least 1")
    @Max(value = 3, message = "Meals per day must not exceed 3")
    private Integer mealsPerDay;

    @NotNull(message = "Include breakfast flag is required")
    private Boolean includeBreakfast;

    @NotNull(message = "Include lunch flag is required")
    private Boolean includeLunch;

    @NotNull(message = "Include dinner flag is required")
    private Boolean includeDinner;

    @Positive(message = "Target calories must be positive")
    private Integer targetCalories;

    @Positive(message = "Target protein must be positive")
    private Integer targetProtein;

    @Positive(message = "Target carbs must be positive")
    private Integer targetCarbs;

    @Positive(message = "Target fats must be positive")
    private Integer targetFats;
}
