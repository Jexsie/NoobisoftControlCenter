# Multi-stage build for better efficiency
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy Maven wrapper and pom.xml first for better layer caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

# Create a non-root user for security
RUN addgroup --system javauser && adduser --system --ingroup javauser javauser

# Copy the packaged JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to the non-root user
RUN chown -R javauser:javauser /app

# Switch to non-root user
USER javauser

# Set environment variables with defaults
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8070

# Expose the port
EXPOSE 8070

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8070/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

