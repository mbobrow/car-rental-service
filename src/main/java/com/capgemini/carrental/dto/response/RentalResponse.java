package com.capgemini.carrental.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data public class RentalResponse {

    private Long id;
    private TenantResponse tenant;
    private List<CarResponse> rentedCars;
    private LocalDate beginningOfRental;
    private LocalDate endOfRental;

}
