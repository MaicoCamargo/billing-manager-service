package com.infra4u.billing_manager_service.use_case.billing;

import com.infra4u.billing_manager_service.entity.Billing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    Page<Billing> findByOrderId(Long order, Pageable pageable);
    Page<Billing> findByServiceId(Long service, Pageable pageable);
    Billing findByServiceIdAndOrderId(Long service, Long order);
}
