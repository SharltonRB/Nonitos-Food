package com.nonitos.food.dto.profile;

import com.nonitos.food.model.ClientAllergy;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for adding an allergy to client profile.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddAllergyRequest {

    @NotNull(message = "Allergy ID is required")
    private Long allergyId;

    @NotNull(message = "Severity is required")
    private ClientAllergy.AllergySeverity severity;

    @Size(max = 255, message = "Notes must not exceed 255 characters")
    private String notes;
}
