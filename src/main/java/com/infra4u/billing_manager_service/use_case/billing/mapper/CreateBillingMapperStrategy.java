package com.infra4u.billing_manager_service.use_case.billing.mapper;

import com.infra4u.billing_manager_service.entity.Billing;
import com.infra4u.billing_manager_service.entity.Payment;
import com.infra4u.billing_manager_service.entity.PaymentType;
import com.infra4u.billing_manager_service.use_case.billing.BillingMapperStrategy;
import com.infra4u.billing_manager_service.use_case.billing.model.Types;
import com.infra4u.billing_manager_service.use_case.billing.model.request.CreateBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.FuturePayment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreateBillingMapperStrategy implements BillingMapperStrategy<Billing, CreateBilling> {
    @Override
    public Billing map(CreateBilling billing) {
        final BigDecimal total = billing.futurePayments().stream()
                .map(FuturePayment::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final List<Payment> payments = billing.futurePayments()
                .stream()
                .flatMap((payment) -> {
                    java.util.List<Payment> installmentPayments = new java.util.ArrayList<>();
                    BigDecimal installmentValue = payment.value().divide(BigDecimal.valueOf(payment.installments()), java.math.RoundingMode.HALF_UP);
                    for (int i = 1; i <= payment.installments(); i++) {
                        BigDecimal value = installmentValue;
                        if (i == payment.installments()) {
                            value = payment.value().subtract(installmentValue.multiply(BigDecimal.valueOf(payment.installments() - 1)));
                        }
                        installmentPayments.add(new Payment(value, i, this.paymentType(payment.type().getId()), payment.expiresAt().atTime(java.time.LocalTime.MAX)));
                    }
                    return installmentPayments.stream();
                }).toList();

        // todo verificar se valor dos pagamentos (total) nao é maior que billing.amount caso sim retornar erro
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
    public Collection<Billing> map(Collection<CreateBilling> billings) {
        return billings.stream().map(this::map).collect(Collectors.toList());
    }

    private PaymentType paymentType(Long id){
        final Types type = Types.getById(id);
        return new PaymentType(type.name(), type.getId());
    }
}
