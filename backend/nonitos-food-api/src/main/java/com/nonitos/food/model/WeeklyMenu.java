package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entity representing a weekly menu.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "weekly_menus")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyMenu extends BaseEntity {

    /** Week start date (Monday) */
    @Column(nullable = false, unique = true)
    private LocalDate weekStartDate;

    /** Week end date (Sunday) */
    @Column(nullable = false)
    private LocalDate weekEndDate;

    /** Menu status: DRAFT, PUBLISHED, ARCHIVED */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MenuStatus status;

    /** Total calories for the week */
    @Column(nullable = false)
    private Integer totalCalories;

    /** Total protein for the week */
    @Column(nullable = false)
    private Integer totalProtein;

    /** Total carbs for the week */
    @Column(nullable = false)
    private Integer totalCarbs;

    /** Total fats for the week */
    @Column(nullable = false)
    private Integer totalFats;

    public enum MenuStatus {
        DRAFT, PUBLISHED, ARCHIVED
    }
}
