package com.infra4u.billing_manager_service.service;

import com.infra4u.billing_manager_service.entity.Payment;
import com.infra4u.billing_manager_service.use_case.payment.PaymentRepository;
import com.infra4u.billing_manager_service.use_case.payment.PaymentService;
import com.infra4u.billing_manager_service.use_case.payment.mapper.UpdatePaymentMapperStrategy;
import com.infra4u.billing_manager_service.use_case.payment.model.request.UpdatePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UpdatePaymentMapperStrategy updatePaymentMapperStrategy;

    @Override
    public void update(UpdatePayment data) {
        final Payment saved = paymentRepository.save(this.updatePaymentMapperStrategy.map(data));
        System.out.println(saved);
    }
}
