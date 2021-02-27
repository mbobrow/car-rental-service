package com.capgemini.carrental.exception;

public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException() {
    }

    public CarNotFoundException(final String message) {
        super(message);
    }

    public CarNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CarNotFoundException(final Throwable cause) {
        super(cause);
    }
}
