package com.nonitos.food.controller;

import com.nonitos.food.dto.ApiResponse;
import com.nonitos.food.dto.profile.*;
import com.nonitos.food.model.User;
import com.nonitos.food.service.ClientProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for client profile management.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ClientProfileController {

    private final ClientProfileService clientProfileService;

    /**
     * Gets the authenticated client's profile.
     *
     * @param user the authenticated user
     * @return the profile response
     */
    @GetMapping
    public ResponseEntity<ApiResponse<ClientProfileResponse>> getProfile(
        @AuthenticationPrincipal User user
    ) {
        ClientProfileResponse profile = clientProfileService.getProfile(user.getId());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    /**
     * Updates the authenticated client's profile.
     *
     * @param user the authenticated user
     * @param request the update request
     * @return the updated profile
     */
    @PutMapping
    public ResponseEntity<ApiResponse<ClientProfileResponse>> updateProfile(
        @AuthenticationPrincipal User user,
        @Valid @RequestBody UpdateProfileRequest request
    ) {
        ClientProfileResponse profile = clientProfileService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profile));
    }

    /**
     * Adds an allergy to the authenticated client's profile.
     *
     * @param user the authenticated user
     * @param request the allergy request
     * @return success response
     */
    @PostMapping("/allergies")
    public ResponseEntity<ApiResponse<Void>> addAllergy(
        @AuthenticationPrincipal User user,
        @Valid @RequestBody AddAllergyRequest request
    ) {
        clientProfileService.addAllergy(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Allergy added successfully", null));
    }

    /**
     * Removes an allergy from the authenticated client's profile.
     *
     * @param user the authenticated user
     * @param allergyId the allergy ID
     * @return success response
     */
    @DeleteMapping("/allergies/{allergyId}")
    public ResponseEntity<ApiResponse<Void>> removeAllergy(
        @AuthenticationPrincipal User user,
        @PathVariable Long allergyId
    ) {
        clientProfileService.removeAllergy(user.getId(), allergyId);
        return ResponseEntity.ok(ApiResponse.success("Allergy removed successfully", null));
    }

    /**
     * Adds a dietary restriction to the authenticated client's profile.
     *
     * @param user the authenticated user
     * @param request the restriction request
     * @return success response
     */
    @PostMapping("/restrictions")
    public ResponseEntity<ApiResponse<Void>> addRestriction(
        @AuthenticationPrincipal User user,
        @Valid @RequestBody AddRestrictionRequest request
    ) {
        clientProfileService.addRestriction(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Restriction added successfully", null));
    }

    /**
     * Removes a dietary restriction from the authenticated client's profile.
     *
     * @param user the authenticated user
     * @param restrictionId the restriction ID
     * @return success response
     */
    @DeleteMapping("/restrictions/{restrictionId}")
    public ResponseEntity<ApiResponse<Void>> removeRestriction(
        @AuthenticationPrincipal User user,
        @PathVariable Long restrictionId
    ) {
        clientProfileService.removeRestriction(user.getId(), restrictionId);
        return ResponseEntity.ok(ApiResponse.success("Restriction removed successfully", null));
    }

    /**
     * Updates the authenticated client's preferences.
     *
     * @param user the authenticated user
     * @param request the preferences request
     * @return success response
     */
    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<Void>> updatePreferences(
        @AuthenticationPrincipal User user,
        @Valid @RequestBody UpdatePreferencesRequest request
    ) {
        clientProfileService.updatePreferences(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Preferences updated successfully", null));
    }
}
