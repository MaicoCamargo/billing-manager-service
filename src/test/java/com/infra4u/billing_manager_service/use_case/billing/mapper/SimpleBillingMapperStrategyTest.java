package com.infra4u.billing_manager_service.use_case.billing.mapper;

import com.infra4u.billing_manager_service.entity.Billing;
import com.infra4u.billing_manager_service.entity.Payment;
import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.billing.model.request.SimpleBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.SimplePayment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleBillingMapperStrategyTest {

    private final SimpleBillingMapperStrategy mapper = new SimpleBillingMapperStrategy();

    @Test
    @DisplayName("Verify installments are distributed evenly")
    void verifyEvenInstallmentDistribution() {
        // Request: 10.00 / 3 installments
        // Previous logic (RoundingMode.DOWN): 3.33, 3.33, 3.34
        // Requested logic (RoundingMode.HALF_UP): 3.33, 3.33, 3.34 (same for 10.00/3)
        
        // Let's try 10.00 / 4 installments
        // Previous logic (RoundingMode.DOWN): 2.50, 2.50, 2.50, 2.50
        
        // Let's try 10.01 / 3 installments
        // Previous logic (RoundingMode.DOWN): 3.33, 3.33, 3.35
        // Better logic: 3.34, 3.34, 3.33 (or similar)
        
        SimplePayment payment = new SimplePayment(3, new BigDecimal("10.01"), null, Types.CREDIT_CARD);
        SimpleBilling billingRequest = new SimpleBilling("Test", "Desc", new BigDecimal("10.01"), "order-1", 1L, 1L, List.of(payment));

        Billing billing = mapper.map(billingRequest);
        List<Payment> payments = billing.getPayments();

        assertEquals(3, payments.size());
        
        // With RoundingMode.HALF_UP: 10.01 / 3 = 3.34
        // Installment 1: 3.34
        // Installment 2: 3.34
        // Installment 3: 10.01 - (3.34 * 2) = 10.01 - 6.68 = 3.33
        assertEquals(new BigDecimal("3.34"), payments.get(0).getValue());
        assertEquals(new BigDecimal("3.34"), payments.get(1).getValue());
        assertEquals(new BigDecimal("3.33"), payments.get(2).getValue());
        
        BigDecimal total = payments.stream().map(Payment::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(new BigDecimal("10.01"), total);
    }

    @Test
    @DisplayName("Verify installments are distributed evenly for 10.00 / 3")
    void verifyEvenInstallmentDistributionTenDividedByThree() {
        SimplePayment payment = new SimplePayment(3, new BigDecimal("10.00"), null, Types.CREDIT_CARD);
        SimpleBilling billingRequest = new SimpleBilling("Test", "Desc", new BigDecimal("10.00"), "order-1", 1L, 1L, List.of(payment));

        Billing billing = mapper.map(billingRequest);
        List<Payment> payments = billing.getPayments();

        assertEquals(3, payments.size());
        
        // With RoundingMode.HALF_UP: 10.00 / 3 = 3.33
        // Installment 1: 3.33
        // Installment 2: 3.33
        // Installment 3: 10.00 - (3.33 * 2) = 10.00 - 6.66 = 3.34
        assertEquals(new BigDecimal("3.33"), payments.get(0).getValue());
        assertEquals(new BigDecimal("3.33"), payments.get(1).getValue());
        assertEquals(new BigDecimal("3.34"), payments.get(2).getValue());
        
        BigDecimal total = payments.stream().map(Payment::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(new BigDecimal("10.00"), total);
    }
}
