package com.capgemini.rentalcars.service;

import com.capgemini.rentalcars.exception.CarNotFoundException;
import com.capgemini.rentalcars.repository.CarRepository;
import com.capgemini.rentalcars.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public Car addCar(final Car newCar) {
        return carRepository.save(newCar);
    }

    public Car getCar(final Long id) {
        return carRepository.findById(id).orElseThrow(CarNotFoundException::new);
    }

    public Car removeCar(final Long id) {
        final Optional<Car> carToBeRemoved = carRepository.findById(id);
        return carToBeRemoved
                .map(car -> {
                    carRepository.deleteById(id);
                    return carToBeRemoved.get();
                })
                .orElseThrow(CarNotFoundException::new);
    }

    public Car updateCar(final Long id, final Car updatedCar) {
        final Optional<Car> carToBeUpdated = carRepository.findById(id);
        return carToBeUpdated
                .map(car -> {
                    updatedCar.setId(car.getId());
                    return carRepository.save(updatedCar);
                })
                .orElseThrow(CarNotFoundException::new);
    }
}
