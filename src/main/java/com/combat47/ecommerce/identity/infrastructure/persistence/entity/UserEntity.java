package com.combat47.ecommerce.identity.infrastructure.persistence.entity;


import jakarta.persistence.*;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "id",  nullable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name",  nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at",  nullable = false)
    private Instant updatedAt;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<RoleEntity> roles =  new HashSet<>();


    protected  UserEntity() {
    }

    public UserEntity(UUID id,
                      String email,
                      String passwordHash,
                      String firstName,
                      String lastName,
                      Instant createdAt,
                      Instant updatedAt,
                      Set<RoleEntity> roles
    ) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roles = (roles != null) ? roles : new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Set<RoleEntity> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }
}
