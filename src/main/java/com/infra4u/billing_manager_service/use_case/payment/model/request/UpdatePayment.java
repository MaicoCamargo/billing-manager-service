package com.infra4u.billing_manager_service.use_case.payment.model.request;

import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdatePayment(
        @NotNull @NotBlank Long id,
        @NotNull @NotBlank Statuses status,
        BigDecimal value,
        Types type) {
}
