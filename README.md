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
- **Документация**: Postman API-collection

---

## 🎯 Функционал
- Управление пациентами (`PeopleController`)
- Работа с медицинскими показателями (`IndicatorController`)
- Запись и анализ измерений (`MeasuresController`)
- Причины изменений показателей (`ReasonsController`)
- Валидация данных и обработка исключений (`GlobalExceptionHandler`)

---

## 🎮 Контроллеры

### `PeopleController` (`/people`)
- `GET /people/{id}` — получить данные пользователя
- `POST /people` — создать пользователя
- `PATCH /people/{id}` — обновить данные
- Управление связанными сущностями (например, `ex_reasons`)

### `MeasuresController` (`/people/{id}/measures`)
- `GET /measures/correct` — получить корректные показатели
- `POST /measures` — добавить измерение
- `GET /measures/decrypt` — расшифровать данные по дате

### `IndicatorController` (`/indicators`)
- CRUD для медицинских показателей
- `GET /indicators/units` — список единиц измерения

### `GlobalExceptionHandler`
Обрабатывает:
- Ошибки валидации (`400 Bad Request`)
- Отсутствующие сущности (`404 Not Found`)
- Некорректные форматы данных (`400 Bad Request`)

---

## 🚀 Запуск
```bash
git clone https://github.com/GGlebux/Transay.git
cd ./Transay/backend
docker-compose up --build
```
