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

## Запуск на сервере (из готовых образов, без исходников)

Образы собирает CI и кладёт в DockerHub, поэтому на сервере нужны только **два файла** —
`docker-compose.prod.yml` и `.env` (с боевыми секретами). Исходный код клонировать не надо.

```bash
mkdir transay && cd transay
curl -fsSL https://raw.githubusercontent.com/GGlebux/Transay/main/docker-compose.prod.yml -o docker-compose.prod.yml
curl -fsSL https://raw.githubusercontent.com/GGlebux/Transay/main/.env.example -o .env
nano .env          # вписать боевые значения (см. ниже), ОДИН раз
docker compose -f docker-compose.prod.yml up -d --pull always
```

Эта же команда обновляет стек: `--pull always` тянет свежие образы перед перезапуском.
Открыто наружу только `frontend` (порт 80); backend и postgres — во внутренней сети.

Боевой `.env` (отличия от dev-дефолтов):
- сильный `POSTGRES_PASSWORD` и совпадающий `DB_PASS`;
- новый `JWT_SECRET` — ровно 32 байта в base64: `openssl rand -base64 32`;
- `FRONT_URL=https://твой-домен`;
- реальные `EMAIL_*`, если нужна почта.

> **Секреты не коммить.** В репозитории только `.env.example` (шаблон). Реальный `.env` живёт
> на сервере. Если репозиторий приватный — тяни raw-файлы с заголовком `Authorization: token <PAT>`
> или копируй их по scp.

### TLS / домен
Перед стеком поставь reverse-proxy с автоматическим HTTPS. Минимальный Caddy рядом с prod-стеком:

```bash
# Caddyfile:  твой-домен { reverse_proxy transay-frontend:80 }
docker run -d --name caddy --restart unless-stopped \
  --network transay_default -p 80:80 -p 443:443 \
  -v $PWD/Caddyfile:/etc/caddy/Caddyfile -v caddy_data:/data \
  caddy
```
(тогда в `docker-compose.prod.yml` у `frontend` маппинг порта `80:80` можно убрать — наружу торчит только Caddy.)

### CI/CD
`.github/workflows/build-and-push.yml` на push в `main` собирает и пушит
`gglebux/transay-backend:latest` и `gglebux/transay-frontend:latest` в DockerHub
(нужны секреты репозитория `DOCKER_USERNAME` / `DOCKER_PASSWORD` — Docker Hub access token).

Автодеплой без ручного `up`: подними watchtower (он раз в минуту проверяет DockerHub и
перезапускает помеченные контейнеры на новых образах):
```bash
docker compose --profile autoupdate -f docker-compose.prod.yml up -d --pull always
```
Полный цикл: `git push` → CI собирает и пушит образы → watchtower на сервере подхватывает.

### Сборка из исходников на сервере (альтернатива)
Если не хочешь DockerHub — склонируй репо и собери на месте: `docker compose up -d --build`
(использует `docker-compose.yml`, который собирает образы локально).

## Заметки
- `frontend/.env.production` зашивает `VITE_API_BASE_URL=/api` в сборку — менять не нужно для
  compose-схемы. Для отдельного деплоя фронта (без nginx-прокси) укажи полный URL API.
- `frontend/nginx.conf` рассчитан на upstream `transay-backend:8080` (этот compose). Для другого
  окружения (напр. приватная сеть Railway) поправь `proxy_pass`.
- `.env` в git не коммитится (см. `.gitignore`). Шаблон — `.env.example`.
