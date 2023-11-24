FROM openjdk:21-jdk-slim
LABEL authors="Casa"

RUN apt-get update \
    && apt-get install -y iproute2 \
    && rm -rf /var/lib/apt/lists/*

ARG JAR_FILE=target/*jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/app.jar"]