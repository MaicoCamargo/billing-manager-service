package com.infra4u.billing_manager_service.service;

import com.infra4u.billing_manager_service.config.multi_tenant.TenantContext;
import com.infra4u.billing_manager_service.config.multi_tenant.TenantPropertySource;
import com.infra4u.billing_manager_service.entity.Payment;
import com.infra4u.billing_manager_service.entity.PaymentStatus;
import com.infra4u.billing_manager_service.entity.Status;
import com.infra4u.billing_manager_service.use_case.payment.PaymentRepository;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentStatusScheduler {

    private final PaymentRepository paymentRepository;
    private final TenantPropertySource tenantPropertySource;

    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void updatePendingPayments() {
        tenantPropertySource.loadTenantProperties().keySet().forEach(tenant -> {
            try {
                TenantContext.setCurrentTenant(tenant);
                LocalDate firstDayNextMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1);
                List<Payment> payments = paymentRepository.findPendingCreditCardPayments(firstDayNextMonth.atTime(23, 59, 59));

                for (Payment payment : payments) {
                    Status completedStatus = new Status(Statuses.COMPLETED.getId());
                    PaymentStatus newStatus = new PaymentStatus(completedStatus, payment);
                    payment.getPaymentStatuses().add(newStatus);
                    System.out.println(payment);
                }
                paymentRepository.saveAll(payments);
            } finally {
                TenantContext.setCurrentTenant(null);
            }
        });
    }
}
