# syntax=docker/dockerfile:1

# ---- Этап сборки: собираем jar внутри Docker ----
# Локально не нужны ни Maven, ни JDK 23 — всё внутри образа.
FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /app

# Сначала только pom.xml — слой с зависимостями кешируется, пока pom не меняется.
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

# Затем исходники и сборка.
COPY src ./src
RUN mvn -q -B clean package -DskipTests

# ---- Этап запуска: тонкий runtime-образ ----
FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
