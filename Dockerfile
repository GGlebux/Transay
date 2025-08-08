# Этап 1: Сборка фронта
FROM node:24-alpine AS frontend-builder
WORKDIR /app/frontend
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install
COPY frontend/ .
EXPOSE 5173
RUN npm run dev

## Этап сборки
#FROM maven:3.9.11-eclipse-temurin-24 AS build
#WORKDIR /app
#COPY backend/pom.xml .
#RUN mvn dependency:go-offline
#COPY backend/src ./src
#RUN mvn package -DskipTests
#
## Этап запуска
#FROM maven:3.9.11-eclipse-temurin-24
#WORKDIR /app
#COPY --from=build /app/target/*.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]