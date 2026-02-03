package com.nonitos.food.dto.profile;

import com.nonitos.food.model.ClientAllergy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for client profile information.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientProfileResponse {
    private Long id;
    private Long userId;
    private String phone;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String notes;
    private List<AllergyInfo> allergies;
    private List<RestrictionInfo> restrictions;
    private PreferencesInfo preferences;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllergyInfo {
        private Long id;
        private Long allergyId;
        private String allergyName;
        private ClientAllergy.AllergySeverity severity;
        private String notes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestrictionInfo {
        private Long id;
        private Long restrictionId;
        private String restrictionName;
        private String notes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreferencesInfo {
        private Long id;
        private String mealPlanType;
        private Integer mealsPerDay;
        private Boolean includeBreakfast;
        private Boolean includeLunch;
        private Boolean includeDinner;
        private Integer targetCalories;
        private Integer targetProtein;
        private Integer targetCarbs;
        private Integer targetFats;
    }
}
