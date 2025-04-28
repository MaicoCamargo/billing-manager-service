package com.infra4u.billing_manager_service.use_case.billing.model;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Types {
    CASH_ON_DELIVERY(1), // Dinheiro á vista
    DEBIT_CARD(2),
    CREDIT_CARD(3),
    BANK_SLIP(4), // boleto
    INSTALLMENT_CASH(5); // Dinheiro parcelado

    private final Long id;

    Types(long id) {
        this.id = id;
    }

    public static Types getById(final Long id) {

        return Stream.of(values())
                .filter(type -> type.id.equals(id))
                .findFirst()
                .orElse(Types.CASH_ON_DELIVERY);
    }
}
