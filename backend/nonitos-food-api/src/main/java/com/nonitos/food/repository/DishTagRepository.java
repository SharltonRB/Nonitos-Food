package com.nonitos.food.repository;

import com.nonitos.food.model.DishTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link DishTag} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface DishTagRepository extends JpaRepository<DishTag, Long> {

    /**
     * Finds a tag by name.
     *
     * @param name the tag name
     * @return optional containing the tag if found
     */
    Optional<DishTag> findByName(String name);
}
