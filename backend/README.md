# TranSay API

RESTful API для работы с медицинскими показателями и анализами.
Принимает показатели человеческих анализов, делает диагнозы и строит отчёт.
Проект реализован на **Java 23** с использованием **Spring Boot** и **PostgreSQL**.

---

## 🛠 Технологии
- **Backend**: Java 23, Spring Boot 3.4
- **База данных**: PostgreSQL
- **Инструменты**: 
  - Maven
  - Hibernate ORM
  - JPA
  - Lombok
  - Docker & Docker-Compose
- **Документация**: Postman API-collection/ Swagger

---

## 🚀 Запуск

Выполнить:
```bash
git clone https://github.com/GGlebux/Transay.git --depth 1 --branch main
cd ./Transay/
cp .env.example .env      # переменные окружения (БД, JWT, почта) — рабочие dev-дефолты
docker compose pull
docker compose up -d
```

> `.env` обязателен (на него ссылается `docker-compose.yml`). Готовый шаблон с рабочими
> значениями лежит в [`.env.example`](.env.example). Для реального деплоя поменяйте секреты
> (`JWT_SECRET`, пароль БД, SMTP). `JWT_SECRET` — base64, декодирующийся ровно в 32 байта.

Бэкенд поднимется на `http://localhost:8081`, миграции Flyway (V1–V9) накатятся автоматически,
БД заполнится тестовыми данными.

---

## 🔑 Тестовые учётные данные

Создаются миграцией [`V9__reset_test_credentials.sql`](src/main/resources/db/migration/V9__reset_test_credentials.sql)
и работают сразу после `docker compose up`:

| Роль  | Email                | Пароль      | Привязка                         |
|-------|----------------------|-------------|----------------------------------|
| ADMIN | `admin@example.com`  | `Admin123!` | —                                |
| USER  | `rejngleb@gmail.com` | `User1234!` | персона 16 «GGlebux» (есть анализ) |

Логин: `POST /api/auth/login` с телом `{"email": "...", "password": "..."}` → access-токен в ответе.
Дальше — `Authorization: Bearer <token>` к защищённым ручкам.

```bash
# пример: логин под пользователем и запрос сводной таблицы анализов
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"rejngleb@gmail.com","password":"User1234!"}' | jq -r .accessToken)

curl -s http://localhost:8081/api/people/measures -H "Authorization: Bearer $TOKEN"
```

> ⚠️ Это пароли для локальной разработки. Перед публичным деплоем смените их.

---

## Документация
Документация доступна по [ссылке](http://localhost:8081/swagger-ui/index.html)
