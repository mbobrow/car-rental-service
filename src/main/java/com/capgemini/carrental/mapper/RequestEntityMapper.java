package com.capgemini.carrental.mapper;

public interface RequestEntityMapper<R, E> {

    E mapToEntity(R request);

}
