package com.infra4u.billing_manager_service.use_case.billing.mapper;

import com.infra4u.billing_manager_service.entity.*;
import com.infra4u.billing_manager_service.use_case.billing.BillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.billing.model.request.SimpleBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.SimplePayment;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SimpleBillingMapperStrategy implements BillingMapperStrategy<Billing, SimpleBilling> {

    @Override
    public Billing map(SimpleBilling billing) {
        final BigDecimal total = billing.payments().stream()
                .map(SimplePayment::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final List<Payment> payments = new ArrayList<>();
        billing.payments()
                .forEach((payment) -> {
                    LocalDateTime hoje = LocalDateTime.now();
                    for (int i = 1; i <= payment.installments(); i++) {
                        LocalDateTime nextMonth =  hoje.plusMonths(i-1).withDayOfMonth(1).with(TemporalAdjusters.firstDayOfNextMonth())
                                .withHour(23)
                                .withMinute(59)
                                .withSecond(59);
                        BigDecimal installmentValue = payment.value().divide(BigDecimal.valueOf(payment.installments()), RoundingMode.HALF_UP);
                        if (i == payment.installments()) {
                            installmentValue = payment.value().subtract(installmentValue.multiply(BigDecimal.valueOf(payment.installments() - 1)));
                        }
                        final Payment newPayment = new Payment(installmentValue, i, this.paymentType(payment.type().getId()), nextMonth, this.paymentStatuses(payment.type(), nextMonth));
                        payments.add(newPayment);
                    }
                });

        return new Billing(
                billing.name(),
                billing.description(),
                billing.amount(),
                payments.size(),
                billing.order(),
                billing.service(),
                billing.user(),
                payments
        );
    }

    @Override
    public Collection<Billing> map(Collection<SimpleBilling> billings) {
        return billings.stream().map(this::map).collect(Collectors.toList());
    }

    private PaymentType paymentType(Long id){
        final Types type = Types.getById(id);
        return new PaymentType(type.name(), type.getId());
    }

    private List<PaymentStatus> paymentStatuses(final Types type, LocalDateTime expirationDate) {
        final ArrayList<PaymentStatus> statuses = new ArrayList<>();
        Statuses baseStatusType = (expirationDate != null && expirationDate.isAfter(LocalDateTime.now())) ? Statuses.IN_PROGRESS : Statuses.PENDING;
        PaymentStatus baseStatus = new PaymentStatus(new Status(baseStatusType.getId(), baseStatusType.name()));
        statuses.add(baseStatus);
        if (Types.CASH_ON_DELIVERY.name().equals(type.name()) || Types.DEBIT_CARD.name().equals(type.name())) {
             PaymentStatus paid = new PaymentStatus(new Status(Statuses.COMPLETED.getId(), Statuses.COMPLETED.name()));
             statuses.add(paid);
        }
        return statuses;
    }
}
