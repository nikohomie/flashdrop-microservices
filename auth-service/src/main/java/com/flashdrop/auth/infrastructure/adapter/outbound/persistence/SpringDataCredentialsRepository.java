package com.flashdrop.auth.infrastructure.adapter.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataCredentialsRepository extends JpaRepository<CredentialsEntity, UUID> {
    Optional<CredentialsEntity> findByLogin(String login);
}
