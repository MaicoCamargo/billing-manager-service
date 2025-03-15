package com.infra4u.billing_manager_service.use_case.billing;

import java.util.Collection;

public interface BillingService {

    Collection<LoadedBilling> loadAll();
    LoadedBilling create(CreateBilling billing);
}
