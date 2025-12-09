package com.capgemini.carrental.model;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity @Table(name = "TENANTS") @Data @NoArgsConstructor public class Tenant {

    @Id @GeneratedValue private Long id;

    private String name;

    @Enumerated(EnumType.STRING) private Gender gender;

    private Integer age;

    @ToString.Exclude @EqualsAndHashCode.Exclude @OneToOne(mappedBy = "tenant", cascade = CascadeType.ALL)
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
