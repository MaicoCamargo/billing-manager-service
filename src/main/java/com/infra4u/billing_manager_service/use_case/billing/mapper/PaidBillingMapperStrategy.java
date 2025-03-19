package com.infra4u.billing_manager_service.use_case.billing.mapper;

import com.infra4u.billing_manager_service.entity.*;
import com.infra4u.billing_manager_service.use_case.billing.BillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.model.request.PaidBilling;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class PaidBillingMapperStrategy implements BillingMapperStrategy<Billing, PaidBilling> {

    /**
     * create an billing paid
     * @param paid record with data to billing paid
     * @return billing entity
     */
    @Override
    public Billing map(PaidBilling paid) {
        PaymentStatus pending = new PaymentStatus(new Status(Statuses.PENDING.getStatus()));
        PaymentStatus completed = new PaymentStatus(new Status(Statuses.COMPLETED.getStatus()));
        Collection<PaymentStatus> status = new ArrayList<>();
        status.add(pending);
        status.add(completed);

        Payment payment = new Payment(paid.amount(), 1, new PaymentType(paid.type().getType()), LocalDateTime.now(), status.stream().toList());
        return new Billing(paid.name(), paid.description(), paid.amount(), 1, paid.order(), Collections.singletonList(payment));
    }

    @Override
    public Collection<Billing> map(Collection<PaidBilling> pads){
        return pads.stream().map(this::map).collect(Collectors.toList());
    }
}
