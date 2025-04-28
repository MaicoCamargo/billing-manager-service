package com.infra4u.billing_manager_service.use_case.billing.mapper;

import com.infra4u.billing_manager_service.entity.*;
import com.infra4u.billing_manager_service.use_case.billing.BillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.billing.model.request.PaidBilling;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaidBillingMapperStrategy implements BillingMapperStrategy<Billing, PaidBilling> {

    /**
     * create a billing paid
     * @param paid record with data to billing paid
     * @return billing entity
     */
    @Override
    public Billing map(PaidBilling paid) {
        final Integer ONLY_ONE_INSTALLMENT = 1;
        Payment payment = new Payment(paid.amount(), ONLY_ONE_INSTALLMENT, type(paid.type().getId()), LocalDateTime.now(), statuses());
        return new Billing(paid.name(), paid.description(), paid.amount(), ONLY_ONE_INSTALLMENT, paid.order(), paid.service().longValue(), paid.user().longValue(), Collections.singletonList(payment));
    }

    @Override
    public Collection<Billing> map(Collection<PaidBilling> pads){
        return pads.stream().map(this::map).collect(Collectors.toList());
    }

    private List<PaymentStatus> statuses(){
        PaymentStatus pending = new PaymentStatus(new Status(Statuses.PENDING.getId(), Statuses.PENDING.name()));
        PaymentStatus completed = new PaymentStatus(new Status(Statuses.COMPLETED.getId(), Statuses.COMPLETED.name()));
        List<PaymentStatus> status = new ArrayList<>();
        status.add(pending);
        status.add(completed);
        return status;
    }

    private PaymentType type(Long id){
        final Types type = Types.getById(id);
        return new PaymentType(type.name(), type.getId());
    }
}
