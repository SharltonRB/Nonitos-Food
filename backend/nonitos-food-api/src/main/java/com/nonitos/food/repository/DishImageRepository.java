package com.nonitos.food.repository;

import com.nonitos.food.model.DishImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link DishImage} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface DishImageRepository extends JpaRepository<DishImage, Long> {

    /**
     * Finds all images for a dish.
     *
     * @param dishId the dish ID
     * @return list of images
     */
    List<DishImage> findByDishIdOrderByDisplayOrderAsc(Long dishId);

    /**
     * Deletes all images for a dish.
     *
     * @param dishId the dish ID
     */
    void deleteByDishId(Long dishId);
}
