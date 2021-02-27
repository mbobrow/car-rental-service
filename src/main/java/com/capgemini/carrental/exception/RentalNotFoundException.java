package com.capgemini.carrental.exception;

public class RentalNotFoundException extends RuntimeException {

    public RentalNotFoundException() {
    }

    public RentalNotFoundException(final String message) {
        super(message);
    }

    public RentalNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RentalNotFoundException(final Throwable cause) {
        super(cause);
    }
}
