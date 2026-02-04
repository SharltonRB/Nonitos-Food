package com.nonitos.food.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response containing dashboard metrics.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMetricsResponse {

    private Long totalUsers;
    private Long totalClients;
    private Long totalOrders;
    private Long pendingOrders;
    private Long completedOrders;
    private BigDecimal totalRevenue;
    private BigDecimal pendingRevenue;
    private Long totalDishes;
    private Long activeDishes;
    private Long totalMenus;
    private Long publishedMenus;
}
