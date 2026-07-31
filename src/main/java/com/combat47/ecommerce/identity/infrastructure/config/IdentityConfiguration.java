package com.combat47.ecommerce.identity.infrastructure.config;


import com.combat47.ecommerce.identity.application.port.in.LoginUseCase;
import com.combat47.ecommerce.identity.application.port.in.RegisterUserUseCase;
import com.combat47.ecommerce.identity.application.port.out.PasswordHasher;
import com.combat47.ecommerce.identity.application.port.out.RefreshTokenRepository;
import com.combat47.ecommerce.identity.application.port.out.TokenProvider;
import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.application.service.LoginService;
import com.combat47.ecommerce.identity.application.service.RefreshTokenService;
import com.combat47.ecommerce.identity.application.service.RegisterUserService;
import com.combat47.ecommerce.identity.infrastructure.persistence.adapter.JpaRefreshTokenRepositoryAdapter;
import com.combat47.ecommerce.identity.infrastructure.persistence.mapper.RefreshTokenEntityMapper;
import com.combat47.ecommerce.identity.infrastructure.persistence.repository.JpaRefreshTokenRepository;
import com.combat47.ecommerce.identity.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class IdentityConfiguration {


    @Bean
    public RegisterUserUseCase registerUserUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher) {

        return new RegisterUserService(userRepository, passwordHasher);
    }

    @Bean
    public LoginUseCase loginUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            TokenProvider tokenProvider
    ) {
        return new LoginService(userRepository, passwordHasher, tokenProvider);
    }

}
