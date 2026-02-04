package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a notification template.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "notification_templates")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplate extends BaseEntity {

    /** Template code (unique identifier) */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /** Notification type */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Notification.NotificationType type;

    /** Template title with variables */
    @Column(nullable = false, length = 200)
    private String titleTemplate;

    /** Template message with variables */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String messageTemplate;

    /** Whether the template is active */
    @Column(nullable = false)
    private Boolean isActive;
}
