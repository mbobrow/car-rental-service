package com.capgemini.carrental.mapper;

public interface EntityResponseMapper<E, R> {

    R mapToResponse(E entity);

}
