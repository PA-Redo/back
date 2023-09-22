FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace/app

COPY . .

RUN ./mvnw install -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /workspace/app

COPY --from=build /workspace/app/core/target/*.jar .
COPY --from=build /workspace/app/croix-rouge-repository/target/*.jar .
COPY --from=build /workspace/app/event/target/*.jar .
COPY --from=build /workspace/app/repository/target/*.jar .
COPY --from=build /workspace/app/stock/target/*.jar .
COPY --from=build /workspace/app/web-api/target/*.jar .

COPY application-env.properties application-prod.properties

ENV JWT_SECRET="" \
    JWT_TOKEN_EXPIRATION_TIME=999999999 \
    JWT_TOKEN_HEADER=Authorization \
    JWT_TOKEN_PREFIX=Bearer \
    SPRING_JPA_DDL_AUTO=update \
    SPRING_DATASOURCE_URL="" \
    SPRING_DATASOURCE_USERNAME="" \
    SPRING_DATASOURCE_PASSWORD="" \
    SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver \
    SPRING_MAIL_HOST="" \
    SPRING_MAIL_PORT=465 \
    SPRING_MAIL_USERNAME="" \
    SPRING_MAIL_PASSWORD="" \
    SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true \
    SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true \
    SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_REQUIRED=true \
    SPRING_MAIL_PROPERTIES_MAIL_SMTP_SSL_ENABLE=true \
    API_URL=""

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "web-api-1.0-SNAPSHOT.jar"]
