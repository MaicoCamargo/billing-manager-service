package com.infra4u.billing_manager_service.use_case.billing.model.request;

import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FuturePayment(BigDecimal value, LocalDateTime expiresAt, Statuses status, Types type) {
    public FuturePayment(BigDecimal value, LocalDateTime expiresAt,Types type) {
        this(value, expiresAt, Statuses.PENDING, type);
    }
}
