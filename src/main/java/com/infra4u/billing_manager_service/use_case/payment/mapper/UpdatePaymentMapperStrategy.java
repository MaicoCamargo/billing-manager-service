package com.infra4u.billing_manager_service.use_case.payment.mapper;

import com.infra4u.billing_manager_service.entity.Payment;
import com.infra4u.billing_manager_service.entity.PaymentStatus;
import com.infra4u.billing_manager_service.entity.PaymentType;
import com.infra4u.billing_manager_service.entity.Status;
import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.payment.PaymentMapperStrategy;
import com.infra4u.billing_manager_service.use_case.payment.PaymentRepository;
import com.infra4u.billing_manager_service.use_case.payment.model.request.UpdatePayment;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UpdatePaymentMapperStrategy implements PaymentMapperStrategy<Payment, UpdatePayment> {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment map(UpdatePayment data) {
        final Payment payment = paymentRepository.findById(data.id())
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        Statuses status = Statuses.getById(data.status().getId());
        PaymentStatus paymentStatus = new PaymentStatus(new Status(status.getId(), status.name()), payment);
        payment.getPaymentStatuses().add(paymentStatus);
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
