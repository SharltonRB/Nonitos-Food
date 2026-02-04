package com.nonitos.food.dto.order;

import com.nonitos.food.model.Order;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating order status.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;

    private String notes;
}
