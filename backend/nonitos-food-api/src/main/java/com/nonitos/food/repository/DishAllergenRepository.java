package com.nonitos.food.repository;

import com.nonitos.food.model.DishAllergen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link DishAllergen} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface DishAllergenRepository extends JpaRepository<DishAllergen, Long> {

    /**
     * Finds all allergens for a dish.
     *
     * @param dishId the dish ID
     * @return list of allergens
     */
    List<DishAllergen> findByDishId(Long dishId);

    /**
     * Deletes all allergens for a dish.
     *
     * @param dishId the dish ID
     */
    void deleteByDishId(Long dishId);
}
