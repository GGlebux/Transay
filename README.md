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

```bash
mkdir transay && cd transay
curl -fsSL https://raw.githubusercontent.com/GGlebux/Transay/main/docker-compose.yml -o docker-compose.yml
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


### Автодеплой (опционально)
Watchtower раз в минуту проверяет DockerHub и перезапускает помеченные контейнеры на новых образах:
```bash
docker compose --profile autoupdate up -d --pull always
```
Полный цикл: `git push` → CI собирает и пушит образы → watchtower на сервере подхватывает.

---

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
