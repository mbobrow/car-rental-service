package com.capgemini.carrental.model;

import com.google.common.base.Enums;

public enum FuelType {

    PETROL, DIESEL, HYBRID;

    public static FuelType getIfPresent(final String type) {
        return Enums.getIfPresent(FuelType.class, type).orNull();
    }

}
