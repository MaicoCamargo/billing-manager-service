package com.infra4u.billing_manager_service.service;

import com.infra4u.billing_manager_service.entity.Billing;
import com.infra4u.billing_manager_service.entity.Payment;
import com.infra4u.billing_manager_service.entity.PaymentStatus;
import com.infra4u.billing_manager_service.entity.Status;
import com.infra4u.billing_manager_service.exception.NotFoundException;
import com.infra4u.billing_manager_service.use_case.billing.BillingRepository;
import com.infra4u.billing_manager_service.use_case.billing.mapper.*;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BillingServiceImpl.cancel() method
 * Following best practices:
 * - Descriptive test names using @DisplayName
 * - AAA pattern (Arrange, Act, Assert)
 * - Proper mocking and verification
 * - Edge case coverage
 * - Clear assertions with meaningful messages
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BillingServiceImpl - Cancel Method Tests")
class BillingServiceImplTest {

    @Mock
    private BillingRepository repository;

    @Mock
    private LoadedBillingMapperStrategy loadedBillingMapperStrategy;

    @Mock
    private PaidBillingMapperStrategy paidBillingMapperStrategy;

    @Mock
    private CreateBillingMapperStrategy createBillingMapperStrategy;

    @Mock
    private SimpleBillingMapperStrategy simpleBillingMapperStrategy;

    @Mock
    private UpdateBillingMapperStrategy updateBillingMapperStrategy;

    @InjectMocks
    private BillingServiceImpl billingService;

    private Billing testBilling;
    private static final String BILLING_CODE = "ANY_CODE";
    private static final String NON_EXISTENT_ID = "NON_EXISTENT_ID";

    @BeforeEach
    void setUp() {
        // Initialize test data
        testBilling = new Billing();
        testBilling.setCode(BILLING_CODE);
        testBilling.setName("Test Billing");
        testBilling.setAmount(BigDecimal.valueOf(1000.00));
        testBilling.setUserId(1L);
    }

    @Test
    @DisplayName("Should successfully cancel billing with payments and add CANCELED status to each payment")
    void cancel_WithPayments_ShouldAddCanceledStatusToAllPayments() {
        // Arrange
        List<Payment> payments = createTestPaymentsWithStatuses(3);
        testBilling.setPayments(payments);

        when(repository.findByCode(BILLING_CODE)).thenReturn(Optional.of(testBilling));
        when(repository.save(any(Billing.class))).thenReturn(testBilling);

        // Act
        Void result = billingService.cancel(BILLING_CODE);

        // Assert
        assertNull(result, "Cancel method should return null");

        // Verify repository interactions
        verify(repository, times(1)).findByCode(BILLING_CODE);
        verify(repository, times(1)).save(testBilling);

        // Verify each payment has a new CANCELED status added
        for (Payment payment : payments) {
            List<PaymentStatus> statuses = payment.getPaymentStatuses();
            assertTrue(statuses.size() > 1, "Payment should have at least 2 statuses (original + canceled)");

            // Verify the last status added is CANCELED
            PaymentStatus lastStatus = statuses.get(statuses.size() - 1);
            assertNotNull(lastStatus, "Last payment status should not be null");
            assertNotNull(lastStatus.getStatus(), "Status object should not be null");
            assertEquals(Statuses.CANCELED.getId(), lastStatus.getStatus().getId(),
                    "Last status should be CANCELED");
            assertEquals(Statuses.CANCELED.name(), lastStatus.getStatus().getName(),
                    "Status name should be CANCELED");
        }
    }

    @Test
    @DisplayName("Should successfully cancel billing with empty payments list")
    void cancel_WithEmptyPaymentsList_ShouldNotThrowException() {
        // Arrange
        testBilling.setPayments(new ArrayList<>());

        when(repository.findByCode(BILLING_CODE)).thenReturn(Optional.of(testBilling));
        when(repository.save(any(Billing.class))).thenReturn(testBilling);

        // Act & Assert
        assertDoesNotThrow(() -> billingService.cancel(BILLING_CODE),
                "Cancel should not throw exception with empty payments list");

        verify(repository, times(1)).findByCode(BILLING_CODE);
        verify(repository, times(1)).save(testBilling);
    }

