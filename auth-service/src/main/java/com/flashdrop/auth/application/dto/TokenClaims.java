package com.flashdrop.auth.application.dto;

import java.util.List;
import java.util.UUID;

/**
 * Identidad verificada extraída de un JWT. Es lo que el gateway propaga
 * como X-User-Id / X-User-Roles hacia los demás servicios (S-05).
 */
public record TokenClaims(UUID userId, String email, List<String> roles) {
}
