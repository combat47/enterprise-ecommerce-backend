package com.combat47.ecommerce.identity.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "user_roles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_role",
                        columnNames = {"user_id", "role"}
                )
        }
)
public class RoleEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 50)
    private String role;

    protected RoleEntity() {
    }

    public RoleEntity(UUID id, UserEntity user, String role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }

    public static RoleEntity create(UserEntity user, String role) {
        return new RoleEntity(UUID.randomUUID(), user, role);
    }

    @PrePersist
    void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return user.getId();
    }

    public String getRole() {
        return role;
    }
}