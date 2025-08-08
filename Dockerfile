# Этап сборки
FROM maven:3.9.11-eclipse-temurin-24 AS build
WORKDIR /app
COPY backend/pom.xml .
RUN mvn dependency:go-offline
COPY backend/src ./src
RUN mvn package -DskipTests
# Этап запуска
FROM maven:3.9.11-eclipse-temurin-24
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]