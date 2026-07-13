package com.flashdrop.auth.infrastructure.adapter.outbound.persistence;

import com.flashdrop.auth.domain.model.Credentials;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "login")
public class CredentialsEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    private String status;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    protected CredentialsEntity() {}

    public static CredentialsEntity fromDomain(Credentials c) {
        CredentialsEntity e = new CredentialsEntity();
        e.id = c.id();
        e.login = c.login();
        e.passwordHash = c.passwordHash();
        e.status = c.status();
        e.userId = c.userId();
        return e;
    }

    public Credentials toDomain() {
        return new Credentials(id, userId, login, passwordHash, status);
    }
}
