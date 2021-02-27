package com.capgemini.carrental.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Data;

import com.capgemini.carrental.model.BodyType;
import com.capgemini.carrental.model.FuelType;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data public class CarRequest {

    @NotBlank private String brand;
    @NotBlank private String model;
    private String bodyType;
    private String fuelType;
    @Positive private Integer year;

    @JsonIgnore public BodyType getBodyTypeConverted() {
        return BodyType.getIfPresent(bodyType);
    }

    @JsonIgnore public FuelType getFuelTypeConverted() {
        return FuelType.getIfPresent(fuelType);
    }

}
