package com.capgemini.carrental.dto.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data public class CancelRentalCarsRequest {

    @NotNull private Long tenantId;
    @NotEmpty private List<Long> carIds;
}
