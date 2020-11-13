package com.capgemini.carrental.service;

import com.capgemini.carrental.dto.CancelRentalCarsRequest;
import com.capgemini.carrental.dto.RentalRequest;
import com.capgemini.carrental.exception.CarAlreadyRentedException;
import com.capgemini.carrental.exception.CarNotFoundException;
import com.capgemini.carrental.exception.RentalNotFoundException;
import com.capgemini.carrental.exception.TenantNotFoundException;
import com.capgemini.carrental.model.Car;
import com.capgemini.carrental.model.Rental;
import com.capgemini.carrental.model.Tenant;
import com.capgemini.carrental.repository.CarRepository;
import com.capgemini.carrental.repository.RentalRepository;
import com.capgemini.carrental.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
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
        return rentalRepository.findById(rentalId)
                .orElseThrow(RentalNotFoundException::new);
    }

    public Rental createOrUpdateRental(final RentalRequest rentalRequest) {
        /*
            Find rental by tenant from provided rental request.
            If rental is found it is updated otherwise the new one is created.
         */
        return rentalRepository.findByTenant(rentalRequest.getRental().getTenant())
                .map(rental -> updateRental(rental, rentalRequest))
                .orElseGet(() -> createRental(rentalRequest));
    }

    private Rental updateRental(final Rental rentalToUpdate, final RentalRequest rentalRequest) {
        final Rental requestedRental = rentalRequest.getRental();
        rentalToUpdate.addCars(findAvailableCars(requestedRental.getRentedCars()));
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
        newRental.addCars(findAvailableCars(requestedRental.getRentedCars()));
        newRental.setBeginningOfRental(requestedRental.getBeginningOfRental());
        newRental.setEndOfRental(requestedRental.getEndOfRental());
        final Rental savedRental = rentalRepository.save(newRental);
        // update car repo as well
        carRepository.saveAll(savedRental.getRentedCars());
        return savedRental;
    }

    private Collection<Car> findCars(final Collection<Car> carsToFind) {
        final Collection<Car> foundCars = carRepository.findAllById(carsToFind.stream()
                .map(Car::getId)
                .collect(Collectors.toList())
        );
        if (foundCars.isEmpty()) {
            throw new CarNotFoundException("Requested cars are not found in the Car-Rental");
        }
        return foundCars;
    }

    private Collection<Car> findAvailableCars(final Collection<Car> requestedCars) { // TODO: should be findAll(Example<S>)?
        final Collection<Car> foundCars = findCars(requestedCars);
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
        // TODO: should be findOne(Example<S>)?
        return tenantRepository.findById(tenant.getId())
                .orElseThrow(() ->
                        new TenantNotFoundException(tenant.toString()
                                .concat(". At first new Tenant has to be registered! ")
                        )
                );
    }

    public void cancelRental(final Long rentalId) {
        rentalRepository.findById(rentalId)
                .orElseThrow(RentalNotFoundException::new)
                .detachAllCars();
        rentalRepository.deleteById(rentalId);
    }

    public void cancelRental(final Tenant tenant) {
        // Find a rental to delete
        final Rental rentalToCancellation = rentalRepository.findByTenant(tenant)
                .orElseThrow(RentalNotFoundException::new);
        // Then detach all cars of found rental
        rentalToCancellation.detachAllCars();
        // Delete the rental itself
        rentalRepository.delete(rentalToCancellation);
    }

    public Rental cancelRentedCars(final CancelRentalCarsRequest cancelRentalCarsRequest) {
        // Find rental to modify rented cars
        final Rental rentalToModify = rentalRepository.findByTenant(cancelRentalCarsRequest.getTenant())
                .orElseThrow(RentalNotFoundException::new);
        // Then detach all cars from cancellation request of found rental
        rentalToModify.detachCars(findCars(cancelRentalCarsRequest.getCars()));
        // Save the modified rental
        return rentalRepository.save(rentalToModify);
    }

}
