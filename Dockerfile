FROM ubuntu:16.04

RUN adduser ubuntu
RUN apt update && apt install openjdk-8-jre -y --no-install-recommends

USER ubuntu
WORKDIR /app

COPY build/libs/mrt.jar /app/mrt.jar

ENTRYPOINT ["java", "-jar", "/app/mrt.jar"]
