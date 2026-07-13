-- Hardening específico de Supabase para auth-service.
-- Ejecutar UNA VEZ en el SQL Editor de Supabase DESPUÉS del primer arranque
-- (cuando Flyway ya creó las tablas users, login, roles, user_has_roles, refresh_tokens).
--
-- Motivo: Supabase expone automáticamente el esquema public por su API REST
-- (PostgREST). Sin esto, las tablas de credenciales quedarían alcanzables con la
-- anon/service key. Este script las cierra y aplica menor privilegio (S-04).

-- 1) Cerrar el acceso de la API pública a credenciales, tokens y usuarios.
REVOKE ALL ON public.login          FROM anon, authenticated;
REVOKE ALL ON public.refresh_tokens FROM anon, authenticated;
REVOKE ALL ON public.users          FROM anon, authenticated;
REVOKE ALL ON public.roles          FROM anon, authenticated;
REVOKE ALL ON public.user_has_roles FROM anon, authenticated;

-- 2) Activar RLS (default-deny) en las tablas de identidad. El servicio se conecta
--    como el rol dueño de las tablas y no se ve afectado; la API pública sí queda bloqueada.
ALTER TABLE public.users          ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.login          ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.roles          ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.user_has_roles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.refresh_tokens ENABLE ROW LEVEL SECURITY;

-- 3) (Opcional, recomendado) Rol dedicado de menor privilegio para el servicio,
--    en vez de conectarte con el superusuario 'postgres' (S-04):
-- CREATE ROLE auth_service LOGIN PASSWORD '<clave-fuerte>';
-- GRANT USAGE ON SCHEMA public TO auth_service;
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO auth_service;
-- ALTER DEFAULT PRIVILEGES IN SCHEMA public
--   GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO auth_service;
