package com.nonitos.food.repository;

import com.nonitos.food.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link User} entity operations.
 * Provides data access methods for user management.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by email address.
     *
     * @param email the user email
     * @return optional containing user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given email exists.
     *
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by email verification token.
     *
     * @param token the verification token
     * @return optional containing user if found, empty otherwise
     */
    Optional<User> findByEmailVerificationToken(String token);
}
