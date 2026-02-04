package com.nonitos.food.dto.menu;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Request DTO for creating a weekly menu.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWeeklyMenuRequest {

    @NotNull(message = "Week start date is required")
    private LocalDate weekStartDate;

    private List<MenuDayRequest> menuDays;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuDayRequest {
        @NotNull(message = "Day of week is required")
        private DayOfWeek dayOfWeek;

        @NotNull(message = "Meal type is required")
        private String mealType;

        @NotNull(message = "Dish ID is required")
        private Long dishId;
    }
}
