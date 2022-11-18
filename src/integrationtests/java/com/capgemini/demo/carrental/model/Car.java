package com.capgemini.demo.carrental.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Data
@Getter
@Setter
public class Car {
    //response
    private String id;
    //request
    //common
    private String brand;
    private String model;
    private String bodyType;
    private String fuelType;
    private String year;
    private String isRented;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return Objects.equals(getBrand(), car.getBrand()) && Objects.equals(getModel(), car.getModel()) && Objects.equals(getBodyType(), car.getBodyType()) && Objects.equals(getFuelType(), car.getFuelType()) && Objects.equals(getYear(), car.getYear()) && Objects.equals(getIsRented(), car.getIsRented());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBrand(), getModel(), getBodyType(), getFuelType(), getYear(), getIsRented());
    }
}
