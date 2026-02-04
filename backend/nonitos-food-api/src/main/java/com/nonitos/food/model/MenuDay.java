package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;

/**
 * Entity representing a day in a weekly menu with assigned dishes.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "menu_days", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"weekly_menu_id", "day_of_week", "meal_type"})
})
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDay extends BaseEntity {

    /** Reference to the weekly menu */
    @ManyToOne
    @JoinColumn(name = "weekly_menu_id", nullable = false)
    private WeeklyMenu weeklyMenu;

    /** Day of the week */
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private DayOfWeek dayOfWeek;

    /** Meal type: BREAKFAST, LUNCH, DINNER */
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false, length = 20)
    private MealType mealType;

    /** Reference to the dish */
    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    public enum MealType {
        BREAKFAST, LUNCH, DINNER
    }
}
