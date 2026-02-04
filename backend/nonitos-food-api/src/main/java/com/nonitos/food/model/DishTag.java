package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Catalog entity representing a tag for dishes.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "dish_tags")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishTag extends BaseEntity {

    /** Name of the tag */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /** Description of the tag */
    @Column(length = 200)
    private String description;
}
