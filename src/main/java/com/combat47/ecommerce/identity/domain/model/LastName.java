package com.combat47.ecommerce.identity.domain.model;

import com.combat47.ecommerce.identity.domain.exception.InvalidLastNameException;

import java.util.Locale;

public record LastName(String value) {

    public LastName {
        if (value == null || value.isBlank()) {
            throw new InvalidLastNameException("Last Name cannot be null or empty");

        }

        value = normalize(value);
    }

    private static String normalize(String lastName) {
        return lastName
                .trim()
                .toLowerCase(Locale.ROOT);
    }

}
