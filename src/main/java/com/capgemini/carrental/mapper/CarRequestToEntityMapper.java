package com.capgemini.carrental.mapper;

import com.capgemini.carrental.dto.request.CarRequest;
import com.capgemini.carrental.model.Car;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service public class CarRequestToEntityMapper implements RequestEntityMapper<CarRequest, Car> {

    private final ModelMapper modelMapper;

    public CarRequestToEntityMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override public Car mapToEntity(final CarRequest request) {
        final Car car = modelMapper.map(request, Car.class);
        car.setBodyType(request.getBodyTypeConverted());
        car.setFuelType(request.getFuelTypeConverted());
        return car;
    }
}
