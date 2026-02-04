package com.nonitos.food.service;

import com.nonitos.food.dto.order.CancelOrderRequest;
import com.nonitos.food.dto.order.CreateOrderRequest;
import com.nonitos.food.dto.order.OrderResponse;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Mock
    private WeeklyMenuRepository weeklyMenuRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    private User testUser;
    private WeeklyMenu testMenu;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFullName("Test User");
        testUser.setRole(User.UserRole.CLIENT);
        setId(testUser, 1L);

        testMenu = new WeeklyMenu();
        testMenu.setWeekStartDate(LocalDate.now().plusDays(7));
        testMenu.setWeekEndDate(LocalDate.now().plusDays(13));
        testMenu.setStatus(WeeklyMenu.MenuStatus.PUBLISHED);
        setId(testMenu, 1L);

        testOrder = new Order();
        testOrder.setOrderCode("TEST1234");
        testOrder.setClient(testUser);
        testOrder.setWeeklyMenu(testMenu);
        testOrder.setStatus(Order.OrderStatus.PENDING_PAYMENT);
        testOrder.setTotalAmount(new BigDecimal("210.00"));
        testOrder.setMealsPerDay(3);
        testOrder.setIncludeBreakfast(true);
        testOrder.setIncludeLunch(true);
        testOrder.setIncludeDinner(true);
        testOrder.setPickupDateTime(LocalDateTime.now().plusDays(7));
        testOrder.setQrCode("QR_TEST1234");
        setId(testOrder, 1L);
    }

    @Test
    void createOrder_Success() {
        CreateOrderRequest request = CreateOrderRequest.builder()
                .weeklyMenuId(1L)
                .mealsPerDay(3)
                .includeBreakfast(true)
                .includeLunch(true)
                .includeDinner(true)
                .pickupDateTime(LocalDateTime.now().plusDays(7))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(weeklyMenuRepository.findById(1L)).thenReturn(Optional.of(testMenu));
        when(orderRepository.existsByOrderCode(anyString())).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(orderStatusHistoryRepository.findByOrderIdOrderByChangedAtDesc(1L))
                .thenReturn(Collections.emptyList());

        OrderResponse response = orderService.createOrder(1L, request);

        assertNotNull(response);
        assertEquals("TEST1234", response.getOrderCode());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_MenuNotPublished() {
        testMenu.setStatus(WeeklyMenu.MenuStatus.DRAFT);
        CreateOrderRequest request = CreateOrderRequest.builder()
                .weeklyMenuId(1L)
                .mealsPerDay(3)
                .includeBreakfast(true)
                .includeLunch(true)
                .includeDinner(true)
                .pickupDateTime(LocalDateTime.now().plusDays(7))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(weeklyMenuRepository.findById(1L)).thenReturn(Optional.of(testMenu));

        assertThrows(BadRequestException.class, () -> orderService.createOrder(1L, request));
    }

    @Test
    void getOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderStatusHistoryRepository.findByOrderIdOrderByChangedAtDesc(1L))
                .thenReturn(Collections.emptyList());

        OrderResponse response = orderService.getOrderById(1L, 1L);

        assertNotNull(response);
        assertEquals("TEST1234", response.getOrderCode());
    }

    @Test
    void getOrderById_Unauthorized() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        assertThrows(BadRequestException.class, () -> orderService.getOrderById(1L, 999L));
    }

    @Test
    void getClientOrders_Success() {
        when(orderRepository.findByClientIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(testOrder));
        when(orderStatusHistoryRepository.findByOrderIdOrderByChangedAtDesc(1L))
                .thenReturn(Collections.emptyList());

        List<OrderResponse> orders = orderService.getClientOrders(1L);

        assertNotNull(orders);
        assertEquals(1, orders.size());
    }

    @Test
    void cancelOrder_Success() {
        testOrder.setPickupDateTime(LocalDateTime.now().plusDays(2));
        CancelOrderRequest request = CancelOrderRequest.builder()
                .reason("Changed plans")
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(orderStatusHistoryRepository.findByOrderIdOrderByChangedAtDesc(1L))
                .thenReturn(Collections.emptyList());

        OrderResponse response = orderService.cancelOrder(1L, request, 1L);

        assertNotNull(response);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void cancelOrder_TooLate() {
        testOrder.setPickupDateTime(LocalDateTime.now().plusHours(12));
        CancelOrderRequest request = CancelOrderRequest.builder()
                .reason("Changed plans")
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        assertThrows(BadRequestException.class, () -> orderService.cancelOrder(1L, request, 1L));
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
