package com.flashdrop.auth.infrastructure.adapter.outbound.persistence;

import com.flashdrop.auth.domain.model.RefreshToken;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected RefreshTokenEntity() {}

    public static RefreshTokenEntity fromDomain(RefreshToken t) {
        RefreshTokenEntity e = new RefreshTokenEntity();
        e.id = t.id();
        e.userId = t.userId();
        e.tokenHash = t.tokenHash();
        e.expiresAt = t.expiresAt();
        e.revoked = t.revoked();
        return e;
    }

    public RefreshToken toDomain() {
        return new RefreshToken(id, userId, tokenHash, expiresAt, revoked);
    }
}
