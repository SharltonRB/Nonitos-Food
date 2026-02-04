package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a notification sent to a user.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "notifications")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {

    /** Reference to the user */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Notification type */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    /** Notification title */
    @Column(nullable = false, length = 200)
    private String title;

    /** Notification message */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    /** Whether the notification has been read */
    @Column(nullable = false)
    private Boolean isRead;

    /** Related entity ID (order, menu, etc.) */
    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    /** Related entity type */
    @Column(name = "related_entity_type", length = 50)
    private String relatedEntityType;

    /** When the notification was read */
    private LocalDateTime readAt;

    public enum NotificationType {
        ORDER_CREATED,
        ORDER_PAID,
        ORDER_IN_PREPARATION,
        ORDER_READY,
        ORDER_COMPLETED,
        ORDER_CANCELLED,
        PAYMENT_RECEIVED,
        PAYMENT_FAILED,
        MENU_PUBLISHED,
        SYSTEM_ANNOUNCEMENT
    }
}
