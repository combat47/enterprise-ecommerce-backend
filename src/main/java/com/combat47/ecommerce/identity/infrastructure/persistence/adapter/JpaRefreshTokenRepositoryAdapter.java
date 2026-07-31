package com.combat47.ecommerce.identity.infrastructure.persistence.adapter;

import com.combat47.ecommerce.identity.application.port.out.RefreshTokenRepository;
import com.combat47.ecommerce.identity.domain.model.RefreshToken;
import com.combat47.ecommerce.identity.infrastructure.persistence.entity.RefreshTokenEntity;
import com.combat47.ecommerce.identity.infrastructure.persistence.entity.UserEntity;
import com.combat47.ecommerce.identity.infrastructure.persistence.mapper.RefreshTokenEntityMapper;
import com.combat47.ecommerce.identity.infrastructure.persistence.repository.JpaRefreshTokenRepository;
import com.combat47.ecommerce.identity.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Repository
public class JpaRefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRepository;
    private final JpaUserRepository jpaUserRepository;
    private final RefreshTokenEntityMapper mapper;

    public JpaRefreshTokenRepositoryAdapter(JpaRefreshTokenRepository jpaRepository, JpaUserRepository jpaUserRepository,
                                            RefreshTokenEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public RefreshToken save(RefreshToken refreshToken) {

        UserEntity userEntity = jpaUserRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + refreshToken.getUserId()));

        RefreshTokenEntity entity = mapper.toEntity(refreshToken,  userEntity);
        RefreshTokenEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepository.findByToken(token).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void revokeAllForUser(UUID userId) {
        jpaRepository.revokeAllForUser(userId);

    }

}
