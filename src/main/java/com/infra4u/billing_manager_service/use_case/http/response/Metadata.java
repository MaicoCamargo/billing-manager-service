package com.infra4u.billing_manager_service.use_case.http.response;

import java.time.LocalDateTime;

public record Metadata(String message, LocalDateTime timestamp) {
    public Metadata() {
        this("Request processed successfully", LocalDateTime.now());
    }

    public Metadata(String message) {
        this(message, LocalDateTime.now());
    }

    public static Metadata of(String message) {
        return new Metadata(message);
    }

    public static Metadata of() {
        return new Metadata();
    }
}
