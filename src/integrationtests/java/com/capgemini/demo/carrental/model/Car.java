package com.capgemini.demo.carrental.model;

import com.capgemini.carrental.model.BodyType;
import com.capgemini.carrental.model.FuelType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
public class Car {

    private Long id;
    private String brand;
    private String model;
    @Enumerated(EnumType.STRING) private BodyType bodyType;
    @Enumerated(EnumType.STRING) private FuelType fuelType;
    private Integer year;
    private boolean isRented;
}
