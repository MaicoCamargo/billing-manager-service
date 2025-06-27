package com.infra4u.billing_manager_service.use_case.billing.model.request;

import java.math.BigDecimal;
import java.util.List;

public record SimpleBillingUpdate(String name,
                                  String description,
                                  BigDecimal amount,
                                  Long id,
                                  Long user,
                                  Long service,
                                  List<SimplePayment> payments) {

}
