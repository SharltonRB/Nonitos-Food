package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Catalog entity representing a dietary restriction type.
 * Pre-populated with common restrictions (vegetarian, vegan, keto, etc.).
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "dietary_restrictions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DietaryRestriction extends BaseEntity {

    /** Name of the dietary restriction (e.g., "Vegetarian", "Keto") */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /** Detailed description of the restriction */
    @Column(length = 200)
    private String description;
}
