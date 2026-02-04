package com.nonitos.food.repository;

import com.nonitos.food.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Transaction} entity operations.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds a transaction by reference.
     *
     * @param transactionReference the transaction reference
     * @return optional containing the transaction if found
     */
    Optional<Transaction> findByTransactionReference(String transactionReference);

    /**
     * Finds all transactions for an order.
     *
     * @param orderId the order ID
     * @return list of transactions
     */
    List<Transaction> findByOrderIdOrderByCreatedAtDesc(Long orderId);

    /**
     * Finds the latest successful transaction for an order.
     *
     * @param orderId the order ID
     * @param status the transaction status
     * @return optional containing the transaction if found
     */
    Optional<Transaction> findFirstByOrderIdAndStatusOrderByCreatedAtDesc(
        Long orderId, 
        Transaction.TransactionStatus status
    );
}
