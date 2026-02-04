package com.nonitos.food.service;

import com.nonitos.food.dto.order.CancelOrderRequest;
import com.nonitos.food.dto.order.CreateOrderRequest;
import com.nonitos.food.dto.order.OrderResponse;
import com.nonitos.food.dto.order.UpdateOrderStatusRequest;
import com.nonitos.food.exception.BadRequestException;
import com.nonitos.food.exception.ResourceNotFoundException;
import com.nonitos.food.model.*;
import com.nonitos.food.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Service for managing orders.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final WeeklyMenuRepository weeklyMenuRepository;
    private final UserRepository userRepository;

    private static final BigDecimal PRICE_PER_MEAL = new BigDecimal("10.00");
    private static final int CANCELLATION_HOURS_LIMIT = 24;

    /**
     * Creates a new order.
     *
     * @param userId the user ID
     * @param request the order creation request
     * @return the created order
     */
    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        User client = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        WeeklyMenu menu = weeklyMenuRepository.findById(request.getWeeklyMenuId())
                .orElseThrow(() -> new ResourceNotFoundException("Weekly menu not found"));

        if (menu.getStatus() != WeeklyMenu.MenuStatus.PUBLISHED) {
            throw new BadRequestException("Menu is not published");
        }

        if (request.getPickupDateTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Pickup date must be in the future");
        }

        // Calculate total amount
        int totalMeals = 7 * request.getMealsPerDay(); // 7 days
        BigDecimal totalAmount = PRICE_PER_MEAL.multiply(new BigDecimal(totalMeals));

        String orderCode = generateUniqueOrderCode();
        String qrCode = generateQRCode(orderCode);

        Order order = Order.builder()
                .orderCode(orderCode)
                .client(client)
                .weeklyMenu(menu)
                .status(Order.OrderStatus.PENDING_PAYMENT)
                .totalAmount(totalAmount)
                .mealsPerDay(request.getMealsPerDay())
                .includeBreakfast(request.getIncludeBreakfast())
                .includeLunch(request.getIncludeLunch())
                .includeDinner(request.getIncludeDinner())
                .pickupDateTime(request.getPickupDateTime())
                .qrCode(qrCode)
                .specialInstructions(request.getSpecialInstructions())
                .build();

        order = orderRepository.save(order);
        addStatusHistory(order, null, Order.OrderStatus.PENDING_PAYMENT, client, "Order created");

        log.info("Created order {} for user {}", orderCode, userId);
        return buildOrderResponse(order);
    }

    /**
     * Gets an order by ID.
     *
     * @param id the order ID
     * @param userId the user ID (for authorization)
     * @return the order
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id, Long userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getClient().getId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to order");
        }

        return buildOrderResponse(order);
    }

    /**
     * Gets all orders for a client.
     *
     * @param userId the user ID
     * @return list of orders
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getClientOrders(Long userId) {
        return orderRepository.findByClientIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::buildOrderResponse)
                .collect(Collectors.toList());
    }

    /**
     * Gets all orders (Admin).
     *
     * @return list of all orders
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::buildOrderResponse)
                .collect(Collectors.toList());
    }

    /**
     * Updates order status (Admin).
     *
     * @param id the order ID
     * @param request the status update request
     * @param adminId the admin user ID
     * @return the updated order
     */
    @Transactional
    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request, Long adminId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(request.getStatus());
        Order.OrderStatus previousStatus = order.getStatus();

        if (!isValidStatusTransition(previousStatus, newStatus)) {
            throw new BadRequestException("Invalid status transition");
        }

        order.setStatus(newStatus);
        orderRepository.save(order);

        addStatusHistory(order, previousStatus, newStatus, admin, request.getNotes());

        log.info("Updated order {} status from {} to {}", order.getOrderCode(), previousStatus, newStatus);
        return buildOrderResponse(order);
    }

    /**
     * Cancels an order.
     *
     * @param id the order ID
     * @param request the cancellation request
     * @param userId the user ID
     * @return the cancelled order
     */
    @Transactional
    public OrderResponse cancelOrder(Long id, CancelOrderRequest request, Long userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getClient().getId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to order");
        }

        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new BadRequestException("Order is already cancelled");
        }

        if (order.getStatus() == Order.OrderStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel completed order");
        }

        // Check cancellation policy (24 hours before pickup)
        LocalDateTime cancellationDeadline = order.getPickupDateTime().minusHours(CANCELLATION_HOURS_LIMIT);
        if (LocalDateTime.now().isAfter(cancellationDeadline)) {
            throw new BadRequestException("Cannot cancel order within 24 hours of pickup");
        }

        Order.OrderStatus previousStatus = order.getStatus();
        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancellationReason(request.getReason());
        order.setCancelledAt(LocalDateTime.now());
        orderRepository.save(order);

        addStatusHistory(order, previousStatus, Order.OrderStatus.CANCELLED, order.getClient(), request.getReason());

        log.info("Cancelled order {}", order.getOrderCode());
        return buildOrderResponse(order);
    }

    private String generateUniqueOrderCode() {
        String code;
        do {
            code = generateRandomCode();
        } while (orderRepository.existsByOrderCode(code));
        return code;
    }

    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }

    private String generateQRCode(String orderCode) {
        // Mock QR code generation - in production, use a QR library
        return "QR_CODE_" + orderCode;
    }

    private boolean isValidStatusTransition(Order.OrderStatus from, Order.OrderStatus to) {
        return switch (from) {
            case PENDING_PAYMENT -> to == Order.OrderStatus.PAID || to == Order.OrderStatus.CANCELLED;
            case PAID -> to == Order.OrderStatus.IN_PREPARATION || to == Order.OrderStatus.CANCELLED;
            case IN_PREPARATION -> to == Order.OrderStatus.READY_FOR_PICKUP;
            case READY_FOR_PICKUP -> to == Order.OrderStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }

    private void addStatusHistory(Order order, Order.OrderStatus previousStatus, 
                                   Order.OrderStatus newStatus, User changedBy, String notes) {
        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .changedBy(changedBy)
                .notes(notes)
                .changedAt(LocalDateTime.now())
                .build();

        orderStatusHistoryRepository.save(history);
    }

    private OrderResponse buildOrderResponse(Order order) {
        List<OrderStatusHistory> history = orderStatusHistoryRepository
                .findByOrderIdOrderByChangedAtDesc(order.getId());

        List<OrderResponse.StatusHistoryInfo> statusHistory = history.stream()
                .map(h -> OrderResponse.StatusHistoryInfo.builder()
                        .previousStatus(h.getPreviousStatus())
                        .newStatus(h.getNewStatus())
                        .changedByName(h.getChangedBy() != null ? h.getChangedBy().getFullName() : null)
                        .notes(h.getNotes())
                        .changedAt(h.getChangedAt())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .clientId(order.getClient().getId())
                .clientName(order.getClient().getFullName())
                .weeklyMenuId(order.getWeeklyMenu().getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .mealsPerDay(order.getMealsPerDay())
                .includeBreakfast(order.getIncludeBreakfast())
                .includeLunch(order.getIncludeLunch())
                .includeDinner(order.getIncludeDinner())
                .pickupDateTime(order.getPickupDateTime())
                .qrCode(order.getQrCode())
                .specialInstructions(order.getSpecialInstructions())
                .cancellationReason(order.getCancellationReason())
                .cancelledAt(order.getCancelledAt())
                .createdAt(order.getCreatedAt())
                .statusHistory(statusHistory)
                .build();
    }
}
