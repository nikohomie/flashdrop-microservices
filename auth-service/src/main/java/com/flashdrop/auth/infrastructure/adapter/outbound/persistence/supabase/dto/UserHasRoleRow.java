package com.flashdrop.auth.infrastructure.adapter.outbound.persistence.supabase.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record UserHasRoleRow(
        @JsonProperty("id_users") Long userId,
        @JsonProperty("id_roles") Long roleId
) {
}
