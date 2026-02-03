package com.nonitos.food.repository;

import com.nonitos.food.model.ClientPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link ClientPreferences} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface ClientPreferencesRepository extends JpaRepository<ClientPreferences, Long> {

    /**
     * Finds preferences by client profile ID.
     *
     * @param clientProfileId the client profile ID
     * @return optional containing the preferences if found
     */
    Optional<ClientPreferences> findByClientProfileId(Long clientProfileId);

    /**
     * Deletes preferences for a client profile.
     *
     * @param clientProfileId the client profile ID
     */
    void deleteByClientProfileId(Long clientProfileId);
}
