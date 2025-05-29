package com.infra4u.billing_manager_service.use_case.http.request;


import com.infra4u.billing_manager_service.use_case.http.response.PageFilterStrategy;
import io.swagger.v3.oas.annotations.media.Schema;

public record PageFilter(@Schema(defaultValue = "0") Integer page,@Schema(description = "Page size", defaultValue = "10") Integer size) implements PageFilterStrategy {}