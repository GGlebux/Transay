FROM openjdk:23-slim

WORKDIR /app

COPY ./target/TranSay-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "TranSay-0.0.1-SNAPSHOT.jar"]