package com.flashdrop.auth.infrastructure.adapter.outbound.persistence.supabase.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flashdrop.auth.domain.model.Role;
import com.flashdrop.auth.domain.model.User;
import com.flashdrop.auth.domain.valueobject.Email;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserRow(
        UUID id,
        String email,
        String rut,
        String name,
        @JsonProperty("last_name") String lastName,
        String phone,
        String photo,
        @JsonProperty("created_at") Instant createdAt,
        List<RoleRow> roles
) {
    public User toDomain() {
        List<Role> domainRoles = roles == null ? List.of() : roles.stream().map(RoleRow::toDomain).toList();
        return new User(id, new Email(email), rut, name, lastName, phone, photo, domainRoles, createdAt);
    }
}
