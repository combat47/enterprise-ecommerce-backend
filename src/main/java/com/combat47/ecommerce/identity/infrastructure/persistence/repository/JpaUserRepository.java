package com.combat47.ecommerce.identity.infrastructure.persistence.repository;

import com.combat47.ecommerce.identity.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmail(String email);


    @Query("""
           select u
           from UserEntity u
           left join fetch u.roles
           where u.email = :email
           """)
    Optional<UserEntity> findByEmail(String email);
}
