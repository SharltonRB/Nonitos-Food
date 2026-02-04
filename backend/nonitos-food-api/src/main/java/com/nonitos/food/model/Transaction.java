package com.nonitos.food.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a payment transaction.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Entity
@Table(name = "transactions")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseEntity {

    /** Reference to the order */
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /** Transaction reference/ID from payment provider */
    @Column(nullable = false, unique = true, length = 100)
    private String transactionReference;

    /** Payment method */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    /** Transaction status */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status;

    /** Transaction amount */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /** Currency code */
    @Column(nullable = false, length = 3)
    private String currency;

    /** Payment provider response */
    @Column(columnDefinition = "TEXT")
    private String providerResponse;

    /** Proof of payment URL (for bank transfer/SINPE) */
    @Column(length = 500)
    private String proofOfPaymentUrl;

    /** Processed at timestamp */
    private LocalDateTime processedAt;

    /** Failed reason */
    @Column(length = 500)
    private String failureReason;

    public enum PaymentMethod {
        CREDIT_CARD,
        BANK_TRANSFER,
        SINPE_MOVIL
    }

    public enum TransactionStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED,
        REFUNDED
    }
}
