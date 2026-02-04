package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing an image associated with a dish.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "dish_images")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishImage extends BaseEntity {

    /** Reference to the dish */
    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    /** URL of the image */
    @Column(nullable = false, length = 500)
    private String imageUrl;

    /** Whether this is the primary image */
    @Column(nullable = false)
    private Boolean isPrimary;

    /** Display order */
    @Column(nullable = false)
    private Integer displayOrder;
}
