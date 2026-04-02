package com.infra4u.billing_manager_service.use_case.billing.model.request;

import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record LoadBillingQueryParams(
        @Schema(defaultValue = "0") Integer page,
        @Schema(description = "Page size", defaultValue = "10") Integer size,
        @Schema(description = "Start Date") LocalDate startDate,
        @Schema(description = "End Date") LocalDate endDate,
        @Schema(description = "Status") Statuses status,
        @Schema(description = "Usuário") Long user,
        @Schema(description = "Code") String code,
        @Schema(description = "Order") String order
        ) {
}
