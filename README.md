# Transay

Веб-сервис расшифровки медицинских анализов. Монорепозиторий: бэкенд + фронтенд + общая
оркестрация через Docker Compose.

```
Transay/
├── backend/            # Spring Boot 3.4 · Java 23 · PostgreSQL 16 · Flyway · JWT
├── frontend/           # React 19 · Vite · TypeScript (отдаётся nginx'ом)
├── docker-compose.yml  # postgres + backend + frontend в одной сети
├── .env.example        # шаблон переменных для compose (скопируй в .env)
└── .github/workflows/  # CI: сборка и push образов в DockerHub
```

## Требования

Для запуска через Docker нужен **только Docker** (Desktop с Compose v2). Java/Maven/Node
локально не нужны — бэкенд собирается внутри multi-stage образа, фронтенд тоже.

## Быстрый старт (всё в контейнерах)

```bash
cp .env.example .env          # при желании поправь секреты
docker compose up --build
```

Открой **http://localhost:3000**. Что поднимается:

| Сервис   | Внутри сети            | На хосте | Заметка                                   |
|----------|------------------------|----------|-------------------------------------------|
| frontend | nginx :80              | `:3000`  | проксирует `/api` → `transay-backend:8080`|
| backend  | Spring Boot :8080      | `:8081`  | профиль `prod`, Flyway мигрирует БД на старте |
| postgres | PostgreSQL :5432       | `:5433`  | данные в volume `postgres_data`           |

Браузер ходит на тот же origin (`:3000`), nginx проксирует `/api` на бэкенд — CORS не мешает.

Остановить: `docker compose down` (с данными — `docker compose down -v`).

### Тестовые учётки (из Flyway-сида)
- `admin@example.com` / `Admin123!` — ADMIN
- `rejngleb@gmail.com` / `User1234!` — USER

## Разработка с hot-reload фронта

Бэкенд + БД в Docker, фронт — локальным Vite (нужен Node 20):

```bash
docker compose up -d postgres backend     # БД + API на :8081
cd frontend
npm install
npm run dev                                # Vite на :5173
```

`vite.config.ts` проксирует `/api` → `http://localhost:8081`, так что фронт работает с тем же бэком.

## Запуск на сервере

1. Установи Docker + Compose, склонируй репозиторий.
2. Создай `.env` с **боевыми** значениями:
   - сильный `POSTGRES_PASSWORD` и совпадающий `DB_PASS`;
   - новый `JWT_SECRET` — ровно 32 байта в base64: `openssl rand -base64 32`;
   - `FRONT_URL=https://твой-домен`;
   - реальные `EMAIL_*`, если нужна почта.
3. `docker compose up -d --build`.
4. Поставь перед стеком reverse-proxy с TLS (Caddy/Traefik/nginx): домен → `frontend` (порт 3000
   или поменяй маппинг на `80:80`). Наружу публикуй только фронт; порты `8081`/`5433` оставь для
   локальной отладки или убери из `ports`.

### Образы и CI
`.github/workflows/build-and-push.yml` на push в `main` собирает и пушит
`gglebux/transay-backend:latest` и `gglebux/transay-frontend:latest` в DockerHub
(нужны секреты репозитория `DOCKER_USERNAME` / `DOCKER_PASSWORD`). На сервере можно либо собирать
локально (`--build`), либо тянуть готовые образы (убрать блоки `build:` и оставить `image:`).

## Заметки
- `frontend/.env.production` зашивает `VITE_API_BASE_URL=/api` в сборку — менять не нужно для
  compose-схемы. Для отдельного деплоя фронта (без nginx-прокси) укажи полный URL API.
- `frontend/nginx.conf` рассчитан на upstream `transay-backend:8080` (этот compose). Для другого
  окружения (напр. приватная сеть Railway) поправь `proxy_pass`.
- `.env` в git не коммитится (см. `.gitignore`). Шаблон — `.env.example`.
