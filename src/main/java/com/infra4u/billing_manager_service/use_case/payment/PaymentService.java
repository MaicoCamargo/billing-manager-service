package com.infra4u.billing_manager_service.use_case.payment;

import com.infra4u.billing_manager_service.use_case.payment.model.request.CreatePayment;
import com.infra4u.billing_manager_service.use_case.payment.model.request.UpdatePayment;
import com.infra4u.billing_manager_service.use_case.payment.model.response.LoadedPayment;

public interface PaymentService {

    LoadedPayment update(UpdatePayment data);
    LoadedPayment create(CreatePayment data);
}
