package com.nonitos.food.service;

import com.nonitos.food.dto.dish.CreateDishRequest;
import com.nonitos.food.dto.dish.DishResponse;
import com.nonitos.food.dto.dish.UpdateDishRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private DishImageRepository dishImageRepository;

    @Mock
    private DishTagRepository dishTagRepository;

    @Mock
    private DishTagAssignmentRepository dishTagAssignmentRepository;

    @Mock
    private DishAllergenRepository dishAllergenRepository;

    @Mock
    private AllergyRepository allergyRepository;

    @InjectMocks
    private DishService dishService;

    private Dish testDish;

    @BeforeEach
    void setUp() {
        testDish = new Dish();
        testDish.setName("Grilled Chicken");
        testDish.setDescription("Healthy grilled chicken breast");
        testDish.setCategory(Dish.DishCategory.LUNCH);
        testDish.setPrice(new BigDecimal("12.99"));
        testDish.setCalories(350);
        testDish.setProtein(45);
        testDish.setCarbs(10);
        testDish.setFats(15);
        testDish.setIsActive(true);

        try {
            var idField = Dish.class.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testDish, 1L);
        } catch (Exception e) {
            // Ignore
        }
    }

    @Test
    void createDish_Success() {
        CreateDishRequest request = CreateDishRequest.builder()
                .name("Grilled Chicken")
                .description("Healthy grilled chicken breast")
                .category(Dish.DishCategory.LUNCH)
                .price(new BigDecimal("12.99"))
                .calories(350)
                .protein(45)
                .carbs(10)
                .fats(15)
                .build();

        when(dishRepository.existsByName(request.getName())).thenReturn(false);
        when(dishRepository.save(any(Dish.class))).thenReturn(testDish);
        when(dishImageRepository.findByDishIdOrderByDisplayOrderAsc(1L)).thenReturn(Collections.emptyList());
        when(dishTagAssignmentRepository.findByDishId(1L)).thenReturn(Collections.emptyList());
        when(dishAllergenRepository.findByDishId(1L)).thenReturn(Collections.emptyList());

        DishResponse response = dishService.createDish(request);

        assertNotNull(response);
        assertEquals("Grilled Chicken", response.getName());
        assertEquals(Dish.DishCategory.LUNCH, response.getCategory());
        verify(dishRepository).save(any(Dish.class));
    }

    @Test
    void createDish_DuplicateName() {
        CreateDishRequest request = CreateDishRequest.builder()
                .name("Grilled Chicken")
                .category(Dish.DishCategory.LUNCH)
                .price(new BigDecimal("12.99"))
                .calories(350)
                .protein(45)
                .carbs(10)
                .fats(15)
                .build();

        when(dishRepository.existsByName(request.getName())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> dishService.createDish(request));
    }

    @Test
    void getDishById_Success() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(testDish));
        when(dishImageRepository.findByDishIdOrderByDisplayOrderAsc(1L)).thenReturn(Collections.emptyList());
        when(dishTagAssignmentRepository.findByDishId(1L)).thenReturn(Collections.emptyList());
        when(dishAllergenRepository.findByDishId(1L)).thenReturn(Collections.emptyList());

        DishResponse response = dishService.getDishById(1L);

        assertNotNull(response);
        assertEquals("Grilled Chicken", response.getName());
    }

    @Test
    void getDishById_NotFound() {
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dishService.getDishById(1L));
    }

    @Test
    void getAllDishes_Success() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Dish> dishPage = new PageImpl<>(List.of(testDish));

        when(dishRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(dishPage);
        when(dishImageRepository.findByDishIdOrderByDisplayOrderAsc(1L)).thenReturn(Collections.emptyList());
        when(dishTagAssignmentRepository.findByDishId(1L)).thenReturn(Collections.emptyList());
        when(dishAllergenRepository.findByDishId(1L)).thenReturn(Collections.emptyList());

        Page<DishResponse> result = dishService.getAllDishes(null, null, null, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateDish_Success() {
        UpdateDishRequest request = UpdateDishRequest.builder()
                .name("Updated Chicken")
                .price(new BigDecimal("14.99"))
                .build();

        when(dishRepository.findById(1L)).thenReturn(Optional.of(testDish));
        when(dishRepository.existsByName("Updated Chicken")).thenReturn(false);
        when(dishRepository.save(any(Dish.class))).thenReturn(testDish);
        when(dishImageRepository.findByDishIdOrderByDisplayOrderAsc(1L)).thenReturn(Collections.emptyList());
        when(dishTagAssignmentRepository.findByDishId(1L)).thenReturn(Collections.emptyList());
        when(dishAllergenRepository.findByDishId(1L)).thenReturn(Collections.emptyList());

        DishResponse response = dishService.updateDish(1L, request);

        assertNotNull(response);
        verify(dishRepository).save(any(Dish.class));
    }

    @Test
    void updateDish_NotFound() {
        UpdateDishRequest request = UpdateDishRequest.builder()
                .name("Updated Chicken")
                .build();

        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dishService.updateDish(1L, request));
    }

    @Test
    void deleteDish_Success() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(testDish));

        dishService.deleteDish(1L);

        verify(dishRepository).delete(testDish);
    }

    @Test
    void deleteDish_NotFound() {
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dishService.deleteDish(1L));
    }
}
