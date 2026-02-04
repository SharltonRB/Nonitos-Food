package com.nonitos.food.service;

import com.nonitos.food.dto.payment.CreditCardPaymentRequest;
import com.nonitos.food.dto.payment.ManualPaymentRequest;
import com.nonitos.food.dto.payment.TransactionResponse;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Order testOrder;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFullName("Test User");
        setId(testUser, 1L);

        WeeklyMenu testMenu = new WeeklyMenu();
        testMenu.setWeekStartDate(LocalDate.now().plusDays(7));
        testMenu.setStatus(WeeklyMenu.MenuStatus.PUBLISHED);
        setId(testMenu, 1L);

        testOrder = new Order();
        testOrder.setOrderCode("TEST1234");
        testOrder.setClient(testUser);
        testOrder.setWeeklyMenu(testMenu);
        testOrder.setStatus(Order.OrderStatus.PENDING_PAYMENT);
        testOrder.setTotalAmount(new BigDecimal("210.00"));
        testOrder.setPickupDateTime(LocalDateTime.now().plusDays(7));
        setId(testOrder, 1L);

        testTransaction = new Transaction();
        testTransaction.setOrder(testOrder);
        testTransaction.setTransactionReference("stripe_test123");
        testTransaction.setPaymentMethod(Transaction.PaymentMethod.CREDIT_CARD);
        testTransaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        testTransaction.setAmount(new BigDecimal("210.00"));
        testTransaction.setCurrency("CRC");
        setId(testTransaction, 1L);
    }

    @Test
    void processCreditCardPayment_Success() {
        CreditCardPaymentRequest request = CreditCardPaymentRequest.builder()
                .orderId(1L)
                .cardNumber("4242424242424242")
                .cardHolderName("Test User")
                .expiryMonth("12")
                .expiryYear("2025")
                .cvv("123")
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(transactionRepository.findFirstByOrderIdAndStatusOrderByCreatedAtDesc(
                1L, Transaction.TransactionStatus.COMPLETED)).thenReturn(Optional.empty());
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        TransactionResponse response = paymentService.processCreditCardPayment(request);

        assertNotNull(response);
        verify(transactionRepository).save(any(Transaction.class));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void processCreditCardPayment_OrderNotFound() {
        CreditCardPaymentRequest request = CreditCardPaymentRequest.builder()
                .orderId(999L)
                .cardNumber("4242424242424242")
                .cardHolderName("Test User")
                .expiryMonth("12")
                .expiryYear("2025")
                .cvv("123")
                .build();

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
            () -> paymentService.processCreditCardPayment(request));
    }

    @Test
    void processCreditCardPayment_OrderNotPending() {
        testOrder.setStatus(Order.OrderStatus.PAID);
        CreditCardPaymentRequest request = CreditCardPaymentRequest.builder()
                .orderId(1L)
                .cardNumber("4242424242424242")
                .cardHolderName("Test User")
                .expiryMonth("12")
                .expiryYear("2025")
                .cvv("123")
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        assertThrows(BadRequestException.class, 
            () -> paymentService.processCreditCardPayment(request));
    }

    @Test
    void submitManualPayment_Success() {
        ManualPaymentRequest request = ManualPaymentRequest.builder()
                .orderId(1L)
                .paymentMethod("BANK_TRANSFER")
                .transactionReference("BANK123456")
                .proofOfPaymentUrl("https://example.com/proof.jpg")
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        TransactionResponse response = paymentService.submitManualPayment(request);

        assertNotNull(response);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void verifyManualPayment_Approved() {
        testTransaction.setStatus(Transaction.TransactionStatus.PENDING);
        
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(testTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        TransactionResponse response = paymentService.verifyManualPayment(1L, true);

        assertNotNull(response);
        verify(transactionRepository).save(any(Transaction.class));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void verifyManualPayment_Rejected() {
        testTransaction.setStatus(Transaction.TransactionStatus.PENDING);
        
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(testTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        TransactionResponse response = paymentService.verifyManualPayment(1L, false);

        assertNotNull(response);
        verify(transactionRepository).save(any(Transaction.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getOrderTransactions_Success() {
        when(transactionRepository.findByOrderIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(testTransaction));

        List<TransactionResponse> transactions = paymentService.getOrderTransactions(1L);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
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
