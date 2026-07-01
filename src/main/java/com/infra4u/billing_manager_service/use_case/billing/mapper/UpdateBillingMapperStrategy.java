package com.infra4u.billing_manager_service.use_case.billing.mapper;

import com.infra4u.billing_manager_service.entity.*;
import com.infra4u.billing_manager_service.exception.NotFoundException;
import com.infra4u.billing_manager_service.use_case.billing.BillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.BillingRepository;
import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.billing.model.request.SimpleBillingUpdate;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UpdateBillingMapperStrategy implements BillingMapperStrategy<Billing, SimpleBillingUpdate> {

    private final BillingRepository repository;
    @Override
    public Billing map(SimpleBillingUpdate data) {
        final Optional<Billing> byId = repository.findById(data.id()).stream().findFirst();
        if (byId.isEmpty()) {
            throw new NotFoundException("Billing not found");
        }
        final List<Payment> payments = new ArrayList<>();
        data.payments()
                .forEach((payment) -> {
                    LocalDateTime hoje = LocalDateTime.now();
                    for (int i = 1; i <= payment.installments(); i++) {
                        LocalDateTime nextMonth =  hoje.plusMonths(i-1).withDayOfMonth(1).with(TemporalAdjusters.firstDayOfNextMonth())
                                .withHour(23)
                                .withMinute(59)
                                .withSecond(59);
                        final Payment newPayment = new Payment(byId.get().getId(), payment.value().divide(BigDecimal.valueOf(payment.installments()), RoundingMode.UP), i, this.paymentType(payment.type().getId()), nextMonth, this.paymentStatuses(payment.type(), nextMonth));
                        payments.add(newPayment);
                    }
                });
        final Billing billing = byId.get();
        billing.setName(billing.getName() + "::" + data.name());
        billing.setDescription(data.description());
        billing.setUserId(data.user());
        billing.getPayments().clear();
        billing.getPayments().addAll(payments);
        billing.setInstallmentCount(payments.size());
        return billing;
    }

    @Override
    public Collection<Billing> map(Collection<SimpleBillingUpdate> billings) {
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
