package com.flashdrop.auth.infrastructure.adapter.outbound.persistence;

import com.flashdrop.auth.domain.model.Role;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    private UUID id;
    private String name;
    private String route;

    protected RoleEntity() {}

    public RoleEntity(UUID id, String name, String route) {
        this.id = id;
        this.name = name;
        this.route = route;
    }

    public Role toDomain() {
        return new Role(id, name, route);
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getRoute() { return route; }
}
