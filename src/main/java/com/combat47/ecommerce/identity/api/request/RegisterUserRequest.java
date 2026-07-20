package com.combat47.ecommerce.identity.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8,  max = 72)
        String password,

        @NotBlank
        @Size(min = 2,  max = 100)
        String firstName,

        @NotBlank
        @Size(min = 2,  max = 100)
        String lastName
) {
}
