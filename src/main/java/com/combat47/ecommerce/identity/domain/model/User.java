package com.combat47.ecommerce.identity.domain.model;

import com.combat47.ecommerce.identity.domain.exception.InvalidEmailException;
import com.combat47.ecommerce.identity.domain.exception.InvalidUserStateException;

import java.time.Instant;
import java.util.UUID;

public final class User {

    private final UUID id;

    private Email email;

    private PasswordHash passwordHash;

    private FirstName firstName;

    private LastName lastName;

    private UserStatus userStatus;

    private final Instant createdAt;

    private Instant updatedAt;


    private User(UUID id, Email email, PasswordHash passwordHash, FirstName firstName, LastName lastName, UserStatus userStatus, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userStatus = userStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User register(Email email, PasswordHash passwordHash, FirstName firstName,
                                LastName lastName) {
        UUID id = UUID.randomUUID();
        UserStatus status = UserStatus.PENDING_VERIFICATION;
        Instant now = Instant.now();
        return new User(id, email, passwordHash, firstName, lastName, status, now, now);
    }

    public void activate() {
        if (this.userStatus != UserStatus.PENDING_VERIFICATION) {
            throw new InvalidUserStateException(
                    "User can only be activated from PENDING_VERIFICATION status. Current status: " +
                            this.userStatus
            );
        }
        this.userStatus = UserStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        if (userStatus != UserStatus.ACTIVE) {
            throw new InvalidUserStateException(
                    "User can only be deactivated from ACTIVE status. Current status: " +
                            this.userStatus
            );
        }
        this.userStatus = UserStatus.INACTIVE;
        this.updatedAt = Instant.now();
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

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
