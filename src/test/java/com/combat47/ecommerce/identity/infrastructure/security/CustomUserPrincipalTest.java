package com.combat47.ecommerce.identity.infrastructure.security;

import com.combat47.ecommerce.identity.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserPrincipalTest {

    private User user;
    private CustomUserPrincipal principal;


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

        principal = new CustomUserPrincipal(user);
    }


    @Test
    void should_return_username() {
        assertEquals("amir@test.com", principal.getUsername());
    }


    @Test
    void should_return_password() {
        assertEquals("hashed-password", principal.getPassword());
    }


    @Test
    void should_return_authorities_with_role_prefix() {
        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();

        Set<String> authorityStrings = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertTrue(authorityStrings.contains("ROLE_" + Role.CUSTOMER.name()));
        assertTrue(authorityStrings.contains("ROLE_" + Role.SELLER.name()));
        assertTrue(authorityStrings.contains("ROLE_" + Role.ADMIN.name()));
        assertEquals(3, authorities.size());
    }


    @Test
    void all_account_flags_should_be_true() {
        assertTrue(principal.isAccountNonExpired());
        assertTrue(principal.isAccountNonLocked());
        assertTrue(principal.isCredentialsNonExpired());
        assertTrue(principal.isEnabled());
    }


    @Test
    void should_return_id() {
        assertEquals(user.getId(), principal.getId());
    }


    @Test
    void should_have_correct_authorities_count() {
        assertEquals(3, principal.getAuthorities().size());
    }

    @Test
    void should_create_principal_with_customer_role_only_when_no_extra_roles_assigned() {
        User newUser = User.register(
                new Email("customer@test.com"),
                new PasswordHash("hashed-password"),
                new FirstName("Test"),
                new LastName("User")
        );

        CustomUserPrincipal newPrincipal = new CustomUserPrincipal(newUser);

        Collection<? extends GrantedAuthority> authorities = newPrincipal.getAuthorities();

        assertEquals(1, authorities.size());

        String authority = authorities.iterator().next().getAuthority();
        assertEquals("ROLE_" + Role.CUSTOMER.name(), authority);
    }

}
