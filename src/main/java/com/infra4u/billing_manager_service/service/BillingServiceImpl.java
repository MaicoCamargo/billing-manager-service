package com.infra4u.billing_manager_service.service;

import com.infra4u.billing_manager_service.entity.Billing;
import com.infra4u.billing_manager_service.exception.NotFoundException;
import com.infra4u.billing_manager_service.use_case.billing.BillingRepository;
import com.infra4u.billing_manager_service.use_case.billing.BillingService;
import com.infra4u.billing_manager_service.use_case.billing.mapper.*;
import com.infra4u.billing_manager_service.use_case.billing.model.request.*;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;
import com.infra4u.billing_manager_service.use_case.http.request.PageFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final BillingRepository repository;
    private final LoadedBillingMapperStrategy loadedBillingMapperStrategy;
    private final PaidBillingMapperStrategy paidBillingMapperStrategy;
    private final CreateBillingMapperStrategy createBillingMapperStrategy;
    private final SimpleBillingMapperStrategy simpleBillingMapperStrategy;
    private final UpdateBillingMapperStrategy updateBillingMapperStrategy;
    private final Sort SORT_BY_CREATED_AT_DESC = Sort.by(Sort.Direction.DESC, "createdAt");

    @Override
    public Page<LoadedBilling> loadAll(final PageFilter pageFilter) {
        final Page<Billing> all = repository.findAll(pageFilter.map(pageFilter, SORT_BY_CREATED_AT_DESC));
        return all.map(loadedBillingMapperStrategy::map);
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

    @Override
    public LoadedBilling create(SimpleBilling billing) {
        final Billing saved = repository.save(simpleBillingMapperStrategy.map(billing));
        return loadedBillingMapperStrategy.map(saved);
    }

    @Override
    public LoadedBilling loadById(Long id) {
        final Optional<Billing> byId = repository.findById(id);
        if (byId.isEmpty()) {
            throw new NotFoundException("Billing with id " + id + " not found");
        }
        return loadedBillingMapperStrategy.map(byId.get());
    }

    @Override
    public Page<LoadedBilling> loadByOrder(Long order) {
        final Page<Billing> byOrderId = repository.findByOrderId(order, Pageable.unpaged(SORT_BY_CREATED_AT_DESC));
        return byOrderId.map(loadedBillingMapperStrategy::map);
    }

    @Override
    public Page<LoadedBilling> loadByService(Long service, PageFilter pageFilter) {
        final Page<Billing> byServiceId = repository.findByServiceId(service, pageFilter.map(pageFilter, SORT_BY_CREATED_AT_DESC));
        return byServiceId.map(loadedBillingMapperStrategy::map);
    }

    @Override
    public Page<LoadedBilling> loadByService(Long service, LoadBillingQueryParams filter) {
        // @todo do filter 'status'
        final PageFilter pageFilter = new PageFilter(filter.page(), filter.size());
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (Objects.nonNull(filter.startDate())) {
            startDate = filter.startDate().atStartOfDay();
        }
        if (Objects.nonNull(filter.endDate())) {
            endDate = filter.endDate().atTime(23, 59, 59);
        }
        final Page<Billing> byLoadBillingQueryParams = this.repository.findByLoadBillingQueryParams(service, startDate, endDate, filter.user(), pageFilter.map(pageFilter, SORT_BY_CREATED_AT_DESC));
        return byLoadBillingQueryParams.map(loadedBillingMapperStrategy::map);
    }

    @Override
    public LoadedBilling loadByServiceAndOrder(Long service, Long order) {
        final Billing byServiceIdAndOrderId = repository.findByServiceIdAndOrderId(service, order);
        if (Objects.isNull(byServiceIdAndOrderId)) {
            return null;
        }
        return loadedBillingMapperStrategy.map(byServiceIdAndOrderId);
    }

    @Override
    public LoadedBilling update(Long id, SimpleBilling billing) {
        final SimpleBillingUpdate simpleBillingUpdate = new SimpleBillingUpdate(billing.name(), billing.description(), billing.amount(), id, billing.user(), billing.service(), billing.payments());
        final Billing mapped = this.updateBillingMapperStrategy.map(simpleBillingUpdate);
        final Billing saved = this.repository.save(mapped);
        return loadedBillingMapperStrategy.map(saved);
    }
}
