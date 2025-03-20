package com.infra4u.billing_manager_service.use_case.billing.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoadedPayment(
        Long id,
        BigDecimal value,
        Integer installment,
        LoadedType type,
        LocalDateTime expirationDate,
        LoadedStatus status) {
}
