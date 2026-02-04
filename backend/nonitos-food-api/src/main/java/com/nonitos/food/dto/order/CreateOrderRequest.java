package com.nonitos.food.dto.order;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Request DTO for creating an order.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotNull(message = "Weekly menu ID is required")
    private Long weeklyMenuId;

    @NotNull(message = "Meals per day is required")
    @Min(value = 1, message = "Meals per day must be at least 1")
    @Max(value = 3, message = "Meals per day must not exceed 3")
    private Integer mealsPerDay;

    @NotNull(message = "Include breakfast flag is required")
    private Boolean includeBreakfast;

    @NotNull(message = "Include lunch flag is required")
    private Boolean includeLunch;

    @NotNull(message = "Include dinner flag is required")
    private Boolean includeDinner;

    @NotNull(message = "Pickup date and time is required")
    private LocalDateTime pickupDateTime;

    private String specialInstructions;
}
