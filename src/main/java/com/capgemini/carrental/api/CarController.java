package com.capgemini.carrental.api;

import com.capgemini.carrental.model.Car;
import com.capgemini.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("api/v1/car")
@RestController
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(final CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<Car> getAllCars() {
        return this.carService.getAllCars();
    }

    @PostMapping
    public Car registerNewCar(@Valid @RequestBody final Car car) {
        return this.carService.addCar(car);
    }

    @GetMapping(path = "{id}")
    public Car getCar(@PathVariable("id") final Long id) {
        return this.carService.getCar(id);
    }

    @DeleteMapping(path = "{id}")
    public Car removeCar(@PathVariable("id") final Long id) {
        return this.carService.removeCar(id);
    }

    @PutMapping(path = "{id}")
    public Car updateCar(@PathVariable("id") final Long id, @Valid @RequestBody final Car updatedCar) {
        return this.carService.updateCar(id, updatedCar);
    }

    // TODO: should create isAvailableCar end-point?
}
