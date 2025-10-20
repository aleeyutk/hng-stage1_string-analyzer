# Build stage
FROM openjdk:17-jdk-slim as builder

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src ./src

# Make mvnw executable and build
RUN chmod +x mvnw && \
    ./mvnw clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/string-analyzer-1.0.0.jar app.jar

# Create a non-root user to run the application
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

# Expose port (Fly.io uses 8080 by default)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
