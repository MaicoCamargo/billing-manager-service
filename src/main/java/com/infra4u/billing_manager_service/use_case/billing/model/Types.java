package com.infra4u.billing_manager_service.use_case.billing.model;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Types {
    CASH(1),
    CARD(2),
    BANK_SLIP(3);

    private final Long id;

    Types(long id) {
        this.id = id;
    }

    public static Types getById(final Long id) {

        return Stream.of(values())
                .filter(type -> type.id.equals(id))
                .findFirst()
                .orElse(Types.CASH);
    }
}
