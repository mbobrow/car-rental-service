package com.capgemini.carrental.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { CarNotFoundException.class })
    protected ResponseEntity<Object> carNotFound(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(exception,
                Objects.toString(exception.toString(), " "),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request);
    }

    @ExceptionHandler(value = { TenantNotFoundException.class })
    protected ResponseEntity<Object> tenantNotFound(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(exception,
                Objects.toString(exception.toString(), " "),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request);
    }

    @ExceptionHandler(value = { RentalNotFoundException.class })
    protected ResponseEntity<Object> rentalNotFound(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(exception,
                Objects.toString(exception.toString(), " "),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request);
    }

    @ExceptionHandler(value = { CarAlreadyRentedException.class })
    protected ResponseEntity<Object> carAlreadyRented(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(exception,
                Objects.toString(exception.toString(), " "),
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request);
    }

    @Override protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        final List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

}
