FROM bellsoft/liberica-openjre-alpine:21 AS layers
WORKDIR /app
COPY target/*.jar app.jar

ENV DB_HOST=credit-db
ENV DB_PORT=5432
ENV DB_NAME=credit_processing
ENV DB_USER=postgres
ENV DB_PASSWORD=admin

ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8083
