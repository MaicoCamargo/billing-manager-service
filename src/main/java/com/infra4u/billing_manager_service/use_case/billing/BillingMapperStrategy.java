package com.infra4u.billing_manager_service.use_case.billing;

import java.util.Collection;

public interface BillingMapperStrategy <R, P> {
    R map(P billing);
    Collection<R> map(Collection<P> billings);
}
