FROM alpine:3.22 AS artifact

WORKDIR /artifact
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

FROM eclipse-temurin:25-jre-alpine

RUN addgroup -S orioneta \
    && adduser -S -G orioneta orioneta

WORKDIR /app

COPY --from=artifact --chown=orioneta:orioneta /artifact/app.jar app.jar

USER orioneta

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]
