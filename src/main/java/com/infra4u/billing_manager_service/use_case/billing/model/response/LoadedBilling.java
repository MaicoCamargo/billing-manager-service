package com.infra4u.billing_manager_service.use_case.billing.model.response;

import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

public record LoadedBilling (String name, String description, BigDecimal amount, String order, Collection<LoadedPaymentSummary> payments, Statuses status, String code) {
    public LoadedBilling(
            String name,
            String description,
            BigDecimal amount,
            String order,
            Collection<LoadedPaymentSummary> payments,
            String code
    ) {
        this(name, description, amount, order, payments, calculateStatus(payments, amount), code);
    }
    private static Statuses calculateStatus(Collection<LoadedPaymentSummary> payments, BigDecimal amount) {
        if (Objects.isNull(payments) || payments.isEmpty()) {
            return Statuses.PENDING;
        }
        final BigDecimal totalPayments = payments.stream()
                .map(LoadedPaymentSummary::value) // Obtém o valor de cada pagamento
                .reduce(BigDecimal.ZERO, BigDecimal::add);// Soma todos os valores
        if  (totalPayments.compareTo(amount) < 0) {
            return Statuses.PENDING;
        }

        boolean allPaid = payments.stream().allMatch(p -> p.status().id().equals(Statuses.COMPLETED.getId()));
        boolean allCanceled = payments.stream().allMatch(p -> p.status().id().equals(Statuses.CANCELED.getId()));
        boolean anyOverdue = payments.stream().anyMatch(p -> p.status().id().equals(Statuses.LATE.getId()));

        if (allPaid)
            return Statuses.COMPLETED;
        if (allCanceled)
            return Statuses.CANCELED;
        if (anyOverdue)
            return Statuses.LATE;
        return Statuses.IN_PROGRESS;
    }
}
