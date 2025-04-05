package com.infra4u.billing_manager_service.use_case.payment.mapper;

import com.infra4u.billing_manager_service.entity.*;
import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.payment.PaymentMapperStrategy;
import com.infra4u.billing_manager_service.use_case.payment.model.request.CreatePayment;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class CreatePaymentMapperStrategy implements PaymentMapperStrategy<Payment, CreatePayment> {


    @Override
    public Payment map(CreatePayment data) {
        Types type = Types.getById(data.type().getId());
        final PaymentType paymentType = new PaymentType(type.name(), type.getId());

        Statuses status = Statuses.getById(data.status().getId());
        final PaymentStatus paymentStatus = new PaymentStatus(new Status(status.getId(), status.name()));

        final Payment payment = new Payment(data.value(), 1, paymentType, data.expirationDate(), Collections.singletonList(paymentStatus));

        final Billing billing = new Billing();
        billing.setId(data.billing());
        payment.setBilling(billing);

        return payment;
    }

    @Override
    public Collection<Payment> map(Collection<CreatePayment> data) {
        return data.stream().map(this::map).collect(Collectors.toList());
    }
}
