package com.combat47.ecommerce.identity.domain.model;

import com.combat47.ecommerce.identity.domain.exception.InvalidEmailException;
import com.combat47.ecommerce.identity.domain.exception.InvalidRoleException;
import com.combat47.ecommerce.identity.domain.exception.InvalidRoleOperationException;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class User {

    private final UUID id;

    private Email email;

    private PasswordHash passwordHash;

    private FirstName firstName;

    private LastName lastName;

    private final Set<Role> roles;

    private final Instant createdAt;

    private Instant updatedAt;



    private User(UUID id, Email email, PasswordHash passwordHash, FirstName firstName,
                 LastName lastName,  Set<Role> roles, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = new HashSet<>(roles);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User register(Email email, PasswordHash passwordHash, FirstName firstName,
                                LastName lastName) {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        Set<Role> defaultRoles = Set.of(Role.CUSTOMER);
        return new User(id, email, passwordHash, firstName, lastName, defaultRoles, now, now);
    }

    public void assignRole(Role role) {
        if (role == null) {
            throw new InvalidRoleException("Role cannot be null");
        }

        if (roles.contains(role)) {
            return;
        }
        roles.add(role);
        this.updatedAt = Instant.now();
    }

    public void removeRole(Role role) {
        if (role == null) {
            throw new InvalidRoleException("Role cannot be null");
        } else if (!roles.contains(role)) {
            return;
        } else if (roles.size() == 1) {
            throw new InvalidRoleOperationException(
                    "User must have at least one role. cannot remove the last role: " + role
            );
        }
        roles.remove(role);
        this.updatedAt = Instant.now();
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }


    public void changeEmail(Email newEmail) {
        if (newEmail == null) {
            throw new InvalidEmailException("new email cannot be null");
        }

        if (newEmail.equals(this.email)) {
            return;
        }

        this.email = newEmail;
        this.updatedAt = Instant.now();
    }

    public static User restore(
                               UUID id,
                               Email email,
                               PasswordHash passwordHash,
                               FirstName firstName,
                               LastName lastName,
                               Set<Role> roles,
                               Instant createdAt,
                               Instant updatedAt
    ) {
        return new User(
                id,
                email,
                passwordHash,
                firstName,
                lastName,
                roles,
                createdAt,
                updatedAt
        );
    }

    public UUID getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    public FirstName getFirstName() {
        return firstName;
    }

    public LastName getLastName() {
        return lastName;
    }

    public Set<Role> getRoles() {
        return new HashSet<>(roles);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
