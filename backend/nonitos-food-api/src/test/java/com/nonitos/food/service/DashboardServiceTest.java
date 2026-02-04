package com.nonitos.food.service;

import com.nonitos.food.dto.dashboard.DashboardMetricsResponse;
import com.nonitos.food.dto.dashboard.UpdateUserRequest;
import com.nonitos.food.dto.dashboard.UserManagementResponse;
import com.nonitos.food.exception.BadRequestException;
import com.nonitos.food.exception.ResourceNotFoundException;
import com.nonitos.food.model.Order;
import com.nonitos.food.model.Transaction;
import com.nonitos.food.model.User;
import com.nonitos.food.model.WeeklyMenu;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private WeeklyMenuRepository weeklyMenuRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFullName("Test User");
        testUser.setRole(User.UserRole.CLIENT);
        testUser.setIsEmailVerified(true);
        setId(testUser, 1L);
    }

    @Test
    void getMetrics_Success() {
        when(userRepository.count()).thenReturn(10L);
        when(userRepository.countByRole(User.UserRole.CLIENT)).thenReturn(8L);
        when(orderRepository.count()).thenReturn(20L);
        when(orderRepository.countByStatus(Order.OrderStatus.PENDING_PAYMENT)).thenReturn(3L);
        when(orderRepository.countByStatus(Order.OrderStatus.PAID)).thenReturn(2L);
        when(orderRepository.countByStatus(Order.OrderStatus.COMPLETED)).thenReturn(15L);
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());
        when(dishRepository.count()).thenReturn(30L);
        when(dishRepository.countByIsActive(true)).thenReturn(25L);
        when(weeklyMenuRepository.count()).thenReturn(5L);
        when(weeklyMenuRepository.countByStatus(WeeklyMenu.MenuStatus.PUBLISHED)).thenReturn(1L);

        DashboardMetricsResponse metrics = dashboardService.getMetrics();

        assertNotNull(metrics);
        assertEquals(10L, metrics.getTotalUsers());
        assertEquals(8L, metrics.getTotalClients());
        assertEquals(20L, metrics.getTotalOrders());
        assertEquals(5L, metrics.getPendingOrders());
        assertEquals(15L, metrics.getCompletedOrders());
    }

    @Test
    void getAllUsers_Success() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<User> userPage = new PageImpl<>(List.of(testUser));
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserManagementResponse> result = dashboardService.getAllUsers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserManagementResponse result = dashboardService.getUserById(1L);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                dashboardService.getUserById(1L)
        );
    }

    @Test
    void updateUser_Success() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .email("test@example.com")
                .fullName("Updated Name")
                .role(User.UserRole.CLIENT)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserManagementResponse result = dashboardService.updateUser(1L, request);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_EmailAlreadyExists() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .email("other@example.com")
                .fullName("Updated Name")
                .role(User.UserRole.CLIENT)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("other@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () ->
                dashboardService.updateUser(1L, request)
        );
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        dashboardService.deleteUser(1L);

        verify(userRepository).delete(testUser);
    }

    @Test
    void deleteUser_CannotDeleteAdmin() {
        testUser.setRole(User.UserRole.ADMIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        assertThrows(BadRequestException.class, () ->
                dashboardService.deleteUser(1L)
        );
    }

    private void setId(Object entity, Long id) {
        try {
            var idField = entity.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (Exception e) {
            // Ignore
        }
    }
}
