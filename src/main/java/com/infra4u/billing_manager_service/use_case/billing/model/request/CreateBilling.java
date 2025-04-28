package com.infra4u.billing_manager_service.use_case.billing.model.request;

import java.math.BigDecimal;
import java.util.List;

public record CreateBilling(
        String name,
        String description,
        BigDecimal amount,
        Long order,
        Long user,
        Long service,
        List<FuturePayment> futurePayments
) {
}