package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entity representing a dish in the catalog.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "dishes")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish extends BaseEntity {

    /** Unique name of the dish */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /** Description of the dish */
    @Column(length = 500)
    private String description;

    /** Category: BREAKFAST, LUNCH, DINNER */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DishCategory category;

    /** Price of the dish */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** Calories per serving */
    @Column(nullable = false)
    private Integer calories;

    /** Protein in grams */
    @Column(nullable = false)
    private Integer protein;

    /** Carbohydrates in grams */
    @Column(nullable = false)
    private Integer carbs;

    /** Fats in grams */
    @Column(nullable = false)
    private Integer fats;

    /** Whether the dish is currently available */
    @Column(nullable = false)
    private Boolean isActive;

    public enum DishCategory {
        BREAKFAST, LUNCH, DINNER
    }
}
