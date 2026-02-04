package com.nonitos.food.repository;

import com.nonitos.food.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link Notification} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Finds all notifications for a user.
     *
     * @param userId the user ID
     * @return list of notifications
     */
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Finds unread notifications for a user.
     *
     * @param userId the user ID
     * @param isRead read status
     * @return list of unread notifications
     */
    List<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(Long userId, Boolean isRead);

    /**
     * Counts unread notifications for a user.
     *
     * @param userId the user ID
     * @param isRead read status
     * @return count of unread notifications
     */
    long countByUserIdAndIsRead(Long userId, Boolean isRead);
}
