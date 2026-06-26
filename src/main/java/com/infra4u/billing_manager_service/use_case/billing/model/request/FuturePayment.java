package com.infra4u.billing_manager_service.use_case.billing.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FuturePayment(BigDecimal value, Integer installments, @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDate expiresAt, Statuses status, Types type) {
    public FuturePayment(BigDecimal value, Integer installments, LocalDate expiresAt, Types type) {
        this(value, installments, expiresAt, Statuses.PENDING, type);
    }
}
