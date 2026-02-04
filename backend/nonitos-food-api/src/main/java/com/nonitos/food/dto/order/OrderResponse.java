package com.nonitos.food.dto.order;

import com.nonitos.food.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for order information.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderCode;
    private Long clientId;
    private String clientName;
    private Long weeklyMenuId;
    private Order.OrderStatus status;
    private BigDecimal totalAmount;
    private Integer mealsPerDay;
    private Boolean includeBreakfast;
    private Boolean includeLunch;
    private Boolean includeDinner;
    private LocalDateTime pickupDateTime;
    private String qrCode;
    private String specialInstructions;
    private String cancellationReason;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private List<StatusHistoryInfo> statusHistory;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusHistoryInfo {
        private Order.OrderStatus previousStatus;
        private Order.OrderStatus newStatus;
        private String changedByName;
        private String notes;
        private LocalDateTime changedAt;
    }
}
