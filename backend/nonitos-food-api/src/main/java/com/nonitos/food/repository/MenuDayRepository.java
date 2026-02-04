package com.nonitos.food.repository;

import com.nonitos.food.model.MenuDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link MenuDay} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface MenuDayRepository extends JpaRepository<MenuDay, Long> {

    /**
     * Finds all menu days for a weekly menu.
     *
     * @param weeklyMenuId the weekly menu ID
     * @return list of menu days
     */
    List<MenuDay> findByWeeklyMenuIdOrderByDayOfWeekAscMealTypeAsc(Long weeklyMenuId);

    /**
     * Finds a specific menu day.
     *
     * @param weeklyMenuId the weekly menu ID
     * @param dayOfWeek the day of week
     * @param mealType the meal type
     * @return optional containing the menu day if found
     */
    Optional<MenuDay> findByWeeklyMenuIdAndDayOfWeekAndMealType(
        Long weeklyMenuId,
        DayOfWeek dayOfWeek,
        MenuDay.MealType mealType
    );

    /**
     * Deletes all menu days for a weekly menu.
     *
     * @param weeklyMenuId the weekly menu ID
     */
    void deleteByWeeklyMenuId(Long weeklyMenuId);
}
