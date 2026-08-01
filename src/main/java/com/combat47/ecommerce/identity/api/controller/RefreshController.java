package com.combat47.ecommerce.identity.api.controller;

import com.combat47.ecommerce.identity.application.command.RefreshTokenCommand;
import com.combat47.ecommerce.identity.application.model.TokenResponse;
import com.combat47.ecommerce.identity.application.port.in.RefreshTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class RefreshController {

    private final RefreshTokenUseCase refreshTokenUseCase;


    public RefreshController(RefreshTokenUseCase refreshTokenUseCase) {
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenCommand command) {
        TokenResponse response = refreshTokenUseCase.refreshToken(command);
        return ResponseEntity.ok(response);
    }
}
