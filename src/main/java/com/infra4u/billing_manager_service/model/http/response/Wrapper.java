package com.infra4u.billing_manager_service.model.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.infra4u.billing_manager_service.use_case.http.response.Pagination;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Collection;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Wrapper<T> {

    private final T content;
    private final Metadata metadata;
    private final Pagination pagination;

    private Wrapper(final T content) {
        this.content = content;
        this.metadata = Metadata.of();
        this.pagination = null;
    }

    private Wrapper(final T content, final Metadata metadata) {
        this.content = content;
        this.metadata = metadata;
        this.pagination = null;
    }

    private Wrapper(final T content, final Pagination pagination) {
        this.content = content;
        this.metadata = Metadata.of();
        this.pagination = pagination;
    }

    public static <T> Wrapper<T> of(final T content) {
        return new Wrapper<>(content);
    }

    public static <T> Wrapper<T> of(final T content, final Metadata metadata) {
        return new Wrapper<>(content, metadata);
    }

    public static <T> Wrapper<Collection<T>> of(final Page<T> content) {
        return new Wrapper<>(content.getContent(), Pagination.of(content));
    }
}