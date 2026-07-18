package com.combat47.ecommerce.identity.application.command;

public record RegisterUserCommand(
        String email,
        String password,
        String firstname,
        String lastname
) {
}
