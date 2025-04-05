package com.infra4u.billing_manager_service.service;

import com.infra4u.billing_manager_service.entity.Billing;
import com.infra4u.billing_manager_service.entity.Payment;
import com.infra4u.billing_manager_service.use_case.billing.BillingRepository;
import com.infra4u.billing_manager_service.use_case.payment.PaymentRepository;
import com.infra4u.billing_manager_service.use_case.payment.PaymentService;
import com.infra4u.billing_manager_service.use_case.payment.mapper.CreatePaymentMapperStrategy;
import com.infra4u.billing_manager_service.use_case.payment.mapper.LoadedPaymentMapperStrategy;
import com.infra4u.billing_manager_service.use_case.payment.mapper.UpdatePaymentMapperStrategy;
import com.infra4u.billing_manager_service.use_case.payment.model.request.CreatePayment;
import com.infra4u.billing_manager_service.use_case.payment.model.request.UpdatePayment;
import com.infra4u.billing_manager_service.use_case.payment.model.response.LoadedPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UpdatePaymentMapperStrategy updatePaymentMapperStrategy;
    private final BillingRepository billingRepository;
    private final CreatePaymentMapperStrategy createPaymentMapperStrategy;
    private final LoadedPaymentMapperStrategy loadedPaymentMapperStrategy;

    @Override
    public LoadedPayment update(UpdatePayment data) {
        final Payment saved = paymentRepository.save(this.updatePaymentMapperStrategy.map(data));
        return loadedPaymentMapperStrategy.map(saved);
    }

    @Override
    public LoadedPayment create(CreatePayment data) {
        final Optional<Billing> billing = billingRepository.findById(data.billing());
        if (billing.isEmpty()) {
            throw new RuntimeException("Billing not found");
        }
        final BigDecimal total = billing.stream()
                .map(Billing::getPayments)
                .map(payments -> payments.stream().map(Payment::getValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(billing.get().getAmount()) >= 0){
            throw new RuntimeException("Total amount exceeds billing amount");
        }
        final Payment saved = paymentRepository.save(createPaymentMapperStrategy.map(data));
        return loadedPaymentMapperStrategy.map(saved);
    }
}
