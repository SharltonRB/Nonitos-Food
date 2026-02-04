package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing order status history for tracking.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "order_status_history")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusHistory extends BaseEntity {

    /** Reference to the order */
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /** Previous status */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Order.OrderStatus previousStatus;

    /** New status */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Order.OrderStatus newStatus;

    /** Changed by user */
    @ManyToOne
    @JoinColumn(name = "changed_by_user_id")
    private User changedBy;

    /** Change reason/notes */
    @Column(length = 500)
    private String notes;

    /** Timestamp of change */
    @Column(nullable = false)
    private LocalDateTime changedAt;
}
