package com.nonitos.food.dto.payment;

import com.nonitos.food.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for transaction information.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private Long orderId;
    private String transactionReference;
    private Transaction.PaymentMethod paymentMethod;
    private Transaction.TransactionStatus status;
    private BigDecimal amount;
    private String currency;
    private String proofOfPaymentUrl;
    private LocalDateTime processedAt;
    private String failureReason;
    private LocalDateTime createdAt;
}
