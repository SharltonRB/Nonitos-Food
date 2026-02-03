package com.nonitos.food.repository;

import com.nonitos.food.model.ClientAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link ClientAllergy} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface ClientAllergyRepository extends JpaRepository<ClientAllergy, Long> {

    /**
     * Finds all allergies for a client profile.
     *
     * @param clientProfileId the client profile ID
     * @return list of client allergies
     */
    List<ClientAllergy> findByClientProfileId(Long clientProfileId);

    /**
     * Finds a specific client allergy.
     *
     * @param clientProfileId the client profile ID
     * @param allergyId the allergy ID
     * @return optional containing the client allergy if found
     */
    Optional<ClientAllergy> findByClientProfileIdAndAllergyId(Long clientProfileId, Long allergyId);

    /**
     * Deletes all allergies for a client profile.
     *
     * @param clientProfileId the client profile ID
     */
    void deleteByClientProfileId(Long clientProfileId);
}
