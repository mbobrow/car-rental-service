package com.capgemini.demo.carrental.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Car {

    private Long id;
    private String brand;
    private String model;
    private String bodyType;
    private String fuelType;
    private Integer year;
    private boolean isRented;

}
