package com.capgemini.carrental.dto;

import com.capgemini.carrental.model.Car;
import com.capgemini.carrental.model.Tenant;
import lombok.Data;

import java.util.List;

@Data
public class CancelRentalCarsRequest {

    private Tenant tenant;
    private List<Car> cars;
}
