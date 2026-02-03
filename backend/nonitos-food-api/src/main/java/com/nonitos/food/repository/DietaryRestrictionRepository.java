package com.nonitos.food.repository;

import com.nonitos.food.model.DietaryRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link DietaryRestriction} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface DietaryRestrictionRepository extends JpaRepository<DietaryRestriction, Long> {

    /**
     * Finds a dietary restriction by name.
     *
     * @param name the restriction name
     * @return optional containing the restriction if found
     */
    Optional<DietaryRestriction> findByName(String name);
}
