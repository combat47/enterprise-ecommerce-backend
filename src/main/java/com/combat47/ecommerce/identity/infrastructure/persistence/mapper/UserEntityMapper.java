package com.combat47.ecommerce.identity.infrastructure.persistence.mapper;

import com.combat47.ecommerce.identity.domain.model.*;
import com.combat47.ecommerce.identity.infrastructure.persistence.entity.RoleEntity;
import com.combat47.ecommerce.identity.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;


@Component
public class UserEntityMapper {

    public UserEntity toEntity(User user) {
        UserEntity userEntity = new UserEntity(
                user.getId(),
                user.getEmail().value(),
                user.getPasswordHash().value(),
                user.getFirstName().value(),
                user.getLastName().value(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                null
        );

        Set<RoleEntity> roleEntities = user.getRoles().stream()
                .map(role -> RoleEntity.create(userEntity, role.name()))
                .collect(Collectors.toSet());

        userEntity.setRoles(roleEntities);

        return userEntity;
    }

    public User toDomain(UserEntity entity) {

        Set<Role> roles = entity.getRoles()
                .stream()
                .map(roleEntity -> Role.valueOf(roleEntity.getRole()))
                .collect(Collectors.toSet());

        return User.restore(
                entity.getId(),
                new Email(entity.getEmail()),
                new PasswordHash(entity.getPasswordHash()),
                new FirstName(entity.getFirstName()),
                new LastName(entity.getLastName()),
                roles,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
