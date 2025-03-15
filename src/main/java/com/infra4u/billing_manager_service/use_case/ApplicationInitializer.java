package com.infra4u.billing_manager_service.use_case;

import com.infra4u.billing_manager_service.entity.Status;
import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationInitializer {


    @PostConstruct
    public void init() {
        log.info("Application Initializer");
    }


    /**
     * create initial billing and payment status
     */
    private void status() {
        // @fixme verificar por que está dando erro no criar os status
        log.info("[status] Initializing...");
        Collection<Status> statusCollection = Stream.of(Statuses.values())
                .map(status -> new Status(status.getStatus(), status.name()))
                .toList();
        log.info("[status] Initialized.");
    }

    private void billingType() {
        log.info("[billingType] Initializing...");
        // @todo inserir tipos de pagamento

    }
}
