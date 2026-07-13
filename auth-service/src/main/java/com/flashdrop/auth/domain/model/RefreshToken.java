package com.flashdrop.auth.domain.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Refresh token persistido (solo su hash). Permite rotación y revocación (S-15).
 */
public record RefreshToken(UUID id, UUID userId, String tokenHash, Instant expiresAt, boolean revoked) {

    public boolean isUsable(Instant now) {
        return !revoked && now.isBefore(expiresAt);
    }

    public RefreshToken revokedCopy() {
        return new RefreshToken(id, userId, tokenHash, expiresAt, true);
    }
}
