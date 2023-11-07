FROM openjdk:21-jdk-slim
LABEL authors="Casa"

ARG JAR_FILE=target/*jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/app.jar"]