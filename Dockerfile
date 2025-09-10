# 1단계: 빌드용 (Gradle + JDK 포함)
FROM gradle:8.8-jdk17 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

# 2단계: 실행용 (불필요한 도구 제거 → 최소화)
FROM openjdk:17-jdk-slim
WORKDIR /spring-boot
COPY --from=builder /app/build/libs/graduation-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/spring-boot/app.jar"]