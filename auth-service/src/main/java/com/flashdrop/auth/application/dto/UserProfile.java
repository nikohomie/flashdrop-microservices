package com.flashdrop.auth.application.dto;

import java.util.List;
import java.util.UUID;

public record UserProfile(
        UUID userId,
        String name,
        String lastName,
        String email,
        String phone,
        String rut,
        String photo,
        List<String> roles
) {
}
