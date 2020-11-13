package com.capgemini.carrental.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "RENTALS")
@Data
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Long.class
)
public class Rental {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_tenant", referencedColumnName = "id")
    private Tenant tenant;

    @JsonManagedReference
    @OneToMany(targetEntity = Car.class, mappedBy = "rental")
    private Set<Car> rentedCars = new HashSet<>();

    private LocalDate beginningOfRental;
    private LocalDate endOfRental;

    private boolean sameAsCurrent(final Tenant newTenant) {
        return Objects.equals(this.tenant, newTenant);
    }

    public void setTenant(final Tenant newTenant) {
        // prevent endless loop
        if (sameAsCurrent(newTenant)) {
            return;
        }
        // set new tenant
        final Tenant oldTenant = this.tenant;
        this.tenant = newTenant;
        // remove this rental from the old tenant
        if (oldTenant != null) {
            oldTenant.setRental(null);
        }
        // set this rental as new one in the new tenant
        if (newTenant != null) {
            newTenant.setRental(this);
        }
    }

    public void addRentedCar(final Car carToRent) {
        // prevent endless loop
        if (this.rentedCars.contains(carToRent)) {
            return;
        }
        // add new rented car
        this.rentedCars.add(carToRent);
        // set already rented car into this rental
        carToRent.setRental(this);
    }

    public void cancelRentedCar(final Car carToCancelRental) {
        // prevent endless loop
        if (!this.rentedCars.contains(carToCancelRental)) {
            return;
        }
        // remove the car from this rental
        this.rentedCars.remove(carToCancelRental);
        // remove this rental from the car
        carToCancelRental.setRental(null);
    }

    public void addCars(final Collection<Car> carsToRent) {
        if (carsToRent.stream().allMatch(Car::isRented)) {
            return;
        }
        for (Car carToRent : new HashSet<>(carsToRent)) {
            carToRent.setRental(this);
            this.rentedCars.add(carToRent);
        }
    }

    public void detachAllCars() {
        for (Car carToDetach : new HashSet<>(this.rentedCars)) {
            carToDetach.setRental(null);
        }
        this.rentedCars.clear();
    }

    public void detachCars(final Collection<Car> carsToDetach) {
        for (Car carToDetach : new HashSet<>(carsToDetach)) {
            if (this.rentedCars.remove(carToDetach)) {
                carToDetach.setRental(null);
            }
        }
    }
}

