package com.infra4u.billing_manager_service.model.http.response;

import lombok.Getter;

@Getter
public class Wrapper<T> {

    private final T content;
    private final Metadata metadata;

    private Wrapper(final T content) {
        this.content = content;
        this.metadata = Metadata.of();
    }

    private Wrapper(final T content, final Metadata metadata) {
        this.content = content;
        this.metadata = metadata;
    }

    public static <T> Wrapper<T> of(final T content) {
        return new Wrapper<>(content);
    }

    public static <T> Wrapper<T> of(final T content, final Metadata metadata) {
        return new Wrapper<>(content, metadata);
    }
}