package com.infra4u.billing_manager_service.use_case.billing.mapper;

import com.infra4u.billing_manager_service.entity.*;
import com.infra4u.billing_manager_service.use_case.billing.BillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.model.request.PaidBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaidBillingMapperStrategy implements BillingMapperStrategy<Billing, PaidBilling> {

    @Override
    public Billing map(PaidBilling paid) {
        Payment payment = new Payment(paid.amount(), 1, new PaymentType(paid.type().getType()), LocalDateTime.now(), new PaymentStatus(new Status(Statuses.COMPLETED.getStatus())));
        Billing billing = new Billing(paid.name(), paid.description(), paid.amount(), 1, paid.order(), Collections.singletonList(payment));
        return billing;
    }

    @Override
    public Collection<Billing> map(Collection<PaidBilling> pads){
        return pads.stream().map(this::map).collect(Collectors.toList());
    }
}
