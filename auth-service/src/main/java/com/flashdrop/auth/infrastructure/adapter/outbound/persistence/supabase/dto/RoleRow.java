package com.flashdrop.auth.infrastructure.adapter.outbound.persistence.supabase.dto;

import com.flashdrop.auth.domain.model.Role;

import java.util.UUID;

public record RoleRow(
        UUID id,
        String name,
        String route
) {
    public Role toDomain() {
        return new Role(id, name, route);
    }
}
