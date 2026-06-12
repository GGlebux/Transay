-- Известные пароли для тестовых аккаунтов (admin + user), чтобы проект логинился
-- "из коробки" после `docker compose up`.
--
-- Почему отдельная миграция, а не правка V2__seed_data.sql:
--   изменение уже применённой миграции ломает её checksum -> Flyway упадёт с
--   "Migration checksum mismatch" на любой БД, где V2 уже накатана (прод/существующие тома).
--   V9 безопасна: на свежей БД она перезапишет seed-пароли, на существующей — просто обновит.
--
-- Хэши считаются через pgcrypto (расширение есть в официальном образе postgres:16),
-- gen_salt('bf', 10) даёт bcrypt $2a$10$..., который принимает Spring BCryptPasswordEncoder.
-- Plaintext-пароли держим прямо здесь — они же и есть документация тестовых кредов.
--
-- Тестовые учётки (значения также продублированы в README.md):
--   admin@example.com  / Admin123!   (роль ADMIN)
--   rejngleb@gmail.com / User1234!   (роль USER, привязан к персоне 16 «GGlebux»)
--
-- Пароли удовлетворяют @Pattern из LoginRequestDTO:
--   минимум 8 символов, заглавная + строчная буква, цифра, спецсимвол, без пробелов.

CREATE EXTENSION IF NOT EXISTS pgcrypto;

UPDATE public.customer
SET password    = crypt('Admin123!', gen_salt('bf', 10)),
    is_verified = true,
    status      = 'ACTIVE'
WHERE id = 1;

UPDATE public.customer
SET password    = crypt('User1234!', gen_salt('bf', 10)),
    is_verified = true,
    status      = 'ACTIVE'
WHERE id = 2;
