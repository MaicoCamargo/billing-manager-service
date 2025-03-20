package com.infra4u.billing_manager_service.use_case.billing.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoadedStatus(String name, Long id, LocalDateTime date) {

}
