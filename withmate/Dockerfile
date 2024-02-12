# 1: Gradle을 사용해 Spring Boot 애플리케이션을 빌드
FROM gradle:7.4.0-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle
COPY --chown=gradle:gradle . /app/
RUN ./gradlew clean build -x test --no-daemon

# 2: 빌드한 애플리케이션을 실행하기 위한 JRE 17을 포함한 새로운 image 생성
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
