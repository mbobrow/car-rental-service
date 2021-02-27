package com.capgemini.carrental.dto.response;

import lombok.Data;

@Data public class CarResponse {

    private Long id;
    private String brand;
    private String model;
    private String bodyType;
    private String fuelType;
    private Integer year;
    private Boolean isRented;

}
