package com.nonitos.food.controller;

import com.nonitos.food.dto.ApiResponse;
import com.nonitos.food.dto.order.CancelOrderRequest;
import com.nonitos.food.dto.order.CreateOrderRequest;
import com.nonitos.food.dto.order.OrderResponse;
import com.nonitos.food.dto.order.UpdateOrderStatusRequest;
import com.nonitos.food.model.User;
import com.nonitos.food.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for order management.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Creates a new order (Client).
     *
     * @param user the authenticated user
     * @param request the order creation request
     * @return the created order
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        OrderResponse order = orderService.createOrder(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", order));
    }

    /**
     * Gets an order by ID (Client - own orders only).
     *
     * @param user the authenticated user
     * @param id the order ID
     * @return the order
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        OrderResponse order = orderService.getOrderById(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    /**
     * Gets all orders for the authenticated client.
     *
     * @param user the authenticated user
     * @return list of orders
     */
    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(
            @AuthenticationPrincipal User user
    ) {
        List<OrderResponse> orders = orderService.getClientOrders(user.getId());
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    /**
     * Gets all orders (Admin).
     *
     * @return list of all orders
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    /**
     * Updates order status (Admin).
     *
     * @param user the authenticated admin
     * @param id the order ID
     * @param request the status update request
     * @return the updated order
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        OrderResponse order = orderService.updateOrderStatus(id, request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", order));
    }

    /**
     * Cancels an order (Client).
     *
     * @param user the authenticated user
     * @param id the order ID
     * @param request the cancellation request
     * @return the cancelled order
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody CancelOrderRequest request
    ) {
        OrderResponse order = orderService.cancelOrder(id, request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", order));
    }
}
