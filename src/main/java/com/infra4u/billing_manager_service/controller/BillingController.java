package com.infra4u.billing_manager_service.controller;

import com.infra4u.billing_manager_service.use_case.billing.BillingService;
import com.infra4u.billing_manager_service.use_case.billing.CreateBilling;
import com.infra4u.billing_manager_service.use_case.billing.LoadedBilling;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("billings")
public class BillingController {

    private final BillingService billingService;

    @GetMapping
    @Operation(summary = "Load all", description = "Load all billings with params")
    public Collection<LoadedBilling> loadAll() {
        return billingService.loadAll();
    }

    @PostMapping
    @Operation(summary = "Save Billing", description = "Sabe billing")
    public LoadedBilling save(CreateBilling create) {
        return billingService.create(create);
    }
}
