package com.infra4u.billing_manager_service.use_case.billing.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.infra4u.billing_manager_service.entity.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoadedStatus(String name, Long id, LocalDateTime date) {

    /**
     * return most recently payment status
     * @param paymentStatus all payment status
     * @return current status
     */
    public static LoadedStatus of(List<PaymentStatus> paymentStatus) {

        if (paymentStatus.isEmpty()) {
            //@fixme lancar erro, por que pagamento deve ter status sempre !
            return null;
        }
        paymentStatus = paymentStatus.stream().sorted(Comparator.comparing(PaymentStatus::getCreatedAt)).toList();
        PaymentStatus current = paymentStatus.getLast();
        return new LoadedStatus(current.getStatus().getName(), current.getStatus().getId(), current.getCreatedAt());

    }
}
