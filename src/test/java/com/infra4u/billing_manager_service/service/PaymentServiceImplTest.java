package com.infra4u.billing_manager_service.service;

import com.infra4u.billing_manager_service.entity.Billing;
import com.infra4u.billing_manager_service.entity.Payment;
import com.infra4u.billing_manager_service.use_case.billing.BillingRepository;
import com.infra4u.billing_manager_service.use_case.payment.PaymentRepository;
import com.infra4u.billing_manager_service.use_case.payment.mapper.CreatePaymentMapperStrategy;
import com.infra4u.billing_manager_service.use_case.payment.mapper.LoadedPaymentMapperStrategy;
import com.infra4u.billing_manager_service.use_case.payment.mapper.UpdatePaymentMapperStrategy;
import com.infra4u.billing_manager_service.use_case.payment.model.request.CreatePayment;
import com.infra4u.billing_manager_service.use_case.payment.model.request.UpdatePayment;
import com.infra4u.billing_manager_service.use_case.payment.model.response.LoadedPayment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentServiceImpl Tests")
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private UpdatePaymentMapperStrategy updatePaymentMapperStrategy;
    @Mock
    private BillingRepository billingRepository;
    @Mock
    private CreatePaymentMapperStrategy createPaymentMapperStrategy;
    @Mock
    private LoadedPaymentMapperStrategy loadedPaymentMapperStrategy;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("Should create payment successfully")
    void create_ShouldCreatePayment_WhenValid() {
        // Arrange
        Long billingId = 1L;
        BigDecimal billingAmount = new BigDecimal("100.00");
        BigDecimal paymentValue = new BigDecimal("50.00");
        // CreatePayment(billing, status, value, type, expirationDate)
        CreatePayment createPayment = new CreatePayment(billingId, null, paymentValue, null, null);

        Billing billing = new Billing();
        billing.setId(billingId);
        billing.setAmount(billingAmount);
        billing.setPayments(new ArrayList<>());

        when(billingRepository.findById(billingId)).thenReturn(Optional.of(billing));

        Payment payment = new Payment();
        when(createPaymentMapperStrategy.map(createPayment)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);

        // LoadedPayment: id, status, value, type, installment, createdAt,
        // expirationDate
        LoadedPayment loadedPayment = new LoadedPayment(1L, null, null, null, null, null, null);
        when(loadedPaymentMapperStrategy.map(payment)).thenReturn(loadedPayment);

        // Act
        LoadedPayment result = paymentService.create(createPayment);

        // Assert
        assertNotNull(result);
        verify(paymentRepository).save(payment);
    }

    @Test
    @DisplayName("Should throw exception when billing not found")
    void create_ShouldThrowException_WhenBillingNotFound() {
        // Arrange
        Long billingId = 99L;
        CreatePayment createPayment = new CreatePayment(billingId, null, BigDecimal.TEN, null, null);
        when(billingRepository.findById(billingId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentService.create(createPayment));
        assertEquals("Billing not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when total payment amount exceeds billing amount (initial payment)")
    void create_ShouldThrowException_WhenExceedsBillingAmount_Initial() {
        // Arrange
        Long billingId = 1L;
        BigDecimal billingAmount = new BigDecimal("100.00");
        BigDecimal paymentValue = new BigDecimal("101.00");
        CreatePayment createPayment = new CreatePayment(billingId, null, paymentValue, null, null);

        Billing billing = new Billing();
        billing.setId(billingId);
        billing.setAmount(billingAmount);
        billing.setPayments(new ArrayList<>());

        when(billingRepository.findById(billingId)).thenReturn(Optional.of(billing));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentService.create(createPayment));
        assertEquals("Total amount exceeds billing amount", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when total payment amount exceeds billing amount (subsequent payment)")
    void create_ShouldThrowException_WhenExceedsBillingAmount_Subsequent() {
        // Arrange
        Long billingId = 1L;
        BigDecimal billingAmount = new BigDecimal("100.00");
        BigDecimal existingPaymentValue = new BigDecimal("60.00");
        BigDecimal newPaymentValue = new BigDecimal("50.00"); // 60 + 50 = 110 > 100

        CreatePayment createPayment = new CreatePayment(billingId, null, newPaymentValue, null, null);

        Billing billing = new Billing();
        billing.setId(billingId);
        billing.setAmount(billingAmount);

        Payment existingPayment = new Payment();
        existingPayment.setValue(existingPaymentValue);
        billing.setPayments(List.of(existingPayment));

        when(billingRepository.findById(billingId)).thenReturn(Optional.of(billing));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentService.create(createPayment));
        assertEquals("Total amount exceeds billing amount", exception.getMessage());
    }

    @Test
    @DisplayName("Should update payment successfully")
    void update_ShouldUpdatePayment() {
        // Arrange
        // UpdatePayment: id, status, value, type
        UpdatePayment updatePayment = new UpdatePayment(1L, null, null, null);
        Payment payment = new Payment();

        when(updatePaymentMapperStrategy.map(updatePayment)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(loadedPaymentMapperStrategy.map(payment)).thenReturn(null);

        // Act
        paymentService.update(updatePayment);

        // Assert
        verify(paymentRepository).save(payment);
    }
}
