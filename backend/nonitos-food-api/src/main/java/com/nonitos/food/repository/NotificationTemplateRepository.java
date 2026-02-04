package com.nonitos.food.repository;

import com.nonitos.food.model.Notification;
import com.nonitos.food.model.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link NotificationTemplate} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    /**
     * Finds a template by type.
     *
     * @param type the notification type
     * @return optional containing the template if found
     */
    Optional<NotificationTemplate> findByType(Notification.NotificationType type);

    /**
     * Finds a template by code.
     *
     * @param code the template code
     * @return optional containing the template if found
     */
    Optional<NotificationTemplate> findByCode(String code);
}
