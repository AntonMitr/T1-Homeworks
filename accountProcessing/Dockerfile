FROM bellsoft/liberica-openjre-alpine:21 AS layers
WORKDIR /app
COPY target/*.jar app.jar

ENV DB_HOST=account-db
ENV DB_PORT=5432
ENV DB_NAME=account_processing
ENV DB_USER=postgres
ENV DB_PASSWORD=admin

ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8082
