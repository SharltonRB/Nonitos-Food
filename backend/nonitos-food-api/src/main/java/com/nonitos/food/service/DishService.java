package com.nonitos.food.service;

import com.nonitos.food.dto.dish.CreateDishRequest;
import com.nonitos.food.dto.dish.DishResponse;
import com.nonitos.food.dto.dish.UpdateDishRequest;
import com.nonitos.food.exception.BadRequestException;
import com.nonitos.food.exception.ResourceNotFoundException;
import com.nonitos.food.model.*;
import com.nonitos.food.repository.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing dishes.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DishService {

    private final DishRepository dishRepository;
    private final DishImageRepository dishImageRepository;
    private final DishTagRepository dishTagRepository;
    private final DishTagAssignmentRepository dishTagAssignmentRepository;
    private final DishAllergenRepository dishAllergenRepository;
    private final AllergyRepository allergyRepository;

    /**
     * Creates a new dish.
     *
     * @param request the dish creation request
     * @return the created dish
     */
    @Transactional
    public DishResponse createDish(CreateDishRequest request) {
        if (dishRepository.existsByName(request.getName())) {
            throw new BadRequestException("Dish with name already exists");
        }

        Dish dish = Dish.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .calories(request.getCalories())
                .protein(request.getProtein())
                .carbs(request.getCarbs())
                .fats(request.getFats())
                .isActive(true)
                .build();

        dish = dishRepository.save(dish);

        // Save images
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            saveImages(dish, request.getImageUrls());
        }

        // Save tags
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            saveTags(dish, request.getTagIds());
        }

        // Save allergens
        if (request.getAllergenIds() != null && !request.getAllergenIds().isEmpty()) {
            saveAllergens(dish, request.getAllergenIds());
        }

        log.info("Created dish: {}", dish.getName());
        return buildDishResponse(dish);
    }

    /**
     * Gets a dish by ID.
     *
     * @param id the dish ID
     * @return the dish
     */
    @Transactional(readOnly = true)
    public DishResponse getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        return buildDishResponse(dish);
    }

    /**
     * Gets all dishes with filters.
     *
     * @param category optional category filter
     * @param isActive optional active status filter
     * @param minPrice optional minimum price filter
     * @param maxPrice optional maximum price filter
     * @param tagName optional tag name filter
     * @param pageable pagination parameters
     * @return page of dishes
     */
    @Transactional(readOnly = true)
    public Page<DishResponse> getAllDishes(
            Dish.DishCategory category,
            Boolean isActive,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String tagName,
            Pageable pageable
    ) {
        Specification<Dish> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null) {
                predicates.add(cb.equal(root.get("category"), category));
            }

            if (isActive != null) {
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return dishRepository.findAll(spec, pageable).map(this::buildDishResponse);
    }

    /**
     * Updates a dish.
     *
     * @param id the dish ID
     * @param request the update request
     * @return the updated dish
     */
    @Transactional
    public DishResponse updateDish(Long id, UpdateDishRequest request) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));

        if (request.getName() != null) {
            if (!request.getName().equals(dish.getName()) && dishRepository.existsByName(request.getName())) {
                throw new BadRequestException("Dish with name already exists");
            }
            dish.setName(request.getName());
        }

        if (request.getDescription() != null) dish.setDescription(request.getDescription());
        if (request.getCategory() != null) dish.setCategory(request.getCategory());
        if (request.getPrice() != null) dish.setPrice(request.getPrice());
        if (request.getCalories() != null) dish.setCalories(request.getCalories());
        if (request.getProtein() != null) dish.setProtein(request.getProtein());
        if (request.getCarbs() != null) dish.setCarbs(request.getCarbs());
        if (request.getFats() != null) dish.setFats(request.getFats());
        if (request.getIsActive() != null) dish.setIsActive(request.getIsActive());

        dishRepository.save(dish);

        // Update images if provided
        if (request.getImageUrls() != null) {
            dishImageRepository.deleteByDishId(dish.getId());
            if (!request.getImageUrls().isEmpty()) {
                saveImages(dish, request.getImageUrls());
            }
        }

        // Update tags if provided
        if (request.getTagIds() != null) {
            dishTagAssignmentRepository.deleteByDishId(dish.getId());
            if (!request.getTagIds().isEmpty()) {
                saveTags(dish, request.getTagIds());
            }
        }

        // Update allergens if provided
        if (request.getAllergenIds() != null) {
            dishAllergenRepository.deleteByDishId(dish.getId());
            if (!request.getAllergenIds().isEmpty()) {
                saveAllergens(dish, request.getAllergenIds());
            }
        }

        log.info("Updated dish: {}", dish.getName());
        return buildDishResponse(dish);
    }

    /**
     * Deletes a dish.
     *
     * @param id the dish ID
     */
    @Transactional
    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));

        dishRepository.delete(dish);
        log.info("Deleted dish: {}", dish.getName());
    }

    private void saveImages(Dish dish, List<String> imageUrls) {
        for (int i = 0; i < imageUrls.size(); i++) {
            DishImage image = DishImage.builder()
                    .dish(dish)
                    .imageUrl(imageUrls.get(i))
                    .isPrimary(i == 0)
                    .displayOrder(i)
                    .build();
            dishImageRepository.save(image);
        }
    }

    private void saveTags(Dish dish, List<Long> tagIds) {
        for (Long tagId : tagIds) {
            DishTag tag = dishTagRepository.findById(tagId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + tagId));
            DishTagAssignment assignment = DishTagAssignment.builder()
                    .dish(dish)
                    .tag(tag)
                    .build();
            dishTagAssignmentRepository.save(assignment);
        }
    }

    private void saveAllergens(Dish dish, List<Long> allergenIds) {
        for (Long allergenId : allergenIds) {
            Allergy allergy = allergyRepository.findById(allergenId)
                    .orElseThrow(() -> new ResourceNotFoundException("Allergy not found: " + allergenId));
            DishAllergen allergen = DishAllergen.builder()
                    .dish(dish)
                    .allergy(allergy)
                    .build();
            dishAllergenRepository.save(allergen);
        }
    }

    private DishResponse buildDishResponse(Dish dish) {
        List<String> images = dishImageRepository.findByDishIdOrderByDisplayOrderAsc(dish.getId())
                .stream()
                .map(DishImage::getImageUrl)
                .collect(Collectors.toList());

        List<String> tags = dishTagAssignmentRepository.findByDishId(dish.getId())
                .stream()
                .map(assignment -> assignment.getTag().getName())
                .collect(Collectors.toList());

        List<String> allergens = dishAllergenRepository.findByDishId(dish.getId())
                .stream()
                .map(allergen -> allergen.getAllergy().getName())
                .collect(Collectors.toList());

        return DishResponse.builder()
                .id(dish.getId())
                .name(dish.getName())
                .description(dish.getDescription())
                .category(dish.getCategory())
                .price(dish.getPrice())
                .calories(dish.getCalories())
                .protein(dish.getProtein())
                .carbs(dish.getCarbs())
                .fats(dish.getFats())
                .isActive(dish.getIsActive())
                .images(images)
                .tags(tags)
                .allergens(allergens)
                .build();
    }
}
