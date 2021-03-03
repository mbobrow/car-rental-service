package com.capgemini.carrental.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import com.capgemini.carrental.exception.CarAlreadyRentedException;
import com.capgemini.carrental.exception.CarNotFoundException;
import com.capgemini.carrental.model.Car;
import com.capgemini.carrental.repository.CarRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional public class CarService {

    private final CarRepository carRepository;

    @Autowired public CarService(final CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Collection<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Collection<Car> getAllAvailableCars() {
        return carRepository.findAll().stream().filter(Car::isAvailable).collect(Collectors.toList());
    }

    public Collection<Car> getAllRentalCars() {
        return carRepository.findAll().stream().filter(Car::isRented).collect(Collectors.toList());
    }

    public Car addCar(final Car newCar) {
        return carRepository.save(newCar);
    }

    public Car getCar(final Long id) {
        return carRepository.findById(id).orElseThrow(CarNotFoundException::new);
    }

    public Car removeCar(final Long id) {
        final Optional<Car> carToRemove = carRepository.findById(id);
        return carToRemove.map(car -> {
            carRepository.deleteById(id);
            return carToRemove.get();
        }).orElseThrow(CarNotFoundException::new);
    }

    public Car updateCar(final Long id, final Car updatedCar) {
        final Optional<Car> carToUpdate = carRepository.findById(id);
        return carToUpdate.map(car -> {
            updatedCar.setId(car.getId());
            return carRepository.save(updatedCar);
        }).orElseThrow(CarNotFoundException::new);
    }

    public Collection<Car> getOnlyAvailableCarsById(final Collection<Long> carIds) {
        final Collection<Car> availableCars = carRepository.findAllById(carIds)
                .stream()
                .filter(Car::isAvailable)
                .collect(Collectors.toList());
        if (availableCars.isEmpty()) {
            throw new CarAlreadyRentedException("Provided cars are already rented!");
        }
        return availableCars;
    }

    public Collection<Car> getOnlyRentedCarsById(final Collection<Long> carIds) {
        final Collection<Car> rentedCars = carRepository.findAllById(carIds)
                .stream()
                .filter(Car::isRented)
                .collect(Collectors.toList());
        if (rentedCars.isEmpty()) {
            throw new CarNotFoundException("Provided cars are not rented!");
        }
        return rentedCars;
    }

    public Collection<Car> getCarsById(final Collection<Long> carIds) {
        return carRepository.findAllById(carIds);
    }
}
