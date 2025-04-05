package com.infra4u.billing_manager_service.use_case.payment.mapper;

import com.infra4u.billing_manager_service.entity.Payment;
import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedStatus;
import com.infra4u.billing_manager_service.use_case.payment.PaymentMapperStrategy;
import com.infra4u.billing_manager_service.use_case.payment.model.response.LoadedPayment;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class LoadedPaymentMapperStrategy implements PaymentMapperStrategy<LoadedPayment, Payment> {

    @Override
    public LoadedPayment map(Payment data) {
        return new LoadedPayment(
                data.getId(),
                LoadedStatus.of(data.getPaymentStatuses()),
                data.getValue(),
                Types.getById(data.getPaymentType().getId()),
                data.getInstallment(),
                data.getCreatedAt(),
                data.getExpirationDate()
        );
    }

    @Override
    public Collection<LoadedPayment> map(Collection<Payment> data) {
        return data.stream().map(this::map).collect(Collectors.toList());
    }
}
