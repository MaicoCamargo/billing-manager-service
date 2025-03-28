package com.infra4u.billing_manager_service.controller;

import com.infra4u.billing_manager_service.model.http.response.Wrapper;
import com.infra4u.billing_manager_service.use_case.billing.BillingService;
import com.infra4u.billing_manager_service.use_case.billing.model.request.CreateBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.PaidBilling;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("billings")
public class BillingController {

    private final BillingService billingService;

    @GetMapping
    @Operation(summary = "Load all", description = "Load all billings with params")
    public Wrapper<Collection<LoadedBilling>> loadAll() {
        return Wrapper.of(billingService.loadAll());
    }

    @PostMapping("paid")
    @Operation(summary = "Save Billing", description = "Save billing")
    public Wrapper<LoadedBilling> paid(@RequestBody PaidBilling paid) {
        return Wrapper.of(billingService.create(paid));
    }

    @PostMapping
    @Operation(summary = "Save Billing", description = "Save billing")
    public Wrapper<LoadedBilling> save(@RequestBody CreateBilling billing) {
        return Wrapper.of(billingService.create(billing));
    }
}
