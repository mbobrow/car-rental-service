package com.capgemini.carrental.exception;

public class TenantNotFoundException extends RuntimeException {

    public TenantNotFoundException() {
    }

    public TenantNotFoundException(final String message) {
        super(message);
    }

    public TenantNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TenantNotFoundException(final Throwable cause) {
        super(cause);
    }
}
