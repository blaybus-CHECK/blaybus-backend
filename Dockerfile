FROM openjdk:17-jdk
WORKDIR /blaybus
COPY build/libs/*SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]