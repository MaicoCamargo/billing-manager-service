package com.infra4u.billing_manager_service.service;

import com.infra4u.billing_manager_service.entity.*;
import com.infra4u.billing_manager_service.use_case.billing.*;
import com.infra4u.billing_manager_service.use_case.billing.mapper.LoadedBillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.mapper.PaidBillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.model.request.PaidBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final BillingRepository repository;
    private final LoadedBillingMapperStrategy loadedBillingMapperStrategy;
    private final PaidBillingMapperStrategy paidBillingMapperStrategy;

    @Override
    public Collection<LoadedBilling> loadAll() {
        List<Billing> all = repository.findAll();
        return loadedBillingMapperStrategy.map(all);
    }

    @Override
    public LoadedBilling create(PaidBilling paid) {
        Billing saved = repository.save(paidBillingMapperStrategy.map(paid));
        return loadedBillingMapperStrategy.map(saved);
    }
}
