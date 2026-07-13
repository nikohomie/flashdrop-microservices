package com.flashdrop.auth.domain.model;

import java.util.UUID;

/**
 * Credenciales de acceso. Nunca salen del servicio: el passwordHash
 * jamás se expone en una respuesta.
 */
public record Credentials(UUID id, UUID userId, String login, String passwordHash, String status) {

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }
}
