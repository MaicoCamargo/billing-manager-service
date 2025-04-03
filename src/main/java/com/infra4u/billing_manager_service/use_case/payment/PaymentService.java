package com.infra4u.billing_manager_service.use_case.payment;

import com.infra4u.billing_manager_service.use_case.payment.model.request.UpdatePayment;

public interface PaymentService {

    void update(UpdatePayment data);
}
