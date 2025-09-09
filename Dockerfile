FROM openjdk:17
WORKDIR /spring-boot
COPY build/libs/graduation-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/spring-boot/app.jar"]