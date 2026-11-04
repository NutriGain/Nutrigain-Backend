# Build stage: compile and package the application
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B -DskipTests package

# Runtime stage: small JRE image running as a non-root user
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --create-home nutrigain
COPY --from=build /workspace/target/nutrigain-backend-*.jar app.jar
USER nutrigain
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+UseContainerSupport"
EXPOSE 8081
HEALTHCHECK --interval=30s --timeout=5s --start-period=90s --retries=3 \
  CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/${PORT:-8081} && printf "GET /actuator/health HTTP/1.0\r\n\r\n" >&3 && grep -q UP <&3' || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
