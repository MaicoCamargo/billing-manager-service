package com.infra4u.billing_manager_service.use_case.payment;

import com.infra4u.billing_manager_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p " +
            "JOIN p.paymentStatuses ps " +
            "WHERE p.paymentType.id = 3 " +
            "AND ps.status.id = 1 " +
            "AND p.expirationDate <= :expirationDate")
    List<Payment> findPendingCreditCardPayments(@Param("expirationDate") LocalDateTime expirationDate);
}
