FROM openjdk:17-jdk-slim

EXPOSE 8080

WORKDIR /app

ARG JAR_FILE=/build/libs/spring-task-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
