package com.nonitos.food.controller;

import com.nonitos.food.dto.ApiResponse;
import com.nonitos.food.model.Allergy;
import com.nonitos.food.model.DietaryRestriction;
import com.nonitos.food.repository.AllergyRepository;
import com.nonitos.food.repository.DietaryRestrictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for catalog data (allergies and dietary restrictions).
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/catalogs")
@RequiredArgsConstructor
public class CatalogController {

    private final AllergyRepository allergyRepository;
    private final DietaryRestrictionRepository dietaryRestrictionRepository;

    /**
     * Gets all available allergies.
     *
     * @return list of allergies
     */
    @GetMapping("/allergies")
    public ResponseEntity<ApiResponse<List<Allergy>>> getAllergies() {
        List<Allergy> allergies = allergyRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(allergies));
    }

    /**
     * Gets all available dietary restrictions.
     *
     * @return list of dietary restrictions
     */
    @GetMapping("/restrictions")
    public ResponseEntity<ApiResponse<List<DietaryRestriction>>> getRestrictions() {
        List<DietaryRestriction> restrictions = dietaryRestrictionRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(restrictions));
    }
}
