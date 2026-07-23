package com.flashdrop.auth.infrastructure.adapter.outbound.persistence.supabase.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record UserHasRoleRow(
        @JsonProperty("user_id") UUID userId,
        @JsonProperty("role_id") UUID roleId
) {
}
