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

        // @todo verificar por que esta sempre inserindo installment 1
        final List<Payment> payments = billing.futurePayments()
                .stream()
                .map((payment) -> new Payment(payment.value(), 1, this.paymentType(payment.type().getId()), payment.expiresAt().atTime(LocalTime.MAX))).toList();

        // todo verificar se valor dos pagamentos (total) nao é maior que billing.amount caso sim retornar erro
        return new Billing(
                billing.name(),
                billing.description(),
                billing.amount(),
                billing.futurePayments().size(),
                billing.order(),
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
