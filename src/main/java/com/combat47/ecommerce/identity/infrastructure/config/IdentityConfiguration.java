package com.combat47.ecommerce.identity.infrastructure.config;


import com.combat47.ecommerce.identity.application.port.in.RegisterUserUseCase;
import com.combat47.ecommerce.identity.application.port.out.PasswordHasher;
import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.application.service.RegisterUserService;
import com.combat47.ecommerce.identity.infrastructure.persistence.inmemory.InMemoryUserRepository;
import com.combat47.ecommerce.identity.infrastructure.security.FakePasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class IdentityConfiguration {

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    public PasswordHasher passwordHasher() {
        return new FakePasswordHasher();
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher) {

        return new RegisterUserService(userRepository, passwordHasher);
    }
}
