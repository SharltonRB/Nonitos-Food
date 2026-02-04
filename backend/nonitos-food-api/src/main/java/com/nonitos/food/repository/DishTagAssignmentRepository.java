package com.nonitos.food.repository;

import com.nonitos.food.model.DishTagAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link DishTagAssignment} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface DishTagAssignmentRepository extends JpaRepository<DishTagAssignment, Long> {

    /**
     * Finds all tag assignments for a dish.
     *
     * @param dishId the dish ID
     * @return list of tag assignments
     */
    List<DishTagAssignment> findByDishId(Long dishId);

    /**
     * Deletes all tag assignments for a dish.
     *
     * @param dishId the dish ID
     */
    void deleteByDishId(Long dishId);
}
