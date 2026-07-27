package com.combat47.ecommerce.identity.infrastructure.security;


import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class IdentityUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private IdentityUserDetailsService userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.register(
                new Email("amir@test.com"),
                new PasswordHash("hashed-password"),
                new FirstName("Amir"),
                new LastName("Jahazi")
        );

        user.assignRole(Role.SELLER);
        user.assignRole(Role.ADMIN);
    }

    @Test
    void should_load_user_by_email_successfully() {
        when(userRepository.findByEmail(any(Email.class)))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("amir@test.com");

        assertNotNull(userDetails);
        assertEquals("amir@test.com", userDetails.getUsername());
        assertEquals("hashed-password", userDetails.getPassword());

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SELLER")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        assertEquals(3, userDetails.getAuthorities().size());

    }

    @Test
    void should_throw_UsernameNotFoundException_when_user_not_found() {
        when(userRepository.findByEmail(any(Email.class)))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("notfound@test.com"));
    }

    @Test
    void should_throw_UsernameNotFoundException_when_email_is_invalid() {
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("invalid-email"));
    }

}
