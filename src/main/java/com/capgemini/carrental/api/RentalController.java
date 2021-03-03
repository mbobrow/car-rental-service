package com.capgemini.carrental.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.capgemini.carrental.dto.request.CancelRentalCarsRequest;
import com.capgemini.carrental.dto.request.RentalRequest;
import com.capgemini.carrental.dto.response.RentalResponse;
import com.capgemini.carrental.mapper.RentalEntityToResponseMapper;
import com.capgemini.carrental.model.Rental;
import com.capgemini.carrental.service.RentalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/rental") @RestController public class RentalController {

    private final RentalService rentalService;
    private final RentalEntityToResponseMapper rentalResponseMapper;

    @Autowired public RentalController(
            final RentalService rentalService, final RentalEntityToResponseMapper rentalResponseMapper) {
        this.rentalService = rentalService;
        this.rentalResponseMapper = rentalResponseMapper;
    }

    @GetMapping(path = "/search") @ResponseBody public List<RentalResponse> getAllRentals() {
        return rentalService.getAllRentals().stream().map(rentalResponseMapper::mapToResponse).collect(Collectors.toList());
    }

    @GetMapping(path = "/search/{id}") @ResponseBody public RentalResponse getRental(@PathVariable("id") final Long id) {
        final Rental rental = rentalService.getRental(id);
        return rentalResponseMapper.mapToResponse(rental);
    }

    @GetMapping(path = "/search/byTenant/{tenantId}") @ResponseBody
    public RentalResponse getRentalByTenant(@PathVariable("tenantId") final Long tenantId) {
        final Rental rental = rentalService.getRentalByTenantId(tenantId);
        return rentalResponseMapper.mapToResponse(rental);
    }

    @GetMapping(path = "/search/byCar/{carId}") @ResponseBody
    public RentalResponse getRentalByCar(@PathVariable("carId") final Long carId) {
        final Rental rental = rentalService.getRentalByCarId(carId);
        return rentalResponseMapper.mapToResponse(rental);
    }

    @PostMapping @ResponseBody @ResponseStatus(HttpStatus.CREATED)
    public RentalResponse createNewRental(@Valid @RequestBody final RentalRequest rentalRequest) {
        final Rental newRental = rentalService.createRental(rentalRequest);
        return rentalResponseMapper.mapToResponse(newRental);
    }

    @PutMapping @ResponseBody public RentalResponse addCarsToExistingRental(
            @Valid @RequestBody final RentalRequest updateRequest) {
        final Rental updatedRental = rentalService.updateRental(updateRequest);
        return rentalResponseMapper.mapToResponse(updatedRental);
    }

    @DeleteMapping(path = "/remove/{rentalId}") public void cancelRental(@PathVariable("rentalId") final Long rentalId) {
        rentalService.cancelRentalById(rentalId);
    }

    @DeleteMapping(path = "/remove/byTenant/{tenantId}")
    public void cancelRentalByTenantId(@PathVariable("tenantId") final Long tenantId) {
        rentalService.cancelRentalByTenantId(tenantId);
    }

    @PatchMapping @ResponseBody
    public RentalResponse cancelRentedCars(@Valid @RequestBody final CancelRentalCarsRequest cancelRentalCarsRequest) {
        final Rental rentalAfterCancellation = rentalService.cancelRentedCars(cancelRentalCarsRequest);
        return rentalResponseMapper.mapToResponse(rentalAfterCancellation);
    }
}
