package com.nonitos.food.service;

import com.nonitos.food.dto.menu.CreateWeeklyMenuRequest;
import com.nonitos.food.dto.menu.UpdateWeeklyMenuRequest;
import com.nonitos.food.dto.menu.WeeklyMenuResponse;
import com.nonitos.food.exception.BadRequestException;
import com.nonitos.food.exception.ResourceNotFoundException;
import com.nonitos.food.model.*;
import com.nonitos.food.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing weekly menus.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WeeklyMenuService {

    private final WeeklyMenuRepository weeklyMenuRepository;
    private final MenuDayRepository menuDayRepository;
    private final DishRepository dishRepository;
    private final DishImageRepository dishImageRepository;

    /**
     * Creates a new weekly menu.
     *
     * @param request the menu creation request
     * @return the created menu
     */
    @Transactional
    public WeeklyMenuResponse createWeeklyMenu(CreateWeeklyMenuRequest request) {
        LocalDate weekStart = request.getWeekStartDate();
        
        if (!weekStart.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            throw new BadRequestException("Week start date must be a Monday");
        }

        if (weeklyMenuRepository.findByWeekStartDate(weekStart).isPresent()) {
            throw new BadRequestException("Menu already exists for this week");
        }

        LocalDate weekEnd = weekStart.plusDays(6);

        WeeklyMenu menu = WeeklyMenu.builder()
                .weekStartDate(weekStart)
                .weekEndDate(weekEnd)
                .status(WeeklyMenu.MenuStatus.DRAFT)
                .totalCalories(0)
                .totalProtein(0)
                .totalCarbs(0)
                .totalFats(0)
                .build();

        menu = weeklyMenuRepository.save(menu);

        if (request.getMenuDays() != null && !request.getMenuDays().isEmpty()) {
            addMenuDays(menu, request.getMenuDays());
            menu = calculateNutritionalSummary(menu);
        }

        log.info("Created weekly menu for week starting {}", weekStart);
        return buildMenuResponse(menu);
    }

    /**
     * Gets a weekly menu by ID.
     *
     * @param id the menu ID
     * @return the menu
     */
    @Transactional(readOnly = true)
    public WeeklyMenuResponse getMenuById(Long id) {
        WeeklyMenu menu = weeklyMenuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
        return buildMenuResponse(menu);
    }

    /**
     * Gets all published menus.
     *
     * @return list of published menus
     */
    @Transactional(readOnly = true)
    public List<WeeklyMenuResponse> getPublishedMenus() {
        return weeklyMenuRepository.findByStatusOrderByWeekStartDateDesc(WeeklyMenu.MenuStatus.PUBLISHED)
                .stream()
                .map(this::buildMenuResponse)
                .collect(Collectors.toList());
    }

    /**
     * Updates a weekly menu.
     *
     * @param id the menu ID
     * @param request the update request
     * @return the updated menu
     */
    @Transactional
    public WeeklyMenuResponse updateMenu(Long id, UpdateWeeklyMenuRequest request) {
        WeeklyMenu menu = weeklyMenuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));

        if (menu.getStatus() == WeeklyMenu.MenuStatus.PUBLISHED) {
            throw new BadRequestException("Cannot update a published menu");
        }

        if (request.getMenuDays() != null) {
            menuDayRepository.deleteByWeeklyMenuId(menu.getId());
            addMenuDays(menu, convertToCreateRequests(request.getMenuDays()));
            menu = calculateNutritionalSummary(menu);
        }

        log.info("Updated weekly menu {}", id);
        return buildMenuResponse(menu);
    }

    /**
     * Publishes a weekly menu.
     *
     * @param id the menu ID
     * @return the published menu
     */
    @Transactional
    public WeeklyMenuResponse publishMenu(Long id) {
        WeeklyMenu menu = weeklyMenuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));

        if (menu.getStatus() == WeeklyMenu.MenuStatus.PUBLISHED) {
            throw new BadRequestException("Menu is already published");
        }

        if (weeklyMenuRepository.existsByWeekStartDateAndStatus(
                menu.getWeekStartDate(), WeeklyMenu.MenuStatus.PUBLISHED)) {
            throw new BadRequestException("A published menu already exists for this week");
        }

        menu.setStatus(WeeklyMenu.MenuStatus.PUBLISHED);
        weeklyMenuRepository.save(menu);

        log.info("Published weekly menu {}", id);
        return buildMenuResponse(menu);
    }

    /**
     * Deletes a weekly menu.
     *
     * @param id the menu ID
     */
    @Transactional
    public void deleteMenu(Long id) {
        WeeklyMenu menu = weeklyMenuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));

        if (menu.getStatus() == WeeklyMenu.MenuStatus.PUBLISHED) {
            throw new BadRequestException("Cannot delete a published menu");
        }

        weeklyMenuRepository.delete(menu);
        log.info("Deleted weekly menu {}", id);
    }

    private void addMenuDays(WeeklyMenu menu, List<CreateWeeklyMenuRequest.MenuDayRequest> requests) {
        for (CreateWeeklyMenuRequest.MenuDayRequest request : requests) {
            Dish dish = dishRepository.findById(request.getDishId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dish not found: " + request.getDishId()));

            MenuDay menuDay = MenuDay.builder()
                    .weeklyMenu(menu)
                    .dayOfWeek(request.getDayOfWeek())
                    .mealType(MenuDay.MealType.valueOf(request.getMealType()))
                    .dish(dish)
                    .build();

            menuDayRepository.save(menuDay);
        }
    }

    private WeeklyMenu calculateNutritionalSummary(WeeklyMenu menu) {
        List<MenuDay> menuDays = menuDayRepository.findByWeeklyMenuIdOrderByDayOfWeekAscMealTypeAsc(menu.getId());

        int totalCalories = 0;
        int totalProtein = 0;
        int totalCarbs = 0;
        int totalFats = 0;

        for (MenuDay menuDay : menuDays) {
            Dish dish = menuDay.getDish();
            totalCalories += dish.getCalories();
            totalProtein += dish.getProtein();
            totalCarbs += dish.getCarbs();
            totalFats += dish.getFats();
        }

        menu.setTotalCalories(totalCalories);
        menu.setTotalProtein(totalProtein);
        menu.setTotalCarbs(totalCarbs);
        menu.setTotalFats(totalFats);

        return weeklyMenuRepository.save(menu);
    }

    private WeeklyMenuResponse buildMenuResponse(WeeklyMenu menu) {
        List<MenuDay> menuDays = menuDayRepository.findByWeeklyMenuIdOrderByDayOfWeekAscMealTypeAsc(menu.getId());

        Map<DayOfWeek, WeeklyMenuResponse.DayMenus> menusByDay = new HashMap<>();

        for (MenuDay menuDay : menuDays) {
            DayOfWeek day = menuDay.getDayOfWeek();
            MenuDay.MealType mealType = menuDay.getMealType();
            Dish dish = menuDay.getDish();

            String imageUrl = dishImageRepository.findByDishIdOrderByDisplayOrderAsc(dish.getId())
                    .stream()
                    .filter(DishImage::getIsPrimary)
                    .findFirst()
                    .map(DishImage::getImageUrl)
                    .orElse(null);

            WeeklyMenuResponse.MealInfo mealInfo = WeeklyMenuResponse.MealInfo.builder()
                    .menuDayId(menuDay.getId())
                    .dishId(dish.getId())
                    .dishName(dish.getName())
                    .calories(dish.getCalories())
                    .protein(dish.getProtein())
                    .carbs(dish.getCarbs())
                    .fats(dish.getFats())
                    .imageUrl(imageUrl)
                    .build();

            menusByDay.putIfAbsent(day, WeeklyMenuResponse.DayMenus.builder().build());
            WeeklyMenuResponse.DayMenus dayMenus = menusByDay.get(day);

            switch (mealType) {
                case BREAKFAST -> dayMenus.setBreakfast(mealInfo);
                case LUNCH -> dayMenus.setLunch(mealInfo);
                case DINNER -> dayMenus.setDinner(mealInfo);
            }
        }

        return WeeklyMenuResponse.builder()
                .id(menu.getId())
                .weekStartDate(menu.getWeekStartDate())
                .weekEndDate(menu.getWeekEndDate())
                .status(menu.getStatus())
                .totalCalories(menu.getTotalCalories())
                .totalProtein(menu.getTotalProtein())
                .totalCarbs(menu.getTotalCarbs())
                .totalFats(menu.getTotalFats())
                .menusByDay(menusByDay)
                .build();
    }

    private List<CreateWeeklyMenuRequest.MenuDayRequest> convertToCreateRequests(
            List<UpdateWeeklyMenuRequest.MenuDayRequest> requests) {
        return requests.stream()
                .map(r -> CreateWeeklyMenuRequest.MenuDayRequest.builder()
                        .dayOfWeek(r.getDayOfWeek())
                        .mealType(r.getMealType())
                        .dishId(r.getDishId())
                        .build())
                .collect(Collectors.toList());
    }
}
