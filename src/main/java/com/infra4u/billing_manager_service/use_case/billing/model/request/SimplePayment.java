package com.infra4u.billing_manager_service.use_case.billing.model.request;

import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;

import java.math.BigDecimal;

public record SimplePayment(Integer installments,
                            BigDecimal value,
                            Statuses status,
                            Types type) {
}
