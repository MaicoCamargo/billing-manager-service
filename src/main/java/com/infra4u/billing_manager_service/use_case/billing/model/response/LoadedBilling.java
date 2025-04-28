package com.infra4u.billing_manager_service.use_case.billing.model.response;

import java.math.BigDecimal;
import java.util.Collection;

public record LoadedBilling (Long id, String name, String description, BigDecimal amount, Long order, Long service, Long user, Collection<LoadedPayment> payments) {
}
