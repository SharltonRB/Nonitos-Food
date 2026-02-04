package com.nonitos.food.repository;

import com.nonitos.food.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Order} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds an order by order code.
     *
     * @param orderCode the order code
     * @return optional containing the order if found
     */
    Optional<Order> findByOrderCode(String orderCode);

    /**
     * Finds all orders for a client.
     *
     * @param clientId the client ID
     * @return list of orders
     */
    List<Order> findByClientIdOrderByCreatedAtDesc(Long clientId);

    /**
     * Finds all orders by status.
     *
     * @param status the order status
     * @return list of orders
     */
    List<Order> findByStatusOrderByCreatedAtDesc(Order.OrderStatus status);

    /**
     * Checks if order code exists.
     *
     * @param orderCode the order code
     * @return true if exists, false otherwise
     */
    boolean existsByOrderCode(String orderCode);

    /**
     * Counts orders by status.
     *
     * @param status the order status
     * @return count of orders with the status
     */
    long countByStatus(Order.OrderStatus status);
}
