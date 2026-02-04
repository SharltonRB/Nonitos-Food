package com.nonitos.food.repository;

import com.nonitos.food.model.WeeklyMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link WeeklyMenu} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface WeeklyMenuRepository extends JpaRepository<WeeklyMenu, Long> {

    /**
     * Finds a menu by week start date.
     *
     * @param weekStartDate the week start date
     * @return optional containing the menu if found
     */
    Optional<WeeklyMenu> findByWeekStartDate(LocalDate weekStartDate);

    /**
     * Finds all menus by status.
     *
     * @param status the menu status
     * @return list of menus
     */
    List<WeeklyMenu> findByStatusOrderByWeekStartDateDesc(WeeklyMenu.MenuStatus status);

    /**
     * Checks if a published menu exists for a given week.
     *
     * @param weekStartDate the week start date
     * @param status the menu status
     * @return true if exists, false otherwise
     */
    boolean existsByWeekStartDateAndStatus(LocalDate weekStartDate, WeeklyMenu.MenuStatus status);

    /**
     * Finds all published menus.
     *
     * @param status the menu status
     * @return list of published menus
     */
    List<WeeklyMenu> findByStatus(WeeklyMenu.MenuStatus status);
}
