FROM openjdk:17-jdk-slim-buster as build
WORKDIR app
COPY . .
RUN ./mvnw install

FROM openjdk:17-jdk-slim-buster
WORKDIR app
COPY --from=build /app/target/backend-*.jar /app/backend-app.jar
ENTRYPOINT java -jar /app/backend-app.jar



