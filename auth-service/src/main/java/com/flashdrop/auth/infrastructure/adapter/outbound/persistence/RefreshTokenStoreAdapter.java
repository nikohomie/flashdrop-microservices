package com.flashdrop.auth.infrastructure.adapter.outbound.persistence;

import com.flashdrop.auth.application.port.outbound.RefreshTokenStore;
import com.flashdrop.auth.domain.model.RefreshToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RefreshTokenStoreAdapter implements RefreshTokenStore {

    private final SpringDataRefreshTokenRepository jpa;

    public RefreshTokenStoreAdapter(SpringDataRefreshTokenRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(RefreshToken token) {
        jpa.save(RefreshTokenEntity.fromDomain(token));
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return jpa.findByTokenHash(tokenHash).map(RefreshTokenEntity::toDomain);
    }

    @Override
    public void revoke(RefreshToken token) {
        jpa.save(RefreshTokenEntity.fromDomain(token));
    }
}
