package com.nonitos.food.dto.menu;

import com.nonitos.food.model.MenuDay;
import com.nonitos.food.model.WeeklyMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for weekly menu information.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyMenuResponse {
    private Long id;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private WeeklyMenu.MenuStatus status;
    private Integer totalCalories;
    private Integer totalProtein;
    private Integer totalCarbs;
    private Integer totalFats;
    private Map<DayOfWeek, DayMenus> menusByDay;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DayMenus {
        private MealInfo breakfast;
        private MealInfo lunch;
        private MealInfo dinner;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealInfo {
        private Long menuDayId;
        private Long dishId;
        private String dishName;
        private Integer calories;
        private Integer protein;
        private Integer carbs;
        private Integer fats;
        private String imageUrl;
    }
}
