package com.infra4u.billing_manager_service.use_case.billing;

import java.math.BigDecimal;

public record CreateBilling(
        String name,
        String description,
        Types type,
        BigDecimal amount,
        Long order,
        Integer installmentCount,
        Integer user
) {
}
