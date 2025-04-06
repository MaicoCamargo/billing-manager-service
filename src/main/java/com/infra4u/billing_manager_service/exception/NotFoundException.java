package com.infra4u.billing_manager_service.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
