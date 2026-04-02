package com.infra4u.billing_manager_service.controller;

import com.infra4u.billing_manager_service.model.http.response.Wrapper;
import com.infra4u.billing_manager_service.use_case.billing.BillingService;
import com.infra4u.billing_manager_service.use_case.billing.model.request.CreateBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.LoadBillingQueryParams;
import com.infra4u.billing_manager_service.use_case.billing.model.request.SimpleBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.response.LoadedBilling;
import com.infra4u.billing_manager_service.use_case.billing.model.request.PaidBilling;
import com.infra4u.billing_manager_service.use_case.http.request.PageFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
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
    @Operation(summary = "Load all", description = "Load all billings with params", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<Collection<LoadedBilling>> loadAll(PageFilter pageFilter) {
        return Wrapper.of(billingService.loadAll(pageFilter));
    }

    @PostMapping("paid")
    @Operation(summary = "Save Billing", description = "Save billing", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<LoadedBilling> paid(@RequestBody PaidBilling paid) {
        return Wrapper.of(billingService.create(paid));
    }

    @PatchMapping("{code}/cancel")
    @Operation(summary = "Cancel Billing", description = "Cancel billing payments", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<Void> cancel(@PathVariable String code) {
        return Wrapper.of(billingService.cancel(code));
    }

    @DeleteMapping("order/{code}")
    @Operation(summary = "Delete Billing", description = "Delete billing", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<Void> delete(@PathVariable String code) {
        return Wrapper.of(billingService.del(code));
    }

    @PostMapping
    @Operation(summary = "Save Billing", description = "Save billing", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<LoadedBilling> save(@RequestBody CreateBilling billing) {
        return Wrapper.of(billingService.create(billing));
    }

    @PostMapping("simple")
    @Operation(summary = "Save Billing", description = "Save simple billing", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<LoadedBilling> save(@RequestBody SimpleBilling billing) {
        return Wrapper.of(billingService.create(billing));
    }

    @GetMapping("{id}")
    @Operation(summary = "Load billing", description = "Load billing by id", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<LoadedBilling> loadById(@PathVariable Long id) {
        return Wrapper.of(billingService.loadById(id));
    }

    @GetMapping("order/{order}")
    @Operation(summary = "Load billing", description = "Load billing by order id", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<Collection<LoadedBilling>> loadByOrderId(@PathVariable String order) {
        return Wrapper.of(billingService.loadByOrder(order));
    }

    @GetMapping("service/{service}")
    @Operation(summary = "Load billing", description = "Load billing by service id", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<Collection<LoadedBilling>> loadByServiceId(@PathVariable Long service, LoadBillingQueryParams filter) {
        return Wrapper.of(billingService.loadByService(service, filter));
    }

    @GetMapping("service/{service}/order/{order}")
    @Operation(summary = "Load billing", description = "Load billing by service id and order id", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<LoadedBilling> loadByServiceId(@PathVariable @PathParam("1") Long service, @PathVariable String order) {
        return Wrapper.of(billingService.loadByServiceAndOrder(service, order));
    }

    @PutMapping("{id}")
    @Operation(summary = "Update an billing", description = "Update billing using the simple form", parameters = {
            @Parameter(ref = "#/components/parameters/X-Client-ID")
    })
    public Wrapper<LoadedBilling> update(@PathVariable Long id, @RequestBody SimpleBilling simpleBilling) {
        return Wrapper.of(billingService.update(id, simpleBilling));
    }


}
