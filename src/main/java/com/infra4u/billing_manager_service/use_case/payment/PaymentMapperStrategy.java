package com.infra4u.billing_manager_service.use_case.payment;

import java.util.Collection;

public interface PaymentMapperStrategy <R, P> {
    R map(P data);
    Collection<R> map(Collection<P> data);

}
