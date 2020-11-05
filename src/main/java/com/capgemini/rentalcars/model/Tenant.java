package com.capgemini.rentalcars.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Entity
@Table(name = "TENANTS")
@Data
@NoArgsConstructor
public class Tenant {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Positive
    private int age;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "tenant")
    private Rental rental;

    private boolean sameAsCurrent(final Rental newRental) {
        return Objects.equals(this.rental, newRental);
    }

    public void setRental(final Rental newRental) {
        // prevent endless loop
        if (sameAsCurrent(newRental)) {
            return;
        }
        // set new Rental
        final Rental oldRental = this.rental;
        this.rental = newRental;
        // remove this tenant from the old rental
        if (oldRental != null) {
            oldRental.setTenant(null);
        }
        // set this tenant as new one in the new rental
        if (newRental != null) {
            newRental.setTenant(this);
        }
    }

}
