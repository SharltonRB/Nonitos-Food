package com.nonitos.food.dto.profile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for adding a dietary restriction to client profile.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddRestrictionRequest {

    @NotNull(message = "Restriction ID is required")
    private Long restrictionId;

    @Size(max = 255, message = "Notes must not exceed 255 characters")
    private String notes;
}
