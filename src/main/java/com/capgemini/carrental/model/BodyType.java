package com.capgemini.carrental.model;

import com.google.common.base.Enums;

public enum BodyType {

    SEDAN, HATCHBACK, COUPE, CONVERTIBLE, COMBI, SUV;

    public static BodyType getIfPresent(final String type) {
        return Enums.getIfPresent(BodyType.class, type).orNull();
    }
}
