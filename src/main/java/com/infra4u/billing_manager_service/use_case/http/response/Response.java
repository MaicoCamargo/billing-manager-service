package com.infra4u.billing_manager_service.use_case.http.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Response {
    public static <T> ResponseEntity<Wrapper<T>> ok(Wrapper<T> content) {
        return ResponseEntity.ok(content);
    }

    public static ResponseEntity<Wrapper<List<ErrorMessage>>> badRequest(List<ErrorMessage> errors) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Wrapper.of(errors, Metadata.of("Request processed with error")));
    }

    public static ResponseEntity<Void> deleted() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    public static ResponseEntity<Wrapper<ErrorMessage>> forbidden(ErrorMessage error) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Wrapper.of(error, Metadata.of("Request processed with error")));
    }

    public static ResponseEntity<Wrapper<ErrorMessage>> internalServerError(ErrorMessage error) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Wrapper.of(error, Metadata.of("Request processed with error")));
    }
}
