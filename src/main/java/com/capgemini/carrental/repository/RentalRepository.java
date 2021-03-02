package com.capgemini.carrental.repository;

import java.util.List;
import java.util.Optional;

import com.capgemini.carrental.model.Car;
import com.capgemini.carrental.model.Rental;
import com.capgemini.carrental.model.Tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Override @Query("SELECT DISTINCT r FROM Rental r JOIN FETCH r.tenant t JOIN FETCH r.rentedCars c ") List<Rental> findAll();

    @Override @Query("SELECT DISTINCT r FROM Rental r JOIN FETCH r.tenant t JOIN FETCH r.rentedCars c WHERE r.id = :id")
    Optional<Rental> findById(@NonNull final Long id);

    @Query("SELECT DISTINCT r FROM Rental r JOIN FETCH r.tenant t JOIN FETCH r.rentedCars c WHERE r.tenant = :tenant")
    Optional<Rental> findByTenant(
            @Param("tenant") final Tenant tenant);

    @Query("SELECT DISTINCT r FROM Rental r JOIN FETCH r.tenant t JOIN FETCH r.rentedCars c WHERE c = :car")
    Optional<Rental> findByCar(
            @Param("car") final Car car);
}
