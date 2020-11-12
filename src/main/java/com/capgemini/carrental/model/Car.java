package com.capgemini.carrental.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "CARS")
@Data
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Long.class
)
public class Car {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @Column(name = "BODY_TYPE")
    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    @Column(name = "FUEL_TYPE")
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    private int year;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(targetEntity = Rental.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_rental", referencedColumnName = "id")
    private Rental rental;

    public boolean isRented() {
        return rental != null && rental.getId() != null;
    }

    @JsonIgnore
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
