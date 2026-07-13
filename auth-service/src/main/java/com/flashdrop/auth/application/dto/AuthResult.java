package com.flashdrop.auth.application.dto;

import java.util.List;
import java.util.UUID;

public record AuthResult(
        UUID userId,
        String name,
        String email,
        List<String> roles,
        String accessToken,
        String refreshToken,
        long expiresInSeconds
) {
}
