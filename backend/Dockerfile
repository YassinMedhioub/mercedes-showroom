# Build stage (Gradle example)
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENV JAVA_OPTS=""
# Cloud Run sets $PORT; make sure Spring uses it (via server.port above)
CMD ["sh","-c","java $JAVA_OPTS -jar app.jar"]
