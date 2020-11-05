package com.capgemini.rentalcars.repository;

import com.capgemini.rentalcars.model.Rental;
import com.capgemini.rentalcars.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT r FROM Rental r WHERE r.tenant = :tenant")
    Optional<Rental> findByTenant(@Param("tenant") final Tenant tenant);

}
