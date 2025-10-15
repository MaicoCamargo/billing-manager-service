package com.infra4u.billing_manager_service.use_case.billing;

import com.infra4u.billing_manager_service.entity.Billing;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    Page<Billing> findByOrderId(Long order, Pageable pageable);
    Page<Billing> findByServiceId(Long service, Pageable pageable);
    Billing findByServiceIdAndOrderId(Long service, Long order);
    @Query("""
        SELECT billing
        FROM Billing billing
        WHERE billing.serviceId = :service
          AND billing.createdAt >= COALESCE(:startDate, billing.createdAt)
          AND billing.createdAt <= COALESCE(:endDate, billing.createdAt)
          AND (:user IS NULL OR billing.userId = :user)
          AND (:code IS NULL OR billing.code = :code)
          AND (:order IS NULL OR billing.orderId = :order)
    """)
    Page<Billing> findByLoadBillingQueryParams(
            @Param("service") Long service,
            @Nullable @Param("startDate") LocalDateTime startDate,
            @Nullable @Param("endDate") LocalDateTime endDate,
            @Nullable @Param("user") Long user,
            @Nullable @Param("code") String code,
            @Nullable @Param("order") Long order,
            Pageable pageable);

}
