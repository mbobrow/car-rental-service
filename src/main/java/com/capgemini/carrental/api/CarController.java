package com.capgemini.carrental.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.capgemini.carrental.dto.request.CarRequest;
import com.capgemini.carrental.dto.response.CarResponse;
import com.capgemini.carrental.mapper.CarEntityToResponseMapper;
import com.capgemini.carrental.mapper.CarRequestToEntityMapper;
import com.capgemini.carrental.model.Car;
import com.capgemini.carrental.service.CarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/car") @RestController public class CarController {

    private final CarService carService;
    private final CarRequestToEntityMapper carEntityMapper;
    private final CarEntityToResponseMapper carResponseMapper;

    @Autowired public CarController(
            final CarService carService,
            final CarRequestToEntityMapper carEntityMapper,
            final CarEntityToResponseMapper carResponseMapper) {
        this.carService = carService;
        this.carEntityMapper = carEntityMapper;
        this.carResponseMapper = carResponseMapper;
    }

    @GetMapping public List<CarResponse> getAllCars(@RequestParam(required = false) final Optional<Boolean> available) {
        return available.map(availableValue -> {
            if (Boolean.TRUE.equals(availableValue)) {
                return carService.getAllAvailableCars()
                        .stream()
                        .map(carResponseMapper::mapToResponse)
                        .collect(Collectors.toList());
            } else {
                return carService.getAllRentalCars()
                        .stream()
                        .map(carResponseMapper::mapToResponse)
                        .collect(Collectors.toList());
            }
        }).orElse(carService.getAllCars().stream().map(carResponseMapper::mapToResponse).collect(Collectors.toList()));
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public CarResponse registerNewCar(@Valid @RequestBody final CarRequest carRequest) {
        final Car car = carEntityMapper.mapToEntity(carRequest);
        final Car newCar = carService.addCar(car);
        return carResponseMapper.mapToResponse(newCar);
    }

    @GetMapping(path = "{id}") public CarResponse getCar(@PathVariable("id") final Long id) {
        final Car car = carService.getCar(id);
        return carResponseMapper.mapToResponse(car);
    }

    @DeleteMapping(path = "{id}") public CarResponse removeCar(@PathVariable("id") final Long id) {
        final Car removedCar = carService.removeCar(id);
        return carResponseMapper.mapToResponse(removedCar);
    }

    @PutMapping(path = "{id}")
    public CarResponse updateCar(@PathVariable("id") final Long id, @Valid @RequestBody final CarRequest updatedCarRequest) {
        final Car car = carEntityMapper.mapToEntity(updatedCarRequest);
        final Car updatedCar = carService.updateCar(id, car);
        return carResponseMapper.mapToResponse(updatedCar);
    }
}
