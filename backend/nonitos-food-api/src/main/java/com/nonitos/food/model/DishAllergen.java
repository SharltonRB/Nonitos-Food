package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Junction table linking dishes to allergens.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "dish_allergens", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"dish_id", "allergy_id"})
})
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishAllergen extends BaseEntity {

    /** Reference to the dish */
    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    /** Reference to the allergy */
    @ManyToOne
    @JoinColumn(name = "allergy_id", nullable = false)
    private Allergy allergy;
}
