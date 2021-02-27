package com.capgemini.carrental.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity @Table(name = "CARS") @Data @NoArgsConstructor public class Car {

    @Id @GeneratedValue private Long id;

    private String brand;

    private String model;

    @Column(name = "BODY_TYPE") @Enumerated(EnumType.STRING) private BodyType bodyType;

    @Column(name = "FUEL_TYPE") @Enumerated(EnumType.STRING) private FuelType fuelType;

    private Integer year;

    @ToString.Exclude @EqualsAndHashCode.Exclude @ManyToOne(targetEntity = Rental.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_rental", referencedColumnName = "id") private Rental rental;

    public boolean isRented() {
        return rental != null && rental.getId() != null;
    }

    public boolean isAvailable() {
        return rental == null || rental.getId() == null;
    }

    private boolean sameAsCurrent(final Rental newRental) {
        return Objects.equals(this.rental, newRental);
    }

    public void setRental(final Rental newRental) {
        // prevent endless loop
        if (sameAsCurrent(newRental)) {
            return;
        }
        // set new rental
        final Rental oldRental = this.rental;
        this.rental = newRental;
        // remove this car from the old rental
        if (oldRental != null) {
            oldRental.cancelRentedCar(this);
        }
        // set this car as new one in the new rental
        if (newRental != null) {
            newRental.addRentedCar(this);
        }
    }
}
