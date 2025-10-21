FROM openjdk:17-slim

WORKDIR /app

# Copy the built JAR file
COPY target/string-analyzer-1.0.0.jar app.jar

# Create non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
