package com.infra4u.billing_manager_service.use_case.http.response;

import org.springframework.data.domain.Page;

public record Wrapper<T>(T content, Pagination pagination, Metadata metadata) {
    public Wrapper(T content) {
        this(content, Pagination.of(Page.empty()), Metadata.of());
    }

    public static <T> Wrapper<T> of(T content) {
        return new Wrapper<>(content);
    }

    public static <T> Wrapper<T> of(T content, Pagination pagination) {
        return new Wrapper<>(content, pagination, Metadata.of());
    }

    public static <T> Wrapper<T> of(T content, Metadata metadata) {
        return new Wrapper<>(content, Pagination.of(Page.empty()), metadata);
    }
}