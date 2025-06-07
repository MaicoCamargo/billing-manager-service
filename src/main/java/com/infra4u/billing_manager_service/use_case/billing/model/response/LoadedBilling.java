package com.infra4u.billing_manager_service.use_case.billing.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

public record LoadedBilling (Long id, String name, String description, BigDecimal amount, Long order, LocalDateTime createdAt, Long user, Collection<LoadedPayment> payments) {
}
