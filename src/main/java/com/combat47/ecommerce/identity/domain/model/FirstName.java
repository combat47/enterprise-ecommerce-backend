package com.combat47.ecommerce.identity.domain.model;

import com.combat47.ecommerce.identity.domain.exception.InvalidFirstNameException;

import java.util.Locale;

public record FirstName(String value) {

    public FirstName {
        if(value == null || value.isBlank()){
            throw new InvalidFirstNameException("First Name cannot be null or empty");

        }

        value = normalize(value);
    }

    private static String normalize(String firstName) {
        return firstName
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}
