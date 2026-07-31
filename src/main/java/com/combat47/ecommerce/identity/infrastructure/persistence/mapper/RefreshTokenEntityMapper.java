package com.combat47.ecommerce.identity.infrastructure.persistence.mapper;

import com.combat47.ecommerce.identity.domain.model.RefreshToken;
import com.combat47.ecommerce.identity.infrastructure.persistence.entity.RefreshTokenEntity;
import com.combat47.ecommerce.identity.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;



@Component
public class RefreshTokenEntityMapper {

    public RefreshTokenEntity toEntity(RefreshToken domain, UserEntity userEntity) {

        return new RefreshTokenEntity(
                domain.getId(),
                domain.getToken(),
                userEntity,
                domain.getExpiresAt(),
                domain.isRevoked(),
                domain.getCreatedAt()
        );
    }

    public RefreshToken toDomain(RefreshTokenEntity entity) {
        return RefreshToken.restore(
                entity.getId(),
                entity.getToken(),
                entity.getUser().getId(),
                entity.getExpiresAt(),
                entity.isRevoked(),
                entity.getCreatedAt()
        );
    }
}
