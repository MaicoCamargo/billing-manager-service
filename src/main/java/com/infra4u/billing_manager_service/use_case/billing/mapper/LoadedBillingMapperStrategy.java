package com.infra4u.billing_manager_service.use_case.billing.mapper;

import com.infra4u.billing_manager_service.entity.Billing;
import com.infra4u.billing_manager_service.entity.Payment;
import com.infra4u.billing_manager_service.use_case.billing.BillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.model.response.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoadedBillingMapperStrategy implements BillingMapperStrategy<LoadedBilling, Billing> {

    @Override
    public LoadedBilling map(Billing billing) {
        List<LoadedPaymentSummary> payments = billing.getPayments().stream()
                .collect(Collectors.groupingBy(payment -> payment.getPaymentType().getId()))
                .values().stream()
                .map(paymentList -> {
                    Payment last = paymentList.getLast();
                    BigDecimal totalValue = paymentList.stream()
                            .map(Payment::getValue)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new LoadedPaymentSummary(
                            totalValue,
                            last.getInstallment(),
                            new LoadedType(last.getPaymentType().getName(), last.getPaymentType().getId()),
                            last.getExpirationDate(),
                            LoadedStatus.of(paymentList.stream()
                                    .flatMap(p -> p.getPaymentStatuses().stream())
                                    .toList())
                    );
                })
                .toList();
        return new LoadedBilling( billing.getName(), billing.getDescription(), billing.getAmount(), billing.getOrderId(), payments, billing.getCode());
    }

    @Override
    public Collection<LoadedBilling> map(Collection<Billing> billings) {
        return billings.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
