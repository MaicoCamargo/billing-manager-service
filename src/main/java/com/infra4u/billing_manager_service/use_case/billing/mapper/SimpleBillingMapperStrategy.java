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
                        final Payment newPayment = new Payment(payment.value().divide(BigDecimal.valueOf(payment.installments()), RoundingMode.UP), i, this.paymentType(payment.type().getId()), nextMonth);
                        payments.add(newPayment);
                    }
                });

        return new Billing(
                billing.name(),
                billing.description(),
                billing.amount(),
                billing.payments().size(),
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
}
