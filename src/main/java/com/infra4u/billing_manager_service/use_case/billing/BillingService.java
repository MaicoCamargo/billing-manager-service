package com.infra4u.billing_manager_service.use_case.billing;

import com.infra4u.billing_manager_service.use_case.billing.model.request.CreateBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.PaidBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.SimpleBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;
import com.infra4u.billing_manager_service.use_case.http.request.PageFilter;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface BillingService {

    Page<LoadedBilling> loadAll(PageFilter pageFilter);
    LoadedBilling create(PaidBilling paid);
    LoadedBilling create(CreateBilling billing);
    LoadedBilling create(SimpleBilling billing);
    LoadedBilling loadById(Long id);
    Page<LoadedBilling> loadByOrder(Long order);
    Page<LoadedBilling> loadByService(Long service, PageFilter pageFilter);
    LoadedBilling loadByServiceAndOrder(Long service, Long order);
    LoadedBilling update(Long id, SimpleBilling billing);
}
