package com.capgemini.carrental.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CarNotFoundException.class})
    protected ResponseEntity<Object> carNotFound(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                Objects.toString(exception.toString(), " "),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(value = {TenantNotFoundException.class})
    protected ResponseEntity<Object> tenantNotFound(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                Objects.toString(exception.toString(), " "),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(value = {RentalNotFoundException.class})
    protected ResponseEntity<Object> rentalNotFound(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                Objects.toString(exception.toString(), " "),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(value = {CarAlreadyRentedException.class})
    protected ResponseEntity<Object> carAlreadyRented(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                Objects.toString(exception.toString(), " "),
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request
        );
    }

    @ExceptionHandler(value = {InvalidRentalRequestException.class})
    protected ResponseEntity<Object> invalidRentalRequest(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                Objects.toString(exception.toString(), " "),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

}
