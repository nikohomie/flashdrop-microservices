-- Compatibilidad con el MVP heredado de Node.js
-- Se crean las tablas de perfiles especializados usando UUID como FK en vez de BIGINT

CREATE TABLE client (
    id         UUID PRIMARY KEY,
    user_id    UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    end_date   TIMESTAMP,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE restaurant (
    id         UUID PRIMARY KEY,
    user_id    UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    name       VARCHAR(160) NOT NULL,
    address    VARCHAR(220),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE delivery (
    id         UUID PRIMARY KEY,
    user_id    UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    vehicle    VARCHAR(80),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
