package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Catalog entity representing an allergy type.
 * Pre-populated with common allergens (dairy, nuts, gluten, etc.).
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "allergies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Allergy extends BaseEntity {

    /** Name of the allergy (e.g., "Dairy", "Nuts") */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /** Detailed description of the allergy */
    @Column(length = 200)
    private String description;
}
