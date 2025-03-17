package com.infra4u.billing_manager_service.use_case.billing.model.response;

import com.infra4u.billing_manager_service.entity.Payment;

import java.math.BigDecimal;
import java.util.Collection;

//@todo remover Collection<Payment> e criar um record para listar os pagamentos
public record LoadedBilling (Long id, String name, String description, BigDecimal amount, Collection<Payment> payments) {
}
