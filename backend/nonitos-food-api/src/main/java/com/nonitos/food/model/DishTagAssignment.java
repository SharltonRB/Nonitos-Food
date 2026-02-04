package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Junction table linking dishes to tags.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "dish_tag_assignments", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"dish_id", "tag_id"})
})
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishTagAssignment extends BaseEntity {

    /** Reference to the dish */
    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    /** Reference to the tag */
    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private DishTag tag;
}
