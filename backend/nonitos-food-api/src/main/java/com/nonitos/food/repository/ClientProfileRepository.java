package com.nonitos.food.repository;

import com.nonitos.food.model.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link ClientProfile} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {

    /**
     * Finds a client profile by user ID.
     *
     * @param userId the user ID
     * @return optional containing the profile if found
     */
    Optional<ClientProfile> findByUserId(Long userId);

    /**
     * Checks if a profile exists for a user.
     *
     * @param userId the user ID
     * @return true if exists, false otherwise
     */
    boolean existsByUserId(Long userId);
}
