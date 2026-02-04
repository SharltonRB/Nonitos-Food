package com.nonitos.food.service;

import com.nonitos.food.dto.payment.CreditCardPaymentRequest;
import com.nonitos.food.dto.payment.ManualPaymentRequest;
import com.nonitos.food.dto.payment.TransactionResponse;
import com.nonitos.food.exception.BadRequestException;
import com.nonitos.food.exception.ResourceNotFoundException;
import com.nonitos.food.model.Order;
import com.nonitos.food.model.Transaction;
import com.nonitos.food.repository.OrderRepository;
import com.nonitos.food.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing payments and transactions.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;

    /**
     * Processes a credit card payment (Mock Stripe).
     *
     * @param request the payment request
     * @return the transaction
     */
    @Transactional
    public TransactionResponse processCreditCardPayment(CreditCardPaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != Order.OrderStatus.PENDING_PAYMENT) {
            throw new BadRequestException("Order is not pending payment");
        }

        // Check if already paid
        if (transactionRepository.findFirstByOrderIdAndStatusOrderByCreatedAtDesc(
                order.getId(), Transaction.TransactionStatus.COMPLETED).isPresent()) {
            throw new BadRequestException("Order is already paid");
        }

        // Mock Stripe payment processing
        String transactionRef = "stripe_" + UUID.randomUUID().toString();
        boolean paymentSuccess = mockStripePayment(request);

        Transaction transaction = Transaction.builder()
                .order(order)
                .transactionReference(transactionRef)
                .paymentMethod(Transaction.PaymentMethod.CREDIT_CARD)
                .status(paymentSuccess ? Transaction.TransactionStatus.COMPLETED : Transaction.TransactionStatus.FAILED)
                .amount(order.getTotalAmount())
                .currency("CRC")
                .providerResponse(paymentSuccess ? "Payment successful" : "Payment declined")
                .processedAt(paymentSuccess ? LocalDateTime.now() : null)
                .failureReason(paymentSuccess ? null : "Card declined by issuer")
                .build();

        transaction = transactionRepository.save(transaction);

        if (paymentSuccess) {
            order.setStatus(Order.OrderStatus.PAID);
            orderRepository.save(order);
            log.info("Credit card payment successful for order {}", order.getOrderCode());
        } else {
            log.warn("Credit card payment failed for order {}", order.getOrderCode());
        }

        return buildTransactionResponse(transaction);
    }

    /**
     * Submits a manual payment (bank transfer/SINPE).
     *
     * @param request the payment request
     * @return the transaction
     */
    @Transactional
    public TransactionResponse submitManualPayment(ManualPaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != Order.OrderStatus.PENDING_PAYMENT) {
            throw new BadRequestException("Order is not pending payment");
        }

        Transaction.PaymentMethod paymentMethod = Transaction.PaymentMethod.valueOf(request.getPaymentMethod());

        Transaction transaction = Transaction.builder()
                .order(order)
                .transactionReference(request.getTransactionReference())
                .paymentMethod(paymentMethod)
                .status(Transaction.TransactionStatus.PENDING)
                .amount(order.getTotalAmount())
                .currency("CRC")
                .proofOfPaymentUrl(request.getProofOfPaymentUrl())
                .providerResponse("Awaiting admin verification")
                .build();

        transaction = transactionRepository.save(transaction);
        log.info("Manual payment submitted for order {}", order.getOrderCode());

        return buildTransactionResponse(transaction);
    }

    /**
     * Verifies a manual payment (Admin).
     *
     * @param transactionId the transaction ID
     * @param approved whether to approve or reject
     * @return the updated transaction
     */
    @Transactional
    public TransactionResponse verifyManualPayment(Long transactionId, boolean approved) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (transaction.getStatus() != Transaction.TransactionStatus.PENDING) {
            throw new BadRequestException("Transaction is not pending verification");
        }

        if (approved) {
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            transaction.setProcessedAt(LocalDateTime.now());
            transaction.setProviderResponse("Payment verified by admin");

            Order order = transaction.getOrder();
            order.setStatus(Order.OrderStatus.PAID);
            orderRepository.save(order);

            log.info("Manual payment approved for order {}", order.getOrderCode());
        } else {
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            transaction.setFailureReason("Payment rejected by admin");
            log.info("Manual payment rejected for transaction {}", transaction.getTransactionReference());
        }

        transactionRepository.save(transaction);
        return buildTransactionResponse(transaction);
    }

    /**
     * Gets all transactions for an order.
     *
     * @param orderId the order ID
     * @return list of transactions
     */
    @Transactional(readOnly = true)
    public List<TransactionResponse> getOrderTransactions(Long orderId) {
        return transactionRepository.findByOrderIdOrderByCreatedAtDesc(orderId)
                .stream()
                .map(this::buildTransactionResponse)
                .collect(Collectors.toList());
    }

    /**
     * Gets a transaction by ID.
     *
     * @param id the transaction ID
     * @return the transaction
     */
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return buildTransactionResponse(transaction);
    }

    /**
     * Mock Stripe payment processing.
     * In production, this would call the actual Stripe API.
     */
    private boolean mockStripePayment(CreditCardPaymentRequest request) {
        // Mock: Cards ending in even numbers succeed, odd numbers fail
        String lastDigit = request.getCardNumber().substring(request.getCardNumber().length() - 1);
        int digit = Integer.parseInt(lastDigit);
        
        // Simulate processing delay
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return digit % 2 == 0;
    }

    private TransactionResponse buildTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .orderId(transaction.getOrder().getId())
                .transactionReference(transaction.getTransactionReference())
                .paymentMethod(transaction.getPaymentMethod())
                .status(transaction.getStatus())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .proofOfPaymentUrl(transaction.getProofOfPaymentUrl())
                .processedAt(transaction.getProcessedAt())
                .failureReason(transaction.getFailureReason())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
