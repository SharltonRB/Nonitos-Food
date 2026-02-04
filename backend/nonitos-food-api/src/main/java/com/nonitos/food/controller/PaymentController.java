package com.nonitos.food.controller;

import com.nonitos.food.dto.ApiResponse;
import com.nonitos.food.dto.payment.CreditCardPaymentRequest;
import com.nonitos.food.dto.payment.ManualPaymentRequest;
import com.nonitos.food.dto.payment.TransactionResponse;
import com.nonitos.food.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for payment management.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Processes a credit card payment (Client).
     *
     * @param request the payment request
     * @return the transaction
     */
    @PostMapping("/credit-card")
    public ResponseEntity<ApiResponse<TransactionResponse>> processCreditCardPayment(
            @Valid @RequestBody CreditCardPaymentRequest request
    ) {
        TransactionResponse transaction = paymentService.processCreditCardPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment processed", transaction));
    }

    /**
     * Submits a manual payment (Client).
     *
     * @param request the payment request
     * @return the transaction
     */
    @PostMapping("/manual")
    public ResponseEntity<ApiResponse<TransactionResponse>> submitManualPayment(
            @Valid @RequestBody ManualPaymentRequest request
    ) {
        TransactionResponse transaction = paymentService.submitManualPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment submitted for verification", transaction));
    }

    /**
     * Verifies a manual payment (Admin).
     *
     * @param transactionId the transaction ID
     * @param approved whether to approve or reject
     * @return the updated transaction
     */
    @PostMapping("/{transactionId}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TransactionResponse>> verifyManualPayment(
            @PathVariable Long transactionId,
            @RequestParam boolean approved
    ) {
        TransactionResponse transaction = paymentService.verifyManualPayment(transactionId, approved);
        String message = approved ? "Payment approved" : "Payment rejected";
        return ResponseEntity.ok(ApiResponse.success(message, transaction));
    }

    /**
     * Gets all transactions for an order.
     *
     * @param orderId the order ID
     * @return list of transactions
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getOrderTransactions(
            @PathVariable Long orderId
    ) {
        List<TransactionResponse> transactions = paymentService.getOrderTransactions(orderId);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    /**
     * Gets a transaction by ID.
     *
     * @param id the transaction ID
     * @return the transaction
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransactionById(@PathVariable Long id) {
        TransactionResponse transaction = paymentService.getTransactionById(id);
        return ResponseEntity.ok(ApiResponse.success(transaction));
    }
}
