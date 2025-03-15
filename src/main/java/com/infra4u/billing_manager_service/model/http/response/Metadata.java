package com.infra4u.billing_manager_service.model.http.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Metadata {
    private final String message;
    private final LocalDateTime timestamp;

    public Metadata() {
        this.message = "Request processed successfully";
        this.timestamp = LocalDateTime.now();
    }

    public Metadata(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static Metadata of(final String message) {
        return new Metadata(message);
    }

    public static Metadata of() {
        return new Metadata();
    }
}
