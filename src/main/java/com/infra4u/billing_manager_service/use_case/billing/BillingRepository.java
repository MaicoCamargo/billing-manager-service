package com.infra4u.billing_manager_service.use_case.billing;

import com.infra4u.billing_manager_service.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    Collection<Billing> findByOrderId(Long order);
    Collection<Billing> findByServiceId(Long service);
    Billing findByServiceIdAndOrderId(Long service, Long order);
}
