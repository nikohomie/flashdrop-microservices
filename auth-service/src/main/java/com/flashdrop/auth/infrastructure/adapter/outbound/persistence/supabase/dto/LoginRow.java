package com.flashdrop.auth.infrastructure.adapter.outbound.persistence.supabase.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flashdrop.auth.domain.model.Credentials;

import java.util.UUID;

public record LoginRow(
        UUID id,
        String login,
        @JsonProperty("password_hash") String passwordHash,
        String status,
        @JsonProperty("user_id") UUID userId
) {
    public Credentials toDomain() {
        return new Credentials(id, userId, login, passwordHash, status);
    }
}
