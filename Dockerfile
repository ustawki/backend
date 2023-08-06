FROM openjdk:17-jdk-slim-buster as build
WORKDIR app
COPY . .
COPY .env /app/
RUN ./gradlew build --no-daemon

FROM openjdk:17-jdk-slim-buster
WORKDIR app
COPY --from=build /app/build/libs/helloworld-1.0.0.jar  /app/backend-app.jar
ENTRYPOINT java -jar /app/backend-app.jar



