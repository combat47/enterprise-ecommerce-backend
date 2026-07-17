package com.combat47.ecommerce.identity.domain.model;

import com.combat47.ecommerce.identity.domain.exception.InvalidEmailException;

import java.util.Locale;
import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public Email {
        if (value == null || value.isBlank()) {
            throw new InvalidEmailException("Email cannot be null or empty");
        }

        value = normalize(value);

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new InvalidEmailException("Invalid Email format");
        }
    }

    private static String normalize(String email) {
        return email
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}