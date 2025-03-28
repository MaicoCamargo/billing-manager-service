package com.infra4u.billing_manager_service.service;

import com.infra4u.billing_manager_service.entity.Billing;
import com.infra4u.billing_manager_service.use_case.billing.BillingRepository;
import com.infra4u.billing_manager_service.use_case.billing.BillingService;
import com.infra4u.billing_manager_service.use_case.billing.mapper.CreateBillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.mapper.LoadedBillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.mapper.PaidBillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.model.request.CreateBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.PaidBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Tag(name = "Billing", description = "Billing management APIs")
public class BillingServiceImpl implements BillingService {

    private final BillingRepository repository;
    private final LoadedBillingMapperStrategy loadedBillingMapperStrategy;
    private final PaidBillingMapperStrategy paidBillingMapperStrategy;
    private final CreateBillingMapperStrategy createBillingMapperStrategy;

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

    @Override
    public LoadedBilling create(CreateBilling billing) {
        final Billing save = repository.save(createBillingMapperStrategy.map(billing));
        return loadedBillingMapperStrategy.map(save);
    }
}
