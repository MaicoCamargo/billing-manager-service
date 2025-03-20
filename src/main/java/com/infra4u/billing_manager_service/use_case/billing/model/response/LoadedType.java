package com.infra4u.billing_manager_service.use_case.billing.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoadedType(String name, Long id) {
}
