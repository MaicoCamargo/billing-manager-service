package com.infra4u.billing_manager_service.use_case.payment.mapper;

import com.infra4u.billing_manager_service.entity.Payment;
import com.infra4u.billing_manager_service.entity.PaymentStatus;
import com.infra4u.billing_manager_service.entity.PaymentType;
import com.infra4u.billing_manager_service.entity.Status;
import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.payment.PaymentMapperStrategy;
import com.infra4u.billing_manager_service.use_case.payment.model.request.UpdatePayment;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UpdatePaymentMapperStrategy implements PaymentMapperStrategy<Payment, UpdatePayment> {

    @Override
    public Payment map(UpdatePayment data) {
        final Payment payment = new Payment();
        payment.setId(data.id());
        Statuses status = Statuses.getById(data.status().getId());
        PaymentStatus paymentStatus = new PaymentStatus(new Status(status.getId(), status.name()));
        payment.setPaymentStatuses(Collections.singletonList(paymentStatus));
        if (Objects.nonNull(data.value())) {
            payment.setValue(data.value());
        }
        if (Objects.nonNull(data.type())) {
            Types type = Types.getById(data.type().getId());
            final PaymentType paymentType = new PaymentType(type.name(), type.getId());
            payment.setPaymentType(paymentType);
        }
        return payment;
    }

    @Override
    public Collection<Payment> map(Collection<UpdatePayment> data) {
        return data.stream().map(this::map).collect(Collectors.toList());
    }
}
