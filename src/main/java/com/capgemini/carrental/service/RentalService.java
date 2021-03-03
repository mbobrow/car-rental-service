package com.capgemini.carrental.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.capgemini.carrental.dto.request.CancelRentalCarsRequest;
import com.capgemini.carrental.dto.request.RentalRequest;
import com.capgemini.carrental.exception.RentalNotFoundException;
import com.capgemini.carrental.model.Car;
import com.capgemini.carrental.model.Rental;
import com.capgemini.carrental.model.Tenant;
import com.capgemini.carrental.repository.RentalRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional public class RentalService {

    private final RentalRepository rentalRepository;
    private final TenantService tenantService;
    private final CarService carService;

    @Autowired public RentalService(
            final RentalRepository rentalRepository, final TenantService tenantService, final CarService carService) {
        this.rentalRepository = rentalRepository;
        this.tenantService = tenantService;
        this.carService = carService;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental getRental(final Long rentalId) {
        return rentalRepository.findById(rentalId).orElseThrow(RentalNotFoundException::new);
    }

    public Rental getRentalByTenantId(final Long tenantId) {
        final Tenant tenant = tenantService.getTenant(tenantId);
        return rentalRepository.findByTenant(tenant).orElseThrow(RentalNotFoundException::new);
    }

    public Rental getRentalByCarId(final Long carId) {
        final Car car = carService.getCar(carId);
        return rentalRepository.findByCar(car).orElseThrow(RentalNotFoundException::new);
    }

    public Rental createRental(final RentalRequest rentalRequest) {
        // check requested Rental already exists
        final Tenant tenant = tenantService.getTenant(rentalRequest.getTenantId());
        rentalRepository.findByTenant(tenant).ifPresent(rental -> {
            rental.detachAllCars();
            rentalRepository.delete(rental);
        });
        // then create new Rental entity
        final Rental newRentalEntity = new Rental();
        newRentalEntity.setTenant(tenant);
        newRentalEntity.addCars(carService.getOnlyAvailableCarsById(rentalRequest.getCarIds()));
        newRentalEntity.setBeginningOfRental(rentalRequest.getBeginningOfRental());
        newRentalEntity.setEndOfRental(rentalRequest.getEndOfRental());
        return rentalRepository.save(newRentalEntity);
    }

    public Rental updateRental(final RentalRequest rentalRequest) {
        final Tenant tenant = tenantService.getTenant(rentalRequest.getTenantId());
        final Optional<Rental> rentalToUpdate = rentalRepository.findByTenant(tenant);
        return rentalToUpdate.map(rental -> {
            rental.setTenant(tenant);
            rental.addCars(carService.getOnlyAvailableCarsById(rentalRequest.getCarIds()));
            rental.setBeginningOfRental(rentalRequest.getBeginningOfRental());
            rental.setEndOfRental(rentalRequest.getEndOfRental());
            return rentalRepository.save(rental);
        }).orElseThrow(RentalNotFoundException::new);
    }

    public void cancelRentalById(final Long rentalId) {
        /*
            Find a rental to delete
            And detach all cars of it
         */
        rentalRepository.findById(rentalId).orElseThrow(RentalNotFoundException::new).detachAllCars();
        rentalRepository.deleteById(rentalId);
    }

    public void cancelRentalByTenantId(final Long tenantId) {
        // Find a rental to delete
        final Tenant tenant = tenantService.getTenant(tenantId);
        final Rental rentalToCancellation = rentalRepository.findByTenant(tenant).orElseThrow(RentalNotFoundException::new);
        // And detach all cars of it
        rentalToCancellation.detachAllCars();
        // Delete the rental itself
        rentalRepository.delete(rentalToCancellation);
    }

    public Rental cancelRentedCars(final CancelRentalCarsRequest cancelRentalCarsRequest) {
        // Find rental to modify rented cars
        final Tenant tenant = tenantService.getTenant(cancelRentalCarsRequest.getTenantId());
        final Rental rentalToModify = rentalRepository.findByTenant(tenant).orElseThrow(RentalNotFoundException::new);
        // Then detach all cars from cancellation request of found rental
        rentalToModify.detachCars(carService.getCarsById(cancelRentalCarsRequest.getCarIds()));
        // Save the modified rental
        return rentalRepository.save(rentalToModify);
    }

    private LocalDate getEarlierDate(final LocalDate current, final LocalDate requested) {
        return current.isBefore(requested) ? current : requested;
    }

    private LocalDate getLaterDate(final LocalDate current, final LocalDate requested) {
        return current.isAfter(requested) ? current : requested;
    }

}
