package com.infra4u.billing_manager_service.use_case.billing;

import lombok.Getter;

@Getter
public enum Types {
    CASH(1),
    CARD(2),
    BANK_SLIP(3);

    private final Long type;

    Types(long type) {
        this.type = type;
    }
}
