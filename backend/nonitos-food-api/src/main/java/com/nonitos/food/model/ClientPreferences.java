package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity storing client preferences for meal planning.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "client_preferences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientPreferences extends BaseEntity {

    /** Reference to the client profile */
    @OneToOne
    @JoinColumn(name = "client_profile_id", nullable = false, unique = true)
    private ClientProfile clientProfile;

    /** Preferred meal plan type (STANDARD, CUSTOM) */
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_plan_type", nullable = false)
    private MealPlanType mealPlanType;

    /** Number of meals per day (1-3) */
    @Column(name = "meals_per_day", nullable = false)
    private Integer mealsPerDay;

    /** Include breakfast */
    @Column(name = "include_breakfast", nullable = false)
    private Boolean includeBreakfast;

    /** Include lunch */
    @Column(name = "include_lunch", nullable = false)
    private Boolean includeLunch;

    /** Include dinner */
    @Column(name = "include_dinner", nullable = false)
    private Boolean includeDinner;

    /** Target daily calories (optional) */
    @Column(name = "target_calories")
    private Integer targetCalories;

    /** Target daily protein in grams (optional) */
    @Column(name = "target_protein")
    private Integer targetProtein;

    /** Target daily carbs in grams (optional) */
    @Column(name = "target_carbs")
    private Integer targetCarbs;

    /** Target daily fats in grams (optional) */
    @Column(name = "target_fats")
    private Integer targetFats;

    public enum MealPlanType {
        STANDARD, CUSTOM
    }
}