    @Test
    @DisplayName("Should successfully cancel billing with null payments")
    void cancel_WithNullPayments_ShouldNotThrowException() {
        // Arrange
        testBilling.setPayments(null);

        when(repository.findByCode(BILLING_CODE)).thenReturn(Optional.of(testBilling));
        when(repository.save(any(Billing.class))).thenReturn(testBilling);

        // Act & Assert
        assertDoesNotThrow(() -> billingService.cancel(BILLING_CODE),
                "Cancel should not throw exception with null payments");

        verify(repository, times(1)).findByCode(BILLING_CODE);
        verify(repository, times(1)).save(testBilling);
    }

    @Test
    @DisplayName("Should throw NotFoundException when billing does not exist")
    void cancel_WithNonExistentBilling_ShouldThrowNotFoundException() {
        // Arrange
        when(repository.findByCode(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> billingService.cancel(NON_EXISTENT_ID),
                "Should throw NotFoundException when billing is not found");

        assertEquals("Billing with code " + NON_EXISTENT_ID + " not found",
                exception.getMessage(),
                "Exception message should contain the billing ID");

        verify(repository, times(1)).findByCode(NON_EXISTENT_ID);
        verify(repository, never()).save(any(Billing.class));
    }

    @Test
    @DisplayName("Should preserve existing payment statuses when adding CANCELED status")
    void cancel_ShouldPreserveExistingPaymentStatuses() {
        // Arrange
        Payment payment = createPaymentWithMultipleStatuses();
        testBilling.setPayments(Collections.singletonList(payment));

        int originalStatusCount = payment.getPaymentStatuses().size();

        when(repository.findByCode(BILLING_CODE)).thenReturn(Optional.of(testBilling));
        when(repository.save(any(Billing.class))).thenReturn(testBilling);

        // Act
        billingService.cancel(BILLING_CODE);

        // Assert
        assertEquals(originalStatusCount + 1, payment.getPaymentStatuses().size(),
                "Should have one additional status after cancellation");

        // Verify CANCELED status is the last one
        PaymentStatus canceledStatus = payment.getPaymentStatuses().get(payment.getPaymentStatuses().size() - 1);
        assertEquals(Statuses.CANCELED.getId(), canceledStatus.getStatus().getId(),
                "Last status should be CANCELED");
    }

    @Test
    @DisplayName("Should save billing exactly once after cancellation")
    void cancel_ShouldSaveBillingOnce() {
        // Arrange
        List<Payment> payments = createTestPaymentsWithStatuses(2);
        testBilling.setPayments(payments);

        ArgumentCaptor<Billing> billingCaptor = ArgumentCaptor.forClass(Billing.class);

        when(repository.findByCode(BILLING_CODE)).thenReturn(Optional.of(testBilling));
        when(repository.save(any(Billing.class))).thenReturn(testBilling);

        // Act
        billingService.cancel(BILLING_CODE);

        // Assert
        verify(repository, times(1)).save(billingCaptor.capture());

        Billing savedBilling = billingCaptor.getValue();
        assertEquals(testBilling, savedBilling, "Saved billing should be the same instance");
        assertEquals(BILLING_CODE, savedBilling.getCode(), "Billing ID should be preserved");
    }

    @Test
    @DisplayName("Should handle single payment cancellation correctly")
    void cancel_WithSinglePayment_ShouldAddCanceledStatus() {
        // Arrange
        Payment payment = createTestPayment(1, BigDecimal.valueOf(1000.00));
        testBilling.setPayments(Collections.singletonList(payment));

        when(repository.findByCode(BILLING_CODE)).thenReturn(Optional.of(testBilling));
        when(repository.save(any(Billing.class))).thenReturn(testBilling);

        // Act
        billingService.cancel(BILLING_CODE);

        // Assert
        List<PaymentStatus> statuses = payment.getPaymentStatuses();
        assertEquals(2, statuses.size(), "Payment should have 2 statuses (PENDING + CANCELED)");

        PaymentStatus canceledStatus = statuses.get(1);
        assertEquals(Statuses.CANCELED.getId(), canceledStatus.getStatus().getId());
        assertEquals(Statuses.CANCELED.name(), canceledStatus.getStatus().getName());
    }

    @Test
    @DisplayName("Should handle multiple payments with different initial statuses")
    void cancel_WithMultiplePaymentsDifferentStatuses_ShouldCancelAll() {
        // Arrange
        Payment pendingPayment = createPaymentWithStatus(Statuses.PENDING);
        Payment completedPayment = createPaymentWithStatus(Statuses.COMPLETED);
        Payment inProgressPayment = createPaymentWithStatus(Statuses.IN_PROGRESS);

        List<Payment> payments = List.of(pendingPayment, completedPayment, inProgressPayment);
        testBilling.setPayments(payments);

        when(repository.findByCode(BILLING_CODE)).thenReturn(Optional.of(testBilling));
        when(repository.save(any(Billing.class))).thenReturn(testBilling);

        // Act
        billingService.cancel(BILLING_CODE);

        // Assert
        for (Payment payment : payments) {
            PaymentStatus lastStatus = payment.getPaymentStatuses().get(
                    payment.getPaymentStatuses().size() - 1);
            assertEquals(Statuses.CANCELED.getId(), lastStatus.getStatus().getId(),
                    "All payments should have CANCELED as their last status");
        }

        verify(repository, times(1)).save(testBilling);
    }

    // Helper methods for test data creation

    /**
     * Creates a list of test payments with initial PENDING status
     */
    private List<Payment> createTestPaymentsWithStatuses(int count) {
        List<Payment> payments = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            payments.add(createTestPayment(i, BigDecimal.valueOf(100.00 * i)));
        }
        return payments;
    }

