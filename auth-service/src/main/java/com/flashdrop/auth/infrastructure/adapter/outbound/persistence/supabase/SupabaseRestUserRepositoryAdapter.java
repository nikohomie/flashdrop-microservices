package com.flashdrop.auth.infrastructure.adapter.outbound.persistence.supabase;

import com.flashdrop.auth.application.port.outbound.UserRepository;
import com.flashdrop.auth.domain.model.User;
import com.flashdrop.auth.domain.valueobject.Email;
import com.flashdrop.auth.infrastructure.adapter.outbound.persistence.supabase.dto.UserHasRoleRow;
import com.flashdrop.auth.infrastructure.adapter.outbound.persistence.supabase.dto.UserRow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Repository
public class SupabaseRestUserRepositoryAdapter implements UserRepository {

    private final RestClient restClient;

    public SupabaseRestUserRepositoryAdapter(
            @Value("${supabase.url}") String url,
            @Value("${supabase.service-role-key}") String key) {
        this.restClient = RestClient.builder()
                .baseUrl(url + "/rest/v1")
                .defaultHeader("apikey", key)
                .defaultHeader("Authorization", "Bearer " + key)
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Override
    public User save(User user) {
        UserRow dto = new UserRow(
                user.id(),
                user.email().value(),
                user.rut(),
                user.name(),
                user.lastName(),
                user.phone(),
                user.photo(),
                user.createdAt(),
                null
        );

        restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/users")
                        .queryParam("on_conflict", "id")
                        .build())
                .header("Prefer", "resolution=merge-duplicates,return=representation")
                .body(dto)
                .retrieve()
                .toBodilessEntity();

        if (!user.roles().isEmpty()) {
            var relations = user.roles().stream()
                    .map(role -> new UserHasRoleRow(user.id(), role.id()))
                    .toList();
            
            restClient.delete()
                    .uri(uriBuilder -> uriBuilder.path("/user_has_roles").queryParam("user_id", "eq." + user.id()).build())
                    .retrieve()
                    .toBodilessEntity();

            restClient.post()
                    .uri("/user_has_roles")
                    .body(relations)
                    .retrieve()
                    .toBodilessEntity();
        }

        return findById(user.id()).orElse(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        UserRow[] rows = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users")
                        .queryParam("id", "eq." + id)
                        .queryParam("select", "*,roles(*)")
                        .build())
                .retrieve()
                .body(UserRow[].class);

        if (rows == null || rows.length == 0) return Optional.empty();
        return Optional.of(rows[0].toDomain());
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        UserRow[] rows = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users")
                        .queryParam("email", "eq." + email.value())
                        .queryParam("select", "*,roles(*)")
                        .build())
                .retrieve()
                .body(UserRow[].class);

        if (rows == null || rows.length == 0) return Optional.empty();
        return Optional.of(rows[0].toDomain());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return findByEmail(email).isPresent();
    }
}
