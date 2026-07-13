package com.flashdrop.auth.infrastructure.adapter.outbound.persistence;

import com.flashdrop.auth.application.port.outbound.RoleRepository;
import com.flashdrop.auth.domain.model.Role;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleRepositoryAdapter implements RoleRepository {

    private final SpringDataRoleRepository jpa;

    public RoleRepositoryAdapter(SpringDataRoleRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpa.findByName(name).map(RoleEntity::toDomain);
    }
}
