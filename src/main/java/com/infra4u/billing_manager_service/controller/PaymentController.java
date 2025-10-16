package com.infra4u.billing_manager_service.controller;

import com.infra4u.billing_manager_service.model.http.response.Wrapper;
import com.infra4u.billing_manager_service.use_case.payment.model.request.CreatePayment;
import com.infra4u.billing_manager_service.use_case.payment.model.response.LoadedPayment;
import com.infra4u.billing_manager_service.use_case.payment.PaymentService;
import com.infra4u.billing_manager_service.use_case.payment.model.request.UpdatePayment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payments")
@Tag(name = "Payment", description = "Payment management APIs")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PutMapping
    @Operation(summary = "Update Payment", description = "Update an payment", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<LoadedPayment> update(@RequestBody UpdatePayment data) {
        return Wrapper.of(paymentService.update(data));
    }

    @PostMapping
    @Operation(summary = "Create Payment", description = "Create an payment", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<LoadedPayment> create(@Valid @RequestBody CreatePayment data) {
        return Wrapper.of(paymentService.create(data));
    }
}
