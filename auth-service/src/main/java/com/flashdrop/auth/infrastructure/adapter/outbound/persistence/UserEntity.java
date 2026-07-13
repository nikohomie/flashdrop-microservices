package com.flashdrop.auth.infrastructure.adapter.outbound.persistence;

import com.flashdrop.auth.domain.model.User;
import com.flashdrop.auth.domain.valueobject.Email;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    private String rut;
    private String name;

    @Column(name = "last_name")
    private String lastName;

    private String phone;
    private String photo;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_has_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    protected UserEntity() {}

    public static UserEntity fromDomain(User user, Set<RoleEntity> roleEntities) {
        UserEntity e = new UserEntity();
        e.id = user.id();
        e.email = user.email().value();
        e.rut = user.rut();
        e.name = user.name();
        e.lastName = user.lastName();
        e.phone = user.phone();
        e.photo = user.photo();
        e.createdAt = user.createdAt();
        e.roles = roleEntities;
        return e;
    }

    public User toDomain() {
        var roleList = roles.stream().map(RoleEntity::toDomain).toList();
        return new User(id, new Email(email), rut, name, lastName, phone, photo, roleList, createdAt);
    }

    public UUID getId() { return id; }
}
