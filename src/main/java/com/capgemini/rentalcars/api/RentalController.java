package com.capgemini.rentalcars.api;

import com.capgemini.rentalcars.dto.RentalRequest;
import com.capgemini.rentalcars.model.Rental;
import com.capgemini.rentalcars.model.Tenant;
import com.capgemini.rentalcars.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("api/v1/rental")
@RestController
public class RentalController {

    private final RentalService rentalService;

    @Autowired
    public RentalController(final RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public List<Rental> getAllRentals() {
        return this.rentalService.getAllRentals();
    }

    @GetMapping(path = "{id}")
    public Rental getRental(@PathVariable("id") final Long id) {
        return this.rentalService.getRental(id);
    }

    @PostMapping
    public Rental rentACar(@Valid @RequestBody final RentalRequest rentalRequest) {
        return this.rentalService.createOrUpdateRental(rentalRequest);
    }

    @DeleteMapping
    public void cancelRental(@RequestBody final Tenant tenant) {
        this.rentalService.cancelRental(tenant);
    }

}
