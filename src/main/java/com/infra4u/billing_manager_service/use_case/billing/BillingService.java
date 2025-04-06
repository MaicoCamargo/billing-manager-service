package com.infra4u.billing_manager_service.use_case.billing;

import com.infra4u.billing_manager_service.use_case.billing.model.request.CreateBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.PaidBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;

import java.util.Collection;

public interface BillingService {

    Collection<LoadedBilling> loadAll();
    LoadedBilling create(PaidBilling paid);
    LoadedBilling create(CreateBilling billing);
    LoadedBilling loadById(Long id);
    Collection<LoadedBilling> loadByOrder(Long order);
    Collection<LoadedBilling> loadByService(Long service);
}
