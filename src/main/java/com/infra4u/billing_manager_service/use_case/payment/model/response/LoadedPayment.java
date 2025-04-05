package com.infra4u.billing_manager_service.use_case.payment.model.response;

import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoadedPayment(
        @NotNull @NotBlank Long id,
        @NotNull @NotBlank LoadedStatus status,
        @NotNull @NotBlank BigDecimal value,
        @NotNull @NotBlank Types type,
        Integer installment,
        @NotNull @NotBlank LocalDateTime createdAt,
        @NotNull @NotBlank LocalDateTime expirationDate) {
}
