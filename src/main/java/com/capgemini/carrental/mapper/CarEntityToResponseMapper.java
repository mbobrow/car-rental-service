package com.capgemini.carrental.mapper;

import com.capgemini.carrental.dto.response.CarResponse;
import com.capgemini.carrental.model.Car;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service public class CarEntityToResponseMapper implements EntityResponseMapper<Car, CarResponse> {

    private final ModelMapper modelMapper;

    public CarEntityToResponseMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override public CarResponse mapToResponse(final Car entity) {
        final CarResponse carResponse = modelMapper.map(entity, CarResponse.class);
        carResponse.setIsRented(entity.isRented());
        return carResponse;
    }
}
