package com.infra4u.billing_manager_service.service;

import com.infra4u.billing_manager_service.config.multi_tenant.TenantContext;
import com.infra4u.billing_manager_service.config.multi_tenant.TenantPropertySource;
import com.infra4u.billing_manager_service.entity.Payment;
import com.infra4u.billing_manager_service.entity.PaymentStatus;
import com.infra4u.billing_manager_service.use_case.payment.PaymentRepository;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentStatusScheduler Tests")
class PaymentStatusSchedulerTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private TenantPropertySource tenantPropertySource;

    @InjectMocks
    private PaymentStatusScheduler scheduler;

    @Test
    @DisplayName("Should update pending payments for all tenants")
    void updatePendingPayments_ShouldUpdateStatusAndSave() {
        // Arrange
        // Arrange
        String tenant1 = "tenant1";
        Map<String, Properties> tenantProps = new HashMap<>();
        tenantProps.put(tenant1, new Properties());

        when(tenantPropertySource.loadTenantProperties()).thenReturn(tenantProps);

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setPaymentStatuses(new ArrayList<>());

        // Ensure the list is mutable
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);

        when(paymentRepository.findPendingCreditCardPayments(any(LocalDateTime.class))).thenReturn(payments);

        try (MockedStatic<TenantContext> mockedTenantContext = mockStatic(TenantContext.class)) {
            // Act
            scheduler.updatePendingPayments();

            // Assert
            mockedTenantContext.verify(() -> TenantContext.setCurrentTenant(tenant1));
            mockedTenantContext.verify(() -> TenantContext.setCurrentTenant(null));

            verify(paymentRepository, times(1)).saveAll(payments);

            assertEquals(1, payment.getPaymentStatuses().size());
            PaymentStatus addedStatus = payment.getPaymentStatuses().get(0);
            assertEquals(Statuses.COMPLETED.getId(), addedStatus.getStatus().getId());
            assertEquals(payment, addedStatus.getPayment());
        }
    }

    @Test
    @DisplayName("Should handle multiple tenants")
    void updatePendingPayments_ShouldIterateOverAllTenants() {
        // Arrange
        String tenant1 = "tenant1";
        String tenant2 = "tenant2";
        Map<String, Properties> tenantProps = new LinkedHashMap<>(); // Use LinkedHashMap for predictable iteration
                                                                     // order if
        // needed, but here verification is enough
        tenantProps.put(tenant1, new Properties());
        tenantProps.put(tenant2, new Properties());

        when(tenantPropertySource.loadTenantProperties()).thenReturn(tenantProps);
        when(paymentRepository.findPendingCreditCardPayments(any(LocalDateTime.class))).thenReturn(new ArrayList<>());

        try (MockedStatic<TenantContext> mockedTenantContext = mockStatic(TenantContext.class)) {
            // Act
            scheduler.updatePendingPayments();

            // Assert
            mockedTenantContext.verify(() -> TenantContext.setCurrentTenant(tenant1));
            mockedTenantContext.verify(() -> TenantContext.setCurrentTenant(tenant2));
            // Verify it resets to null after each or finally. The implementation does it in
            // each iteration.
            mockedTenantContext.verify(() -> TenantContext.setCurrentTenant(null), times(2));

            verify(paymentRepository, times(2)).findPendingCreditCardPayments(any(LocalDateTime.class));
        }
    }
}
