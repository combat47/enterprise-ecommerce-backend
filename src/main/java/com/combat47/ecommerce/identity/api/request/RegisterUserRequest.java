package com.combat47.ecommerce.identity.api.request;

public record RegisterUserRequest(
        String email,
        String password,
        String firstname,
        String lastname
) {
}
