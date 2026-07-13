-- Esquema del bounded context de identidad (auth-service).
-- IDs como UUID (v7 generados por la aplicación).

CREATE TABLE roles (
    id    UUID PRIMARY KEY,
    name  VARCHAR(80)  NOT NULL UNIQUE,
    route VARCHAR(180) NOT NULL
);

CREATE TABLE users (
    id         UUID PRIMARY KEY,
    email      VARCHAR(180) NOT NULL UNIQUE,
    rut        VARCHAR(30),
    name       VARCHAR(120),
    last_name  VARCHAR(120),
    phone      VARCHAR(40) UNIQUE,
    photo      TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Credenciales separadas del perfil (nunca se exponen fuera del servicio).
CREATE TABLE login (
    id            UUID PRIMARY KEY,
    login         VARCHAR(180) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    user_id       UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE user_has_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE INDEX idx_login_user ON login(user_id);

-- Roles base del dominio.
INSERT INTO roles (id, name, route) VALUES
    (gen_random_uuid(), 'Cliente',     '/client/products/list'),
    (gen_random_uuid(), 'Restaurante', '/restaurant/orders/list'),
    (gen_random_uuid(), 'Repartidor',  '/delivery/orders/list');
