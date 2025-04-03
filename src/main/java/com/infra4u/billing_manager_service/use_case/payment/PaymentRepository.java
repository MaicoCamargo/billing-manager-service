package com.infra4u.billing_manager_service.use_case.payment;

import com.infra4u.billing_manager_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
