package com.nonitos.food.service;

import com.nonitos.food.dto.profile.*;
import com.nonitos.food.exception.BadRequestException;
import com.nonitos.food.exception.ResourceNotFoundException;
import com.nonitos.food.model.*;
import com.nonitos.food.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Service for managing client profiles, allergies, restrictions, and preferences.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;
    private final ClientAllergyRepository clientAllergyRepository;
    private final ClientRestrictionRepository clientRestrictionRepository;
    private final ClientPreferencesRepository clientPreferencesRepository;
    private final AllergyRepository allergyRepository;
    private final DietaryRestrictionRepository dietaryRestrictionRepository;
    private final UserRepository userRepository;

    /**
     * Creates a client profile for a user.
     *
     * @param userId the user ID
     * @return the created profile
     */
    @Transactional
    public ClientProfile createProfile(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (clientProfileRepository.existsByUserId(userId)) {
            throw new BadRequestException("Profile already exists for this user");
        }

        ClientProfile profile = ClientProfile.builder()
            .user(user)
            .build();

        ClientProfile savedProfile = clientProfileRepository.save(profile);

        // Create default preferences
        ClientPreferences preferences = ClientPreferences.builder()
            .clientProfile(savedProfile)
            .mealPlanType(ClientPreferences.MealPlanType.STANDARD)
            .mealsPerDay(3)
            .includeBreakfast(true)
            .includeLunch(true)
            .includeDinner(true)
            .build();

        clientPreferencesRepository.save(preferences);

        log.info("Created profile for user ID: {}", userId);
        return savedProfile;
    }

    /**
     * Gets a client profile by user ID.
     *
     * @param userId the user ID
     * @return the profile response
     */
    @Transactional(readOnly = true)
    public ClientProfileResponse getProfile(Long userId) {
        ClientProfile profile = clientProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        return buildProfileResponse(profile);
    }

    /**
     * Updates a client profile.
     *
     * @param userId the user ID
     * @param request the update request
     * @return the updated profile
     */
    @Transactional
    public ClientProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        ClientProfile profile = clientProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        if (request.getPhone() != null) profile.setPhone(request.getPhone());
        if (request.getAddress() != null) profile.setAddress(request.getAddress());
        if (request.getEmergencyContactName() != null) profile.setEmergencyContactName(request.getEmergencyContactName());
        if (request.getEmergencyContactPhone() != null) profile.setEmergencyContactPhone(request.getEmergencyContactPhone());
        if (request.getNotes() != null) profile.setNotes(request.getNotes());

        clientProfileRepository.save(profile);
        log.info("Updated profile for user ID: {}", userId);

        return buildProfileResponse(profile);
    }

    /**
     * Adds an allergy to a client profile.
     *
     * @param userId the user ID
     * @param request the allergy request
     */
    @Transactional
    public void addAllergy(Long userId, AddAllergyRequest request) {
        ClientProfile profile = clientProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Allergy allergy = allergyRepository.findById(request.getAllergyId())
            .orElseThrow(() -> new ResourceNotFoundException("Allergy not found"));

        if (clientAllergyRepository.findByClientProfileIdAndAllergyId(profile.getId(), allergy.getId()).isPresent()) {
            throw new BadRequestException("Allergy already added to profile");
        }

        ClientAllergy clientAllergy = ClientAllergy.builder()
            .clientProfile(profile)
            .allergy(allergy)
            .severity(request.getSeverity())
            .notes(request.getNotes())
            .build();

        clientAllergyRepository.save(clientAllergy);
        log.info("Added allergy {} to profile for user ID: {}", allergy.getName(), userId);
    }

    /**
     * Removes an allergy from a client profile.
     *
     * @param userId the user ID
     * @param allergyId the allergy ID
     */
    @Transactional
    public void removeAllergy(Long userId, Long allergyId) {
        ClientProfile profile = clientProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        ClientAllergy clientAllergy = clientAllergyRepository
            .findByClientProfileIdAndAllergyId(profile.getId(), allergyId)
            .orElseThrow(() -> new ResourceNotFoundException("Allergy not found in profile"));

        clientAllergyRepository.delete(clientAllergy);
        log.info("Removed allergy ID {} from profile for user ID: {}", allergyId, userId);
    }

    /**
     * Adds a dietary restriction to a client profile.
     *
     * @param userId the user ID
     * @param request the restriction request
     */
    @Transactional
    public void addRestriction(Long userId, AddRestrictionRequest request) {
        ClientProfile profile = clientProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        DietaryRestriction restriction = dietaryRestrictionRepository.findById(request.getRestrictionId())
            .orElseThrow(() -> new ResourceNotFoundException("Dietary restriction not found"));

        if (clientRestrictionRepository.findByClientProfileIdAndRestrictionId(profile.getId(), restriction.getId()).isPresent()) {
            throw new BadRequestException("Restriction already added to profile");
        }

        ClientRestriction clientRestriction = ClientRestriction.builder()
            .clientProfile(profile)
            .restriction(restriction)
            .notes(request.getNotes())
            .build();

        clientRestrictionRepository.save(clientRestriction);
        log.info("Added restriction {} to profile for user ID: {}", restriction.getName(), userId);
    }

    /**
     * Removes a dietary restriction from a client profile.
     *
     * @param userId the user ID
     * @param restrictionId the restriction ID
     */
    @Transactional
    public void removeRestriction(Long userId, Long restrictionId) {
        ClientProfile profile = clientProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        ClientRestriction clientRestriction = clientRestrictionRepository
            .findByClientProfileIdAndRestrictionId(profile.getId(), restrictionId)
            .orElseThrow(() -> new ResourceNotFoundException("Restriction not found in profile"));

        clientRestrictionRepository.delete(clientRestriction);
        log.info("Removed restriction ID {} from profile for user ID: {}", restrictionId, userId);
    }

    /**
     * Updates client preferences.
     *
     * @param userId the user ID
     * @param request the preferences request
     */
    @Transactional
    public void updatePreferences(Long userId, UpdatePreferencesRequest request) {
        ClientProfile profile = clientProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        ClientPreferences preferences = clientPreferencesRepository.findByClientProfileId(profile.getId())
            .orElseGet(() -> ClientPreferences.builder().clientProfile(profile).build());

        preferences.setMealPlanType(request.getMealPlanType());
        preferences.setMealsPerDay(request.getMealsPerDay());
        preferences.setIncludeBreakfast(request.getIncludeBreakfast());
        preferences.setIncludeLunch(request.getIncludeLunch());
        preferences.setIncludeDinner(request.getIncludeDinner());
        preferences.setTargetCalories(request.getTargetCalories());
        preferences.setTargetProtein(request.getTargetProtein());
        preferences.setTargetCarbs(request.getTargetCarbs());
        preferences.setTargetFats(request.getTargetFats());

        clientPreferencesRepository.save(preferences);
        log.info("Updated preferences for user ID: {}", userId);
    }

    private ClientProfileResponse buildProfileResponse(ClientProfile profile) {
        var allergies = clientAllergyRepository.findByClientProfileId(profile.getId()).stream()
            .map(ca -> ClientProfileResponse.AllergyInfo.builder()
                .id(ca.getId())
                .allergyId(ca.getAllergy().getId())
                .allergyName(ca.getAllergy().getName())
                .severity(ca.getSeverity())
                .notes(ca.getNotes())
                .build())
            .collect(Collectors.toList());

        var restrictions = clientRestrictionRepository.findByClientProfileId(profile.getId()).stream()
            .map(cr -> ClientProfileResponse.RestrictionInfo.builder()
                .id(cr.getId())
                .restrictionId(cr.getRestriction().getId())
                .restrictionName(cr.getRestriction().getName())
                .notes(cr.getNotes())
                .build())
            .collect(Collectors.toList());

        var preferencesEntity = clientPreferencesRepository.findByClientProfileId(profile.getId()).orElse(null);
        ClientProfileResponse.PreferencesInfo preferences = null;
        if (preferencesEntity != null) {
            preferences = ClientProfileResponse.PreferencesInfo.builder()
                .id(preferencesEntity.getId())
                .mealPlanType(preferencesEntity.getMealPlanType().name())
                .mealsPerDay(preferencesEntity.getMealsPerDay())
                .includeBreakfast(preferencesEntity.getIncludeBreakfast())
                .includeLunch(preferencesEntity.getIncludeLunch())
                .includeDinner(preferencesEntity.getIncludeDinner())
                .targetCalories(preferencesEntity.getTargetCalories())
                .targetProtein(preferencesEntity.getTargetProtein())
                .targetCarbs(preferencesEntity.getTargetCarbs())
                .targetFats(preferencesEntity.getTargetFats())
                .build();
        }

        return ClientProfileResponse.builder()
            .id(profile.getId())
            .userId(profile.getUser().getId())
            .phone(profile.getPhone())
            .address(profile.getAddress())
            .emergencyContactName(profile.getEmergencyContactName())
            .emergencyContactPhone(profile.getEmergencyContactPhone())
            .notes(profile.getNotes())
            .allergies(allergies)
            .restrictions(restrictions)
            .preferences(preferences)
            .build();
    }
}
