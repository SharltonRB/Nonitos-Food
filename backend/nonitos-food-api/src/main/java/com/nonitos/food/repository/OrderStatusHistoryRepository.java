package com.nonitos.food.repository;

import com.nonitos.food.model.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link OrderStatusHistory} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {

    /**
     * Finds all status history for an order.
     *
     * @param orderId the order ID
     * @return list of status history
     */
    List<OrderStatusHistory> findByOrderIdOrderByChangedAtDesc(Long orderId);
}
