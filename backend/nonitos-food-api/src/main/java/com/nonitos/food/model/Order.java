package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a customer order.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    /** Unique order code for pickup */
    @Column(nullable = false, unique = true, length = 10)
    private String orderCode;

    /** Reference to the client */
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    /** Reference to the weekly menu */
    @ManyToOne
    @JoinColumn(name = "weekly_menu_id", nullable = false)
    private WeeklyMenu weeklyMenu;

    /** Order status */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    /** Total amount */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /** Number of meals per day */
    @Column(nullable = false)
    private Integer mealsPerDay;

    /** Include breakfast */
    @Column(nullable = false)
    private Boolean includeBreakfast;

    /** Include lunch */
    @Column(nullable = false)
    private Boolean includeLunch;

    /** Include dinner */
    @Column(nullable = false)
    private Boolean includeDinner;

    /** Pickup date and time */
    @Column(nullable = false)
    private LocalDateTime pickupDateTime;

    /** QR code for pickup verification */
    @Column(length = 500)
    private String qrCode;

    /** Special instructions */
    @Column(columnDefinition = "TEXT")
    private String specialInstructions;

    /** Cancellation reason */
    @Column(length = 500)
    private String cancellationReason;

    /** Cancelled at */
    private LocalDateTime cancelledAt;

    public enum OrderStatus {
        PENDING_PAYMENT,
        PAID,
        IN_PREPARATION,
        READY_FOR_PICKUP,
        COMPLETED,
        CANCELLED
    }
}
