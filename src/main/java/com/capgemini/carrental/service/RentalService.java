package com.capgemini.carrental.service;

import com.capgemini.carrental.dto.RentalRequest;
import com.capgemini.carrental.exception.*;
import com.capgemini.carrental.model.Car;
import com.capgemini.carrental.model.Rental;
import com.capgemini.carrental.model.Tenant;
import com.capgemini.carrental.repository.CarRepository;
import com.capgemini.carrental.repository.RentalRepository;
import com.capgemini.carrental.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental getRental(final Long rentalId) {
        return rentalRepository.findById(rentalId).orElseThrow(RentalNotFoundException::new);
    }

    /**
     * TODO: consider improvement
     * - reduce amount of repo calls
     * - findByTenant from repo, not request
     * - at first valid request then rental should be created by repos, not request data
     * <p>
     * Concept:
     * - check existence of tenant then cars
     * - check every requested car is not already rented
     * - find rental by tenant (for create or update purposes)
     * - create new rental or update existing one based on data from repo (request let us know what do we want to find in repo for rental purposes)
     */
    public Rental createOrUpdateRental(final RentalRequest rentalRequest) {
        /*
            Find rental by tenant from provided rental request.
            If rental is found it is updated otherwise the new one is created.
         */
        final Optional<Rental> optionalRental = rentalRepository.findByTenant(rentalRequest.getRental().getTenant());
        return optionalRental
                .map(rental -> updateRental(rental, rentalRequest))
                .orElseGet(() -> createRental(rentalRequest));
    }

    private Rental updateRental(final Rental rentalToUpdate, final RentalRequest rentalRequest) {
        final Rental requestedRental = rentalRequest.getRental();
        rentalToUpdate.addCars(findCars(requestedRental.getRentedCars()));
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
        newRental.setTenant(findTenant(requestedRental.getTenant()));
        newRental.addCars(findCars(requestedRental.getRentedCars()));
        newRental.setBeginningOfRental(requestedRental.getBeginningOfRental());
        newRental.setEndOfRental(requestedRental.getEndOfRental());
        final Rental savedRental = rentalRepository.save(newRental);
        // update car repo as well
        carRepository.saveAll(savedRental.getRentedCars());
        return savedRental;
    }

    private Collection<Car> findCars(final Set<Car> requestedCars) {
        final Collection<Car> foundCars = carRepository.findAllById(requestedCars.stream()
                .map(Car::getId)
                .collect(Collectors.toList())
        );
        if (foundCars.isEmpty()) {
            throw new CarNotFoundException("Requested cars are not found in the Car-Rental");
        }
        foundCars.forEach(car -> {
            if (car.isRented()) {
                throw new CarAlreadyRentedException(car.toString()
                        .concat(" is already rented by Tenant with id:")
                        .concat(car.getRental().getTenant().getId().toString())
                );
            }
        });
        return foundCars;
    }

    private Tenant findTenant(final Tenant tenant) {
        final Optional<Tenant> optionalTenant = tenantRepository.findById(tenant.getId());
        return optionalTenant.orElseThrow(
                () -> new TenantNotFoundException(tenant.toString()
                        .concat(". At first new Tenant has to be registered! ")
                )
        );
    }

    // TODO: to be removed
    private void checkTenantExistence(final Tenant requestedTenant) {
        final ExampleMatcher tenantMatcher = ExampleMatcher.matching()
                .withIgnorePaths("rental")
                .withIgnoreCase();
        if (!tenantRepository.exists(Example.of(requestedTenant, tenantMatcher))
                && !tenantRepository.existsById(requestedTenant.getId())) {
            throw new TenantNotFoundException(requestedTenant.toString()
                    .concat(". At first new Tenant has to be registered! ")
            );
        }
    }

    // TODO: to be removed
    private void checkRentalRequest(final RentalRequest rentalRequest) {
        // Check if tenant from provided rental request already exists in tenant repository
        checkTenantExistence(rentalRequest.getRental().getTenant());
        /*
            Check if cars from provided rental request already exists in cars repository
            Then check if they are available to rent.
         */
        checkCarsExistence(rentalRequest);
    }

    // TODO: to be removed
    private void checkCarsExistence(final RentalRequest rentalRequest) {
        final Set<Car> requestedCars = rentalRequest.getRental().getRentedCars();
        // Check if rental request contains at least one car
        if (requestedCars.isEmpty()) {
            throw new InvalidRentalRequestException("Rental request does not contain any cars. "
                    .concat("At least one car is needed! ")
            );
        }
        /*
            Check if car rental owns the cars from provided rental request.
            Then check car is available to rent.
         */
        final ExampleMatcher carMatcher = ExampleMatcher.matching()
                .withIgnorePaths("rental", "available")
                .withIgnoreCase();
        requestedCars.forEach(
                car -> {
                    if (carRepository.exists(Example.of(car, carMatcher))
                            || carRepository.existsById(car.getId())) {
                        if (!car.isAvailable()) { // TODO: car has to be from repo!
                            throw new CarAlreadyRentedException(car.toString()
                                    .concat(" is already rented by Tenant with id:")
                                    .concat(rentalRequest.getRental().getTenant().getId().toString())
                            );
                        }
                    } else {
                        throw new CarNotFoundException(car.toString()
                                .concat(". Car-Rental does not own this car. ")
                        );
                    }
                }
        );
    }

    // TODO: to be removed
    private void checkCarsAvailability(final RentalRequest rentalRequest) {
        final Rental requestedRental = rentalRequest.getRental();
        final List<Car> foundCars = carRepository.findAllById(
                requestedRental.getRentedCars().stream()
                        .map(Car::getId)
                        .collect(Collectors.toList())
        );
        requestedRental.getRentedCars()
                .forEach(requestedCar -> {
                    if (!foundCars.contains(requestedCar)) {
                        throw new CarNotFoundException(requestedCar.toString()
                                .concat(". Car-Rental does not own this car. ")
                        );
                    }
                    carRepository.findById(requestedCar.getId())
                            .filter(Car::isAvailable)
                            .orElseThrow(() -> new CarAlreadyRentedException(requestedCar.toString()
                                    .concat(" is already rented by Tenant with id:")
                                    .concat(requestedRental.getTenant().getId().toString()))
                            );
                });
    }

    public void cancelRental(final Tenant tenant) {
        // Find a rental to delete
        final Optional<Rental> optionalRentalToDelete = rentalRepository.findByTenant(tenant);
        // Delete the rental itself
        rentalRepository.delete(optionalRentalToDelete.orElseThrow(RentalNotFoundException::new));
        // Then detach cars from found rental
        detachCars(optionalRentalToDelete.get());
    }

    private Rental detachCars(final Rental rentalToDelete) {
        for (Car car : new HashSet<>(rentalToDelete.getRentedCars())) {
            car.setRental(null);
        }
        return rentalToDelete;
    }

}
