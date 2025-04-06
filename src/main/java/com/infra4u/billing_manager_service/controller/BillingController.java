package com.infra4u.billing_manager_service.controller;

import com.infra4u.billing_manager_service.model.http.response.Wrapper;
import com.infra4u.billing_manager_service.use_case.billing.BillingService;
import com.infra4u.billing_manager_service.use_case.billing.model.request.CreateBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.PaidBilling;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("billings")
@Tag(name = "Billing", description = "Billing management APIs")
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

    @GetMapping("{id}")
    @Operation(summary = "Load billing", description = "Load billing by id")
    public Wrapper<LoadedBilling> loadById(@PathVariable Long id) {
        return Wrapper.of(billingService.loadById(id));
    }

    @GetMapping("order/{order}")
    @Operation(summary = "Load billing", description = "Load billing by order id")
    public Wrapper<Collection<LoadedBilling>> loadByOrderId(@PathVariable Long order) {
        return Wrapper.of(billingService.loadByOrder(order));
    }

}
