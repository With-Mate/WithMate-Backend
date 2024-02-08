FROM openjdk:17-jdk-slim as build
WORKDIR /app
COPY . /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]