package com.combat47.ecommerce.identity.application.command;

public record LoginCommand(
        String email,
        String password
) {
}