    /**
     * Creates a single test payment with PENDING status
     */
    private Payment createTestPayment(int installment, BigDecimal value) {
        Payment payment = new Payment();
        payment.setId((long) installment);
        payment.setValue(value);

        PaymentStatus pendingStatus = new PaymentStatus();
        Status status = new Status(Statuses.PENDING.getId(), Statuses.PENDING.name());
        pendingStatus.setStatus(status);
        pendingStatus.setPayment(payment);

        List<PaymentStatus> statuses = new ArrayList<>();
        statuses.add(pendingStatus);
        payment.setPaymentStatuses(statuses);

        return payment;
    }

    /**
     * Creates a payment with multiple existing statuses
     */
    private Payment createPaymentWithMultipleStatuses() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setValue(BigDecimal.valueOf(500.00));

        List<PaymentStatus> statuses = new ArrayList<>();

        // Add PENDING status
        PaymentStatus pendingStatus = new PaymentStatus();
        pendingStatus.setStatus(new Status(Statuses.PENDING.getId(), Statuses.PENDING.name()));
        pendingStatus.setPayment(payment);
        statuses.add(pendingStatus);

        // Add IN_PROGRESS status
        PaymentStatus inProgressStatus = new PaymentStatus();
        inProgressStatus.setStatus(new Status(Statuses.IN_PROGRESS.getId(), Statuses.IN_PROGRESS.name()));
        inProgressStatus.setPayment(payment);
        statuses.add(inProgressStatus);

        payment.setPaymentStatuses(statuses);
        return payment;
    }

    /**
     * Creates a payment with a specific initial status
     */
    private Payment createPaymentWithStatus(Statuses statusType) {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setValue(BigDecimal.valueOf(300.00));

        PaymentStatus paymentStatus = new PaymentStatus();
        Status status = new Status(statusType.getId(), statusType.name());
        paymentStatus.setStatus(status);
        paymentStatus.setPayment(payment);

        List<PaymentStatus> statuses = new ArrayList<>();
        statuses.add(paymentStatus);
        payment.setPaymentStatuses(statuses);

        return payment;
    }
}
