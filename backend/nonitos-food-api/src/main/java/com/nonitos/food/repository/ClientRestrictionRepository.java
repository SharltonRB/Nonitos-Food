package com.nonitos.food.repository;

import com.nonitos.food.model.ClientRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link ClientRestriction} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface ClientRestrictionRepository extends JpaRepository<ClientRestriction, Long> {

    /**
     * Finds all dietary restrictions for a client profile.
     *
     * @param clientProfileId the client profile ID
     * @return list of client restrictions
     */
    List<ClientRestriction> findByClientProfileId(Long clientProfileId);

    /**
     * Finds a specific client restriction.
     *
     * @param clientProfileId the client profile ID
     * @param restrictionId the restriction ID
     * @return optional containing the client restriction if found
     */
    Optional<ClientRestriction> findByClientProfileIdAndRestrictionId(Long clientProfileId, Long restrictionId);

    /**
     * Deletes all restrictions for a client profile.
     *
     * @param clientProfileId the client profile ID
     */
    void deleteByClientProfileId(Long clientProfileId);
}
