package com.nonitos.food.service;

import com.nonitos.food.dto.dashboard.DashboardMetricsResponse;
import com.nonitos.food.dto.dashboard.UpdateUserRequest;
import com.nonitos.food.dto.dashboard.UserManagementResponse;
import com.nonitos.food.exception.BadRequestException;
import com.nonitos.food.exception.ResourceNotFoundException;
import com.nonitos.food.model.Order;
import com.nonitos.food.model.Transaction;
import com.nonitos.food.model.User;
import com.nonitos.food.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service for admin dashboard operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final DishRepository dishRepository;
    private final WeeklyMenuRepository weeklyMenuRepository;

    /**
     * Gets dashboard metrics.
     *
     * @return dashboard metrics
     */
    public DashboardMetricsResponse getMetrics() {
        long totalUsers = userRepository.count();
        long totalClients = userRepository.countByRole(User.UserRole.CLIENT);
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.countByStatus(Order.OrderStatus.PENDING_PAYMENT) +
                orderRepository.countByStatus(Order.OrderStatus.PAID);
        long completedOrders = orderRepository.countByStatus(Order.OrderStatus.COMPLETED);

        BigDecimal totalRevenue = transactionRepository.findAll().stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.COMPLETED)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pendingRevenue = transactionRepository.findAll().stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.PENDING)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalDishes = dishRepository.count();
        long activeDishes = dishRepository.countByIsActive(true);
        long totalMenus = weeklyMenuRepository.count();
        long publishedMenus = weeklyMenuRepository.countByStatus(com.nonitos.food.model.WeeklyMenu.MenuStatus.PUBLISHED);

        return DashboardMetricsResponse.builder()
                .totalUsers(totalUsers)
                .totalClients(totalClients)
                .totalOrders(totalOrders)
                .pendingOrders(pendingOrders)
                .completedOrders(completedOrders)
                .totalRevenue(totalRevenue)
                .pendingRevenue(pendingRevenue)
                .totalDishes(totalDishes)
                .activeDishes(activeDishes)
                .totalMenus(totalMenus)
                .publishedMenus(publishedMenus)
                .build();
    }

    /**
     * Gets all users with pagination.
     *
     * @param pageable pagination parameters
     * @return page of users
     */
    public Page<UserManagementResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::toUserManagementResponse);
    }

    /**
     * Gets a user by ID.
     *
     * @param userId the user ID
     * @return user information
     */
    public UserManagementResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toUserManagementResponse(user);
    }

    /**
     * Updates a user.
     *
     * @param userId the user ID
     * @param request the update request
     * @return updated user
     */
    @Transactional
    public UserManagementResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());

        user = userRepository.save(user);
        log.info("User {} updated by admin", userId);

        return toUserManagementResponse(user);
    }

    /**
     * Deletes a user.
     *
     * @param userId the user ID
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() == User.UserRole.ADMIN) {
            throw new BadRequestException("Cannot delete admin user");
        }

        userRepository.delete(user);
        log.info("User {} deleted by admin", userId);
    }

    private UserManagementResponse toUserManagementResponse(User user) {
        return UserManagementResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .isEmailVerified(user.getIsEmailVerified())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLoginAt())
                .build();
    }
}
