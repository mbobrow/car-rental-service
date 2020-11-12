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
