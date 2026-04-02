package com.infra4u.billing_manager_service.use_case.billing.model.request;

import com.infra4u.billing_manager_service.use_case.billing.model.Types;

import java.math.BigDecimal;

public record PaidBilling(
        String name,
        String description,
        Types type,
        BigDecimal amount,
        String order,
        Long user,
        Long service) {
}
