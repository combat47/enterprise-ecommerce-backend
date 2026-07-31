package com.combat47.ecommerce.identity.infrastructure.persistence.adapter;

import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.domain.model.Email;
import com.combat47.ecommerce.identity.domain.model.User;
import com.combat47.ecommerce.identity.infrastructure.persistence.entity.UserEntity;
import com.combat47.ecommerce.identity.infrastructure.persistence.mapper.UserEntityMapper;
import com.combat47.ecommerce.identity.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public class JpaUserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper userEntityMapper;

    public JpaUserRepositoryAdapter(JpaUserRepository jpaUserRepository, UserEntityMapper userEntityMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userEntityMapper = userEntityMapper;
    }


    @Override
    public boolean existsByEmail(Email email) {
        return jpaUserRepository.existsByEmail(email.value());
    }


    @Override
    public User save(User user) {

        UserEntity userEntity = userEntityMapper.toEntity(user);

        UserEntity saved = jpaUserRepository.save(userEntity);

        return userEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(Email email) {

        return jpaUserRepository.findByEmail(email.value()).map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id).map(userEntityMapper::toDomain);
    }
}
