package com.capgemini.carrental.repository;

import java.util.Optional;

import com.capgemini.carrental.model.Car;
import com.capgemini.carrental.model.Rental;
import com.capgemini.carrental.model.Tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT r FROM Rental r WHERE r.tenant = :tenant") Optional<Rental> findByTenant(
            @Param("tenant") final Tenant tenant);

    @Query("SELECT r FROM Rental r JOIN r.rentedCars c WHERE c = :car") Optional<Rental> findByCar(@Param("car") final Car car);
}
