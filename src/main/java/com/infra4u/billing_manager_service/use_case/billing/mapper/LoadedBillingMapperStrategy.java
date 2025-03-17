package com.infra4u.billing_manager_service.use_case.billing.mapper;

import com.infra4u.billing_manager_service.entity.Billing;
import com.infra4u.billing_manager_service.use_case.billing.BillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class LoadedBillingMapperStrategy implements BillingMapperStrategy<LoadedBilling, Billing> {

    @Override
    public LoadedBilling map(Billing billing) {
        // @todo mapear corretamente Collections.emptyList()
        return new LoadedBilling(billing.getId(), billing.getName(), billing.getDescription(), billing.getAmount(), Collections.emptyList());
    }

    @Override
    public Collection<LoadedBilling> map(Collection<Billing> billings) {
        // @todo mapear corretamente Collections.emptyList()
        return billings.stream()
                .map(billing -> new LoadedBilling(billing.getId(), billing.getName(), billing.getDescription(), billing.getAmount(), Collections.emptyList()))
                .collect(Collectors.toList());
    }
}
