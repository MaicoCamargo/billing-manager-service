package com.infra4u.billing_manager_service.use_case.billing.mapper;

import com.infra4u.billing_manager_service.entity.*;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedPaymentSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadedBillingMapperStrategyTest {

    private final LoadedBillingMapperStrategy mapper = new LoadedBillingMapperStrategy();

    @Test
    @DisplayName("Verify grouping by payment type in LoadedBillingMapperStrategy")
    void shouldGroupByPaymentType() {
        // Arrange
        PaymentType cardType = new PaymentType("CARD", 1L);
        PaymentType cashType = new PaymentType("CASH", 2L);

        Payment p1 = new Payment(BigDecimal.valueOf(100), 1, cardType, LocalDateTime.now());
        Payment p2 = new Payment(BigDecimal.valueOf(50), 1, cardType, LocalDateTime.now());
        Payment p3 = new Payment(BigDecimal.valueOf(30), 1, cashType, LocalDateTime.now());

        Billing billing = new Billing();
        billing.setId(1L);
        billing.setAmount(BigDecimal.valueOf(180));
        billing.setPayments(Arrays.asList(p1, p2, p3));

        // Act
        LoadedBilling result = mapper.map(billing);

        // Assert
        assertEquals(2, result.payments().size());
        
        LoadedPaymentSummary cardGroup = result.payments().stream()
                .filter(p -> p.type().id().equals(1L))
                .findFirst()
                .orElseThrow();
        assertEquals(new BigDecimal("150"), cardGroup.value());

        LoadedPaymentSummary cashGroup = result.payments().stream()
                .filter(p -> p.type().id().equals(2L))
                .findFirst()
                .orElseThrow();
        assertEquals(new BigDecimal("30"), cashGroup.value());
    }
}
