package com.nonitos.food.repository;

import com.nonitos.food.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Dish} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface DishRepository extends JpaRepository<Dish, Long>, JpaSpecificationExecutor<Dish> {

    /**
     * Checks if a dish with the given name exists.
     *
     * @param name the dish name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Finds a dish by name.
     *
     * @param name the dish name
     * @return optional containing the dish if found
     */
    Optional<Dish> findByName(String name);

    /**
     * Counts dishes by active status.
     *
     * @param isActive the active status
     * @return count of dishes
     */
    long countByIsActive(Boolean isActive);
}
