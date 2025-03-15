package com.infra4u.billing_manager_service.use_case.payment_status;

import lombok.Getter;

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

    private final Long status;

    Statuses(Long status) {
        this.status = status;
    }
}