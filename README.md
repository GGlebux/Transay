# Transay

Веб-сервис расшифровки медицинских анализов. Монорепозиторий: бэкенд + фронтенд + общая
оркестрация через Docker Compose.

```
Transay/
├── backend/                 # Spring Boot 3.4 · Java 23 · PostgreSQL 16 · Flyway · JWT
├── frontend/                # React 19 · Vite · TypeScript (отдаётся nginx'ом)
├── docker-compose.yml       # ПРОД: запуск из готовых образов DockerHub (default)
├── docker-compose.dev.yml   # DEV: сборка из исходников
├── .env.example             # шаблон переменных (скопируй в .env)
└── .github/workflows/       # CI: build-and-push (образы) + release (deploy-архив)
```

Нужен **только Docker** (Desktop с Compose v2). Java/Maven/Node локально не требуются.

---

## Запуск на сервере — минимум команд

Образы собирает CI и кладёт в DockerHub. На сервере исходники не нужны.

**Вариант A — один архив (рекомендую, версионируется через GitHub Release):**
```bash
curl -fsSL https://github.com/GGlebux/Transay/releases/latest/download/transay-deploy.tar.gz | tar xz
cd transay
cp .env.example .env && nano .env          # вписать боевые секреты, ОДИН раз
docker compose up -d --pull always
```

**Вариант B — без релиза, тянем compose напрямую:**
```bash
mkdir transay && cd transay
curl -fsSL https://raw.githubusercontent.com/GGlebux/Transay/main/docker-compose.yml -o docker-compose.yml
curl -fsSL https://raw.githubusercontent.com/GGlebux/Transay/main/.env.example -o .env
nano .env
docker compose up -d --pull always
```

`docker compose up -d --pull always` — это и старт, и обновление (тянет свежие образы).
Наружу открыт только `frontend` (порт 80); backend и postgres — во внутренней сети.

Боевой `.env` (отличия от dev-дефолтов):
- сильный `POSTGRES_PASSWORD` и совпадающий `DB_PASS`;
- новый `JWT_SECRET` — ровно 32 байта в base64: `openssl rand -base64 32`;
- `FRONT_URL=https://твой-домен`;
- реальные `EMAIL_*`, если нужна почта.

> **Секреты не коммитятся.** В репозитории только `.env.example`. Реальный `.env` создаётся на
> сервере. Приватный репо — добавь к `curl` заголовок `-H "Authorization: token <PAT>"` или копируй по scp.

### TLS / домен
Перед стеком — reverse-proxy с автоматическим HTTPS. Минимальный Caddy:
```bash
# Caddyfile:  твой-домен { reverse_proxy transay-frontend:80 }
docker run -d --name caddy --restart unless-stopped \
  --network transay_default -p 80:80 -p 443:443 \
  -v $PWD/Caddyfile:/etc/caddy/Caddyfile -v caddy_data:/data \
  caddy
```
Тогда у `frontend` в `docker-compose.yml` маппинг `80:80` можно убрать — наружу торчит только Caddy.

### Автодеплой (опционально)
Watchtower раз в минуту проверяет DockerHub и перезапускает помеченные контейнеры на новых образах:
```bash
docker compose --profile autoupdate up -d --pull always
```
Полный цикл: `git push` → CI собирает и пушит образы → watchtower на сервере подхватывает.

---

## Локальный запуск (разработка)

**Всё в контейнерах, сборка из исходников:**
```bash
cp .env.example .env
docker compose -f docker-compose.dev.yml up --build
```
Открой **http://localhost:3000**.

| Сервис   | На хосте | Заметка                                       |
|----------|----------|-----------------------------------------------|
| frontend | `:3000`  | nginx, проксирует `/api` → `transay-backend:8080` |
| backend  | `:8081`  | профиль `prod`, Flyway мигрирует БД на старте |
| postgres | `:5433`  | данные в volume `postgres_data`               |

Остановить: `docker compose -f docker-compose.dev.yml down` (с данными — добавь `-v`).

**Hot-reload фронта** (бэк+БД в Docker, фронт локальным Vite, нужен Node 20):
```bash
docker compose -f docker-compose.dev.yml up -d postgres backend
cd frontend && npm install && npm run dev      # Vite на :5173, проксирует /api → :8081
```

### Тестовые учётки (из Flyway-сида)
- `admin@example.com` / `Admin123!` — ADMIN
- `rejngleb@gmail.com` / `User1234!` — USER

---

## CI/CD

- **`build-and-push.yml`** — на push в `main` и на теги `v*` собирает и пушит
  `gglebux/transay-backend` и `gglebux/transay-frontend`. Теги образов: `latest` (только main),
  `sha-<commit>` (откат на любой коммит), `vX.Y.Z` (на git-тег). Нужны секреты репозитория
  `DOCKER_USERNAME` / `DOCKER_PASSWORD` (Docker Hub access token).
- **`release.yml`** — на тег `vX.Y.Z` пакует `docker-compose.yml` + `.env.example` в
  `transay-deploy.tar.gz` и публикует GitHub Release (его тянет «Вариант A»).

Выпуск версии: `git tag v1.0.0 && git push origin v1.0.0` → соберутся образы с тегом `v1.0.0`
и появится релиз с deploy-архивом. Откат: пропиши на сервере конкретный тег образа вместо `latest`.

## Заметки
- `frontend/.env.production` зашивает `VITE_API_BASE_URL=/api` в сборку. Для отдельного деплоя
  фронта (без nginx-прокси) укажи полный URL API.
- `frontend/nginx.conf` рассчитан на upstream `transay-backend:8080`. Для другого окружения
  (напр. приватная сеть Railway) поправь `proxy_pass`.
- backend отдаёт `/actuator/health` (открыт в SecurityConfig) → используется в compose-healthcheck;
  фронт стартует только когда бэк «healthy».
- backend-образ — multi-stage на `eclipse-temurin:23-jre-alpine`, запуск от non-root. Дальше можно
  ужать через layered jars / jlink (пока не делалось).
- `.env` в git не коммитится. Шаблон — `.env.example`.
