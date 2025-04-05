package com.infra4u.billing_manager_service.use_case.payment.model.request;

import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreatePayment(
        @NotNull Long billing,
        @NotNull Statuses status,
        @NotNull BigDecimal value,
        @NotNull Types type,
        LocalDateTime expirationDate) {
}
