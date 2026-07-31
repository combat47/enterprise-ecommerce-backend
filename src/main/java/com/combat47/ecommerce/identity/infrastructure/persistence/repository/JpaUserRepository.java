package com.combat47.ecommerce.identity.infrastructure.persistence.repository;

import com.combat47.ecommerce.identity.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmail(String email);


    @Query("""
           SELECT u
           FROM UserEntity u
           LEFT JOIN FETCH u.roles
           WHERE u.email = :email
           """)
    Optional<UserEntity> findByEmail(String email);

    @Query("""
           SELECT u
           FROM UserEntity u
           LEFT JOIN FETCH u.roles
           WHERE u.id = :id
           """)
    Optional<UserEntity> findById(@Param("id") UUID id);

}
