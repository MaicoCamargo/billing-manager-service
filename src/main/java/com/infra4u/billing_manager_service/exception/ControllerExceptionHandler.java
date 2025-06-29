package com.infra4u.billing_manager_service.exception;

import com.infra4u.billing_manager_service.model.http.response.ErrorMessage;
import com.infra4u.billing_manager_service.model.http.response.Metadata;
import com.infra4u.billing_manager_service.model.http.response.Wrapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Hidden
@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(UnprocessableEntityException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public Wrapper<ErrorMessage> unprocessableEntityException(UnprocessableEntityException unprocessableEntityException, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                unprocessableEntityException.getMessage(),
                request.getDescription(false));

        return Wrapper.of(message, Metadata.of("Request processed with error"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Wrapper<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                ex.getMessage(),
                request.getDescription(false));
        log.error(ex.getMessage(), ex);
        return Wrapper.of(message, Metadata.of("Request processed with error"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Wrapper<Collection<ErrorMessage>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorMessage> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(new ErrorMessage("erro campo: " + error.getField(), error.getDefaultMessage()))
        );

        return Wrapper.of(errors, Metadata.of("Request processed with errors"));
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public Wrapper<ErrorMessage> forbiddenException(ForbiddenException forbiddenException, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                forbiddenException.getMessage(),
                request.getDescription(false));

        return Wrapper.of(message, Metadata.of("Request processed with error"));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Wrapper<ErrorMessage> handleNotFoundException(NotFoundException notFoundException, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                notFoundException.getMessage(),
                request.getDescription(false));

        return Wrapper.of(message, Metadata.of("Request processed with error"));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Wrapper<ErrorMessage> handleNoSuchElementException(NoResourceFoundException noResourceFoundException, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                noResourceFoundException.getMessage(),
                request.getDescription(false));

        return Wrapper.of(message, Metadata.of("Request processed with error"));
    }
}
