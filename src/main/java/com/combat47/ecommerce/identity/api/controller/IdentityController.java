package com.combat47.ecommerce.identity.api.controller;


import com.combat47.ecommerce.identity.api.request.LoginRequest;
import com.combat47.ecommerce.identity.api.request.RegisterUserRequest;
import com.combat47.ecommerce.identity.api.response.RegisterUserResponse;
import com.combat47.ecommerce.identity.application.command.LoginCommand;
import com.combat47.ecommerce.identity.application.command.RegisterUserCommand;
import com.combat47.ecommerce.identity.application.model.TokenResponse;
import com.combat47.ecommerce.identity.application.port.in.LoginUseCase;
import com.combat47.ecommerce.identity.application.port.in.RegisterUserUseCase;
import com.combat47.ecommerce.identity.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/identity")
public class IdentityController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;

    public IdentityController(RegisterUserUseCase registerUserUseCase, LoginUseCase loginUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        RegisterUserCommand command =
                new RegisterUserCommand(
                        request.email(),
                        request.password(),
                        request.firstName(),
                        request.lastName());

        User user = registerUserUseCase.execute(command);

        RegisterUserResponse response =
                new RegisterUserResponse(
                        user.getId(),
                        "User registered successfully",
                        user.getCreatedAt()
                );
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response =
                loginUseCase.login(
                        new LoginCommand(
                                request.email(),
                                request.password()
                        )
                );

        return ResponseEntity.ok(response);
    }
}
