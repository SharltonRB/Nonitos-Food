package com.nonitos.food.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

/**
 * Request DTO for updating a weekly menu.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWeeklyMenuRequest {

    private List<MenuDayRequest> menuDays;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuDayRequest {
        private DayOfWeek dayOfWeek;
        private String mealType;
        private Long dishId;
    }
}
