package com.infra4u.billing_manager_service.service;

import com.infra4u.billing_manager_service.entity.*;
import com.infra4u.billing_manager_service.use_case.billing.*;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final BillingRepository repository;

    @Override
    public Collection<LoadedBilling> loadAll() {
        List<Billing> all = repository.findAll();
        return all.stream()
                .map(billing -> new LoadedBilling(billing.getId(), billing.getName(), billing.getDescription(), billing.getAmount(), Collections.emptyList())).collect(Collectors.toList());
    }

    @Override
    public LoadedBilling create(PaidBilling paid) {
        Payment payment = new Payment(paid.amount(), 1, new PaymentType(paid.type().getType()), LocalDateTime.now(), new PaymentStatus(new Status(Statuses.COMPLETED.getStatus())));
        Billing billing = new Billing(paid.name(), paid.description(), paid.amount(), 1, paid.order(), Collections.singletonList(payment));
        Billing saved = repository.save(billing);
        return new LoadedBilling(saved.getId(), saved.getName(), saved.getDescription(), saved.getAmount(), Collections.emptyList());
    }
}
