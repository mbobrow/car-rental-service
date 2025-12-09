package com.capgemini.carrental.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data public class RentalRequest {

    @NotNull
    private Long tenantId;
    @NotEmpty
    private List<Long> carIds;
    @FutureOrPresent
    private LocalDate beginningOfRental;
    @FutureOrPresent private LocalDate endOfRental;

}
