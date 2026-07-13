package com.flashdrop.auth.infrastructure.adapter.outbound.persistence;

import com.flashdrop.auth.application.port.outbound.UserRepository;
import com.flashdrop.auth.domain.model.User;
import com.flashdrop.auth.domain.valueobject.Email;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository jpa;
    private final SpringDataRoleRepository roleJpa;

    public UserRepositoryAdapter(SpringDataUserRepository jpa, SpringDataRoleRepository roleJpa) {
        this.jpa = jpa;
        this.roleJpa = roleJpa;
    }

    @Override
    public User save(User user) {
        var roleEntities = user.roles().stream()
                .map(r -> roleJpa.findById(r.id()).orElseGet(() -> new RoleEntity(r.id(), r.name(), r.route())))
                .collect(Collectors.toCollection(HashSet::new));
        return jpa.save(UserEntity.fromDomain(user, roleEntities)).toDomain();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpa.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpa.findByEmail(email.value()).map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpa.existsByEmail(email.value());
    }
}
