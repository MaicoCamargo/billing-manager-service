package com.infra4u.billing_manager_service.use_case.billing;

import com.infra4u.billing_manager_service.use_case.billing.model.request.PaidBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;

import java.util.Collection;

public interface BillingService {

    Collection<LoadedBilling> loadAll();
    LoadedBilling create(PaidBilling paid);
}
