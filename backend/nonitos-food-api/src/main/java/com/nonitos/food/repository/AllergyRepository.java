package com.nonitos.food.repository;

import com.nonitos.food.model.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Allergy} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {

    /**
     * Finds an allergy by name.
     *
     * @param name the allergy name
     * @return optional containing the allergy if found
     */
    Optional<Allergy> findByName(String name);
}
