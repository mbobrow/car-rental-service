package com.capgemini.carrental.model;

import com.google.common.base.Enums;

public enum Gender {

    MALE, FEMALE, UNDEFINED;

    public static Gender getIfPresent(final String gender) {
        return Enums.getIfPresent(Gender.class, gender).or(UNDEFINED);
    }

}
