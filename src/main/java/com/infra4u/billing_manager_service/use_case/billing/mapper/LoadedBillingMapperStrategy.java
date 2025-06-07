package com.infra4u.billing_manager_service.use_case.billing.mapper;

import com.infra4u.billing_manager_service.entity.Billing;
import com.infra4u.billing_manager_service.use_case.billing.BillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedPayment;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedStatus;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoadedBillingMapperStrategy implements BillingMapperStrategy<LoadedBilling, Billing> {

    @Override
    public LoadedBilling map(Billing billing) {
        List<LoadedPayment> payments = billing.getPayments()
                .stream()
                .map(payment -> new LoadedPayment(payment.getId(), payment.getValue(), payment.getInstallment(), new LoadedType(payment.getPaymentType().getName(), payment.getPaymentType().getId()), payment.getExpirationDate(), LoadedStatus.of(payment.getPaymentStatuses())))
                .toList();
        return new LoadedBilling(billing.getId(), billing.getName(), billing.getDescription(), billing.getAmount(), billing.getOrderId(), billing.getCreatedAt(), billing.getUserId(), payments);
    }

    @Override
    public Collection<LoadedBilling> map(Collection<Billing> billings) {
        return billings.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
