package com.nonitos.food.service;

import com.nonitos.food.dto.menu.CreateWeeklyMenuRequest;
import com.nonitos.food.dto.menu.WeeklyMenuResponse;
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

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeeklyMenuServiceTest {

    @Mock
    private WeeklyMenuRepository weeklyMenuRepository;

    @Mock
    private MenuDayRepository menuDayRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private DishImageRepository dishImageRepository;

    @InjectMocks
    private WeeklyMenuService weeklyMenuService;

    private WeeklyMenu testMenu;
    private Dish testDish;
    private LocalDate monday;

    @BeforeEach
    void setUp() {
        monday = LocalDate.of(2026, 2, 9); // A Monday

        testDish = new Dish();
        testDish.setName("Test Dish");
        testDish.setCategory(Dish.DishCategory.LUNCH);
        testDish.setPrice(new BigDecimal("10.00"));
        testDish.setCalories(500);
        testDish.setProtein(30);
        testDish.setCarbs(50);
        testDish.setFats(20);
        testDish.setIsActive(true);

        try {
            var idField = Dish.class.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testDish, 1L);
        } catch (Exception e) {
            // Ignore
        }

        testMenu = new WeeklyMenu();
        testMenu.setWeekStartDate(monday);
        testMenu.setWeekEndDate(monday.plusDays(6));
        testMenu.setStatus(WeeklyMenu.MenuStatus.DRAFT);
        testMenu.setTotalCalories(0);
        testMenu.setTotalProtein(0);
        testMenu.setTotalCarbs(0);
        testMenu.setTotalFats(0);

        try {
            var idField = WeeklyMenu.class.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testMenu, 1L);
        } catch (Exception e) {
            // Ignore
        }
    }

    @Test
    void createWeeklyMenu_Success() {
        CreateWeeklyMenuRequest request = CreateWeeklyMenuRequest.builder()
                .weekStartDate(monday)
                .build();

        when(weeklyMenuRepository.findByWeekStartDate(monday)).thenReturn(Optional.empty());
        when(weeklyMenuRepository.save(any(WeeklyMenu.class))).thenReturn(testMenu);
        when(menuDayRepository.findByWeeklyMenuIdOrderByDayOfWeekAscMealTypeAsc(1L))
                .thenReturn(Collections.emptyList());

        WeeklyMenuResponse response = weeklyMenuService.createWeeklyMenu(request);

        assertNotNull(response);
        assertEquals(monday, response.getWeekStartDate());
        assertEquals(WeeklyMenu.MenuStatus.DRAFT, response.getStatus());
        verify(weeklyMenuRepository).save(any(WeeklyMenu.class));
    }

    @Test
    void createWeeklyMenu_NotMonday() {
        LocalDate tuesday = LocalDate.of(2026, 2, 10);
        CreateWeeklyMenuRequest request = CreateWeeklyMenuRequest.builder()
                .weekStartDate(tuesday)
                .build();

        assertThrows(BadRequestException.class, () -> weeklyMenuService.createWeeklyMenu(request));
    }

    @Test
    void createWeeklyMenu_AlreadyExists() {
        CreateWeeklyMenuRequest request = CreateWeeklyMenuRequest.builder()
                .weekStartDate(monday)
                .build();

        when(weeklyMenuRepository.findByWeekStartDate(monday)).thenReturn(Optional.of(testMenu));

        assertThrows(BadRequestException.class, () -> weeklyMenuService.createWeeklyMenu(request));
    }

    @Test
    void getMenuById_Success() {
        when(weeklyMenuRepository.findById(1L)).thenReturn(Optional.of(testMenu));
        when(menuDayRepository.findByWeeklyMenuIdOrderByDayOfWeekAscMealTypeAsc(1L))
                .thenReturn(Collections.emptyList());

        WeeklyMenuResponse response = weeklyMenuService.getMenuById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getMenuById_NotFound() {
        when(weeklyMenuRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> weeklyMenuService.getMenuById(1L));
    }

    @Test
    void getPublishedMenus_Success() {
        testMenu.setStatus(WeeklyMenu.MenuStatus.PUBLISHED);
        when(weeklyMenuRepository.findByStatusOrderByWeekStartDateDesc(WeeklyMenu.MenuStatus.PUBLISHED))
                .thenReturn(List.of(testMenu));
        when(menuDayRepository.findByWeeklyMenuIdOrderByDayOfWeekAscMealTypeAsc(1L))
                .thenReturn(Collections.emptyList());

        List<WeeklyMenuResponse> menus = weeklyMenuService.getPublishedMenus();

        assertNotNull(menus);
        assertEquals(1, menus.size());
    }

    @Test
    void publishMenu_Success() {
        when(weeklyMenuRepository.findById(1L)).thenReturn(Optional.of(testMenu));
        when(weeklyMenuRepository.existsByWeekStartDateAndStatus(monday, WeeklyMenu.MenuStatus.PUBLISHED))
                .thenReturn(false);
        when(weeklyMenuRepository.save(any(WeeklyMenu.class))).thenReturn(testMenu);
        when(menuDayRepository.findByWeeklyMenuIdOrderByDayOfWeekAscMealTypeAsc(1L))
                .thenReturn(Collections.emptyList());

        WeeklyMenuResponse response = weeklyMenuService.publishMenu(1L);

        assertNotNull(response);
        verify(weeklyMenuRepository).save(any(WeeklyMenu.class));
    }

    @Test
    void publishMenu_AlreadyPublished() {
        testMenu.setStatus(WeeklyMenu.MenuStatus.PUBLISHED);
        when(weeklyMenuRepository.findById(1L)).thenReturn(Optional.of(testMenu));

        assertThrows(BadRequestException.class, () -> weeklyMenuService.publishMenu(1L));
    }

    @Test
    void deleteMenu_Success() {
        when(weeklyMenuRepository.findById(1L)).thenReturn(Optional.of(testMenu));

        weeklyMenuService.deleteMenu(1L);

        verify(weeklyMenuRepository).delete(testMenu);
    }

    @Test
    void deleteMenu_Published() {
        testMenu.setStatus(WeeklyMenu.MenuStatus.PUBLISHED);
        when(weeklyMenuRepository.findById(1L)).thenReturn(Optional.of(testMenu));

        assertThrows(BadRequestException.class, () -> weeklyMenuService.deleteMenu(1L));
    }
}
