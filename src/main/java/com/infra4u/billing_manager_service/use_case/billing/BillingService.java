package com.infra4u.billing_manager_service.use_case.billing;

import com.infra4u.billing_manager_service.use_case.billing.model.request.CreateBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.LoadBillingQueryParams;
import com.infra4u.billing_manager_service.use_case.billing.model.request.PaidBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.SimpleBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;
import com.infra4u.billing_manager_service.use_case.http.request.PageFilter;
import org.springframework.data.domain.Page;


public interface BillingService {

    Page<LoadedBilling> loadAll(PageFilter pageFilter);
    LoadedBilling create(PaidBilling paid);
    LoadedBilling create(CreateBilling billing);
    LoadedBilling create(SimpleBilling billing);
    LoadedBilling loadById(Long id);
    Page<LoadedBilling> loadByOrder(String order);
    Page<LoadedBilling> loadByService(Long service, PageFilter pageFilter);
    Page<LoadedBilling> loadByService(Long service, LoadBillingQueryParams pageFilter);
    LoadedBilling loadByServiceAndOrder(Long service, String order);
    LoadedBilling update(Long id, SimpleBilling billing);
    Void cancel(String code);
    Void del(String code);
}
