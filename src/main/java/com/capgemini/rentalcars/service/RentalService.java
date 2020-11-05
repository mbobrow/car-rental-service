package com.capgemini.rentalcars.service;

import com.capgemini.rentalcars.dto.RentalRequest;
import com.capgemini.rentalcars.exception.*;
import com.capgemini.rentalcars.model.Car;
import com.capgemini.rentalcars.model.Rental;
import com.capgemini.rentalcars.model.Tenant;
import com.capgemini.rentalcars.repository.CarRepository;
import com.capgemini.rentalcars.repository.RentalRepository;
import com.capgemini.rentalcars.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RentalService {

    private final RentalRepository rentalRepository;
    private final TenantRepository tenantRepository;
    private final CarRepository carRepository;

    @Autowired
    public RentalService(final RentalRepository rentalRepository, final TenantRepository tenantRepository, final CarRepository carRepository) {
        this.rentalRepository = rentalRepository;
        this.tenantRepository = tenantRepository;
        this.carRepository = carRepository;
    }

    private Rental updateRental(final Rental rentalToUpdate, final RentalRequest rentalRequest) {
        final Rental requestedRental = rentalRequest.getRental();
        rentalToUpdate.setRentedCars(requestedRental.getRentedCars());
        rentalToUpdate.setBeginningOfRental(requestedRental.getBeginningOfRental());
        rentalToUpdate.setEndOfRental(requestedRental.getEndOfRental());
        // update car repo as well
        carRepository.saveAll(rentalToUpdate.getRentedCars());
        return rentalRepository.save(rentalToUpdate);
    }

    private Rental createRental(final RentalRequest rentalRequest) {
        final Rental requestedRental = rentalRequest.getRental();
        // then create new Rental
        final Rental newRental = new Rental();
        newRental.setTenant(requestedRental.getTenant());
        newRental.setRentedCars(requestedRental.getRentedCars());
        newRental.setBeginningOfRental(requestedRental.getBeginningOfRental());
        newRental.setEndOfRental(requestedRental.getEndOfRental());
        final Rental savedRental = rentalRepository.save(newRental);
        // update car repo as well
        carRepository.saveAll(savedRental.getRentedCars());
        return savedRental;
    }

    private void checkTenantExistence(final Tenant requestedTenant) {
        final ExampleMatcher tenantMatcher = ExampleMatcher.matching()
                .withIgnorePaths("rental")
                .withIgnoreCase();
        if (!tenantRepository.exists(Example.of(requestedTenant, tenantMatcher))
                && !tenantRepository.existsById(requestedTenant.getId())) {
            throw new TenantNotFoundException(requestedTenant.toString()
                    .concat(". At first new Tenant has to be registered!")
            );
        }
    }

    private void checkRentalRequest(RentalRequest rentalRequest) {
        if (rentalRequest.getRental().getRentedCars().isEmpty()) {
            throw new InvalidRentalRequestException("Rental request does not contain cars. "
                    .concat("At least one car is needed! ")
            );
        }
    }

    private void checkCarsAvailability(final RentalRequest rentalRequest) {
        final Rental requestedRental = rentalRequest.getRental();
        final List<Car> foundedCars = carRepository.findAllById(
                requestedRental.getRentedCars().stream()
                        .map(Car::getId)
                        .collect(Collectors.toList())
        );
        requestedRental.getRentedCars()
                .forEach(requestedCar -> {
                    if (!foundedCars.contains(requestedCar)) {
                        throw new CarNotFoundException(requestedCar.toString()
                                .concat(". Car-Rental does not own this car.")
                        );
                    }
                    carRepository.findById(requestedCar.getId())
                            .filter(car -> car.getRental() == null)
                            .orElseThrow(() -> new CarAlreadyRentedException(requestedCar.toString()
                                    .concat(" is already rented by Tenant with id:")
                                    .concat(requestedRental.getTenant().getId().toString()))
                            );
                });
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental getRental(final Long rentalId) {
        return rentalRepository.findById(rentalId).orElseThrow(RentalNotFoundException::new);
    }

    public Rental createOrUpdateRental(final RentalRequest rentalRequest) {
        // Check if tenant from provided rental request already exists in tenant repository
        final Tenant requestedTenant = rentalRequest.getRental().getTenant();
        checkTenantExistence(requestedTenant);
        // Check if rental request contains at least one car
        checkRentalRequest(rentalRequest);
        /*
            Check if car rental owns the cars from provided rental request.
            Then check car is available to rent.
         */
        checkCarsAvailability(rentalRequest);
        /*
            Find rental by tenant from provided rental request.
            If rental is found it is updated otherwise the new one is created.
         */
        final Optional<Rental> optionalRental = rentalRepository.findByTenant(requestedTenant);
        return optionalRental
                .map(rental -> updateRental(rental, rentalRequest))
                .orElseGet(() -> createRental(rentalRequest));
    }

    public void cancelRental(final Tenant tenant) {
        // Check if tenant from provided rental request already exists in tenant repository
        checkTenantExistence(tenant);
        // Check if rental is found
        final Optional<Rental> optionalRentalToDelete = rentalRepository.findByTenant(tenant);
        if (!optionalRentalToDelete.isPresent()) {
            throw new RentalNotFoundException();
        }
        // Then detach cars from rental
        final Rental rentalToDelete = optionalRentalToDelete.get();
        for (Car car : new HashSet<>(rentalToDelete.getRentedCars())) {
            car.setRental(null);
        }
        // And delete rental itself
        rentalRepository.delete(rentalToDelete);
    }

}
