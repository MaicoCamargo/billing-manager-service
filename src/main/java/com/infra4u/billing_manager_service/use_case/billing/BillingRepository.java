package com.infra4u.billing_manager_service.use_case.billing;

import com.infra4u.billing_manager_service.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<Billing, Long> {
}
