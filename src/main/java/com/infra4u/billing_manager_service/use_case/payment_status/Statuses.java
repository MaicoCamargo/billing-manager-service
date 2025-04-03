package com.infra4u.billing_manager_service.use_case.payment_status;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Statuses {
    PENDING(1L),      // Pagamento pendente
    COMPLETED(2L),    // Pagamento concluído
    FAILED(3L),       // Pagamento falhou
    LATE(4L),         // Pagamento atrasado
    CANCELED(5L),     // Pagamento cancelado
    REFUND(6L),       // Pagamento estornado
    IN_PROGRESS(7L),  // Pagamento em andamento
    ;

    private final Long id;

    Statuses(Long id) {
        this.id = id;
    }

    public static Statuses getById(final Long id) {

        return Stream.of(values())
                .filter(status -> status.id.equals(id))
                .findFirst()
                .orElse(Statuses.PENDING);
    }
}