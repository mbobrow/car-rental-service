package com.capgemini.carrental.dto.request;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data public class RentalRequest {

    @NotNull private Long tenantId;
    @NotEmpty private List<Long> carIds;
    @FutureOrPresent private LocalDate beginningOfRental;
    @FutureOrPresent private LocalDate endOfRental;

}
