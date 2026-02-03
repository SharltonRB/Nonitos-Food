package com.nonitos.food.service;

import com.nonitos.food.dto.profile.*;
import com.nonitos.food.exception.BadRequestException;
import com.nonitos.food.exception.ResourceNotFoundException;
import com.nonitos.food.model.*;
import com.nonitos.food.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientProfileServiceTest {

    @Mock
    private ClientProfileRepository clientProfileRepository;

    @Mock
    private ClientAllergyRepository clientAllergyRepository;

    @Mock
    private ClientRestrictionRepository clientRestrictionRepository;

    @Mock
    private ClientPreferencesRepository clientPreferencesRepository;

    @Mock
    private AllergyRepository allergyRepository;

    @Mock
    private DietaryRestrictionRepository dietaryRestrictionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClientProfileService clientProfileService;

    private User testUser;
    private ClientProfile testProfile;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFullName("Test User");
        testUser.setRole(User.UserRole.CLIENT);
        // Simulate ID from database
        try {
            var idField = User.class.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testUser, 1L);
        } catch (Exception e) {
            // Ignore for test
        }

        testProfile = new ClientProfile();
        testProfile.setUser(testUser);
        testProfile.setPhone("1234567890");
        testProfile.setAddress("123 Test St");
        try {
            var idField = ClientProfile.class.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testProfile, 1L);
        } catch (Exception e) {
            // Ignore for test
        }
    }

    @Test
    void createProfile_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(clientProfileRepository.existsByUserId(1L)).thenReturn(false);
        when(clientProfileRepository.save(any(ClientProfile.class))).thenReturn(testProfile);
        when(clientPreferencesRepository.save(any(ClientPreferences.class))).thenReturn(new ClientPreferences());

        ClientProfile result = clientProfileService.createProfile(1L);

        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        verify(clientProfileRepository).save(any(ClientProfile.class));
        verify(clientPreferencesRepository).save(any(ClientPreferences.class));
    }

    @Test
    void createProfile_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientProfileService.createProfile(1L));
    }

    @Test
    void createProfile_ProfileAlreadyExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(clientProfileRepository.existsByUserId(1L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> clientProfileService.createProfile(1L));
    }

    @Test
    void updateProfile_Success() {
        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .phone("9876543210")
                .address("456 New St")
                .build();

        when(clientProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));
        when(clientProfileRepository.save(any(ClientProfile.class))).thenReturn(testProfile);
        when(clientAllergyRepository.findByClientProfileId(1L)).thenReturn(java.util.Collections.emptyList());
        when(clientRestrictionRepository.findByClientProfileId(1L)).thenReturn(java.util.Collections.emptyList());
        when(clientPreferencesRepository.findByClientProfileId(1L)).thenReturn(Optional.empty());

        ClientProfileResponse result = clientProfileService.updateProfile(1L, request);

        assertNotNull(result);
        verify(clientProfileRepository).save(any(ClientProfile.class));
    }

    @Test
    void addAllergy_Success() {
        Allergy allergy = new Allergy();
        allergy.setName("Dairy");
        try {
            var idField = Allergy.class.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(allergy, 1L);
        } catch (Exception e) {
            // Ignore
        }

        AddAllergyRequest request = AddAllergyRequest.builder()
                .allergyId(1L)
                .severity(ClientAllergy.AllergySeverity.MODERATE)
                .build();

        when(clientProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));
        when(allergyRepository.findById(1L)).thenReturn(Optional.of(allergy));
        when(clientAllergyRepository.findByClientProfileIdAndAllergyId(1L, 1L)).thenReturn(Optional.empty());

        clientProfileService.addAllergy(1L, request);

        verify(clientAllergyRepository).save(any(ClientAllergy.class));
    }

    @Test
    void addAllergy_AlreadyExists() {
        Allergy allergy = new Allergy();
        allergy.setName("Dairy");
        try {
            var idField = Allergy.class.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(allergy, 1L);
        } catch (Exception e) {
            // Ignore
        }

        ClientAllergy existingAllergy = new ClientAllergy();
        AddAllergyRequest request = AddAllergyRequest.builder()
                .allergyId(1L)
                .severity(ClientAllergy.AllergySeverity.MODERATE)
                .build();

        when(clientProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));
        when(allergyRepository.findById(1L)).thenReturn(Optional.of(allergy));
        when(clientAllergyRepository.findByClientProfileIdAndAllergyId(1L, 1L))
                .thenReturn(Optional.of(existingAllergy));

        assertThrows(BadRequestException.class, () -> clientProfileService.addAllergy(1L, request));
    }

    @Test
    void removeAllergy_Success() {
        ClientAllergy clientAllergy = new ClientAllergy();
        try {
            var idField = ClientAllergy.class.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(clientAllergy, 1L);
        } catch (Exception e) {
            // Ignore
        }

        when(clientProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));
        when(clientAllergyRepository.findByClientProfileIdAndAllergyId(1L, 1L))
                .thenReturn(Optional.of(clientAllergy));

        clientProfileService.removeAllergy(1L, 1L);

        verify(clientAllergyRepository).delete(clientAllergy);
    }

    @Test
    void addRestriction_Success() {
        DietaryRestriction restriction = new DietaryRestriction();
        restriction.setName("Vegan");
        try {
            var idField = DietaryRestriction.class.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(restriction, 1L);
        } catch (Exception e) {
            // Ignore
        }

        AddRestrictionRequest request = AddRestrictionRequest.builder()
                .restrictionId(1L)
                .build();

        when(clientProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));
        when(dietaryRestrictionRepository.findById(1L)).thenReturn(Optional.of(restriction));
        when(clientRestrictionRepository.findByClientProfileIdAndRestrictionId(1L, 1L))
                .thenReturn(Optional.empty());

        clientProfileService.addRestriction(1L, request);

        verify(clientRestrictionRepository).save(any(ClientRestriction.class));
    }

    @Test
    void updatePreferences_Success() {
        UpdatePreferencesRequest request = UpdatePreferencesRequest.builder()
                .mealPlanType(ClientPreferences.MealPlanType.CUSTOM)
                .mealsPerDay(2)
                .includeBreakfast(true)
                .includeLunch(true)
                .includeDinner(false)
                .targetCalories(2000)
                .build();

        ClientPreferences preferences = new ClientPreferences();
        preferences.setClientProfile(testProfile);
        try {
            var idField = ClientPreferences.class.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(preferences, 1L);
        } catch (Exception e) {
            // Ignore
        }

        when(clientProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));
        when(clientPreferencesRepository.findByClientProfileId(1L)).thenReturn(Optional.of(preferences));

        clientProfileService.updatePreferences(1L, request);

        verify(clientPreferencesRepository).save(any(ClientPreferences.class));
    }
}
