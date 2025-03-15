package com.infra4u.billing_manager_service.service;

import com.infra4u.billing_manager_service.entity.Billing;
import com.infra4u.billing_manager_service.entity.BillingType;
import com.infra4u.billing_manager_service.use_case.billing.BillingRepository;
import com.infra4u.billing_manager_service.use_case.billing.BillingService;
import com.infra4u.billing_manager_service.use_case.billing.CreateBilling;
import com.infra4u.billing_manager_service.use_case.billing.LoadedBilling;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final BillingRepository repository;

    @Override
    public Collection<LoadedBilling> loadAll() {
        List<Billing> all = repository.findAll();
        return all.stream()
                .map(billing -> new LoadedBilling(billing.getId(), billing.getName(), billing.getDescription())).collect(Collectors.toList());
    }

    @Override
    public LoadedBilling create(CreateBilling data) {
        Billing billing = new Billing(data.name(), data.description(), new BillingType(data.type().getType()), data.amount(), 1L, data.installmentCount());
        Billing saved = repository.save(billing);
        return new LoadedBilling(saved.getId(), saved.getName(), saved.getDescription());
    }
}
