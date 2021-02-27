package com.capgemini.carrental.exception;

public class CarAlreadyRentedException extends RuntimeException {

    public CarAlreadyRentedException() {
    }

    public CarAlreadyRentedException(final String message) {
        super(message);
    }

    public CarAlreadyRentedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CarAlreadyRentedException(final Throwable cause) {
        super(cause);
    }
}
