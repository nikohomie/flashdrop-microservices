package com.flashdrop.auth.infrastructure.adapter.outbound.persistence;

import com.flashdrop.auth.application.port.outbound.CredentialStore;
import com.flashdrop.auth.domain.model.Credentials;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CredentialStoreAdapter implements CredentialStore {

    private final SpringDataCredentialsRepository jpa;

    public CredentialStoreAdapter(SpringDataCredentialsRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(Credentials credentials) {
        jpa.save(CredentialsEntity.fromDomain(credentials));
    }

    @Override
    public Optional<Credentials> findByLogin(String login) {
        return jpa.findByLogin(login).map(CredentialsEntity::toDomain);
    }
}
