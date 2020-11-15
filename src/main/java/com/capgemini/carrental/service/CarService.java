package com.capgemini.carrental.service;

import com.capgemini.carrental.exception.CarNotFoundException;
import com.capgemini.carrental.model.Car;
import com.capgemini.carrental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getAllAvailableCars() {
        return carRepository.findAll().stream()
                .filter(Car::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Car> getAllRentalCars() {
        return carRepository.findAll().stream()
                .filter(Car::isRented)
                .collect(Collectors.toList());
    }

    public Car addCar(final Car newCar) {
        return carRepository.save(newCar);
    }

    public Car getCar(final Long id) {
        return carRepository.findById(id).orElseThrow(CarNotFoundException::new);
    }

    public Car removeCar(final Long id) {
        final Optional<Car> carToRemove = carRepository.findById(id);
        return carToRemove
                .map(car -> {
                    carRepository.deleteById(id);
                    return carToRemove.get();
                })
                .orElseThrow(CarNotFoundException::new);
    }

    public Car updateCar(final Long id, final Car updatedCar) {
        final Optional<Car> carToUpdate = carRepository.findById(id);
        return carToUpdate
                .map(car -> {
                    updatedCar.setId(car.getId());
                    return carRepository.save(updatedCar);
                })
                .orElseThrow(CarNotFoundException::new);
    }
}
