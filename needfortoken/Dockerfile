# FROM eclipse-temurin:21-jdk AS builder
# WORKDIR /app
# COPY . .
# RUN ./mvnw package

# FROM eclipse-temurin:21-jdk AS runner
# WORKDIR /app
# COPY --from=builder /app/target/*.jar app.jar

# ENV HEDERA_ACCOUNT_ID="${{ secrets.HEDERA_ACCOUNT_ID }}"
# ENV HEDERA_PRIVATE_KEY="${{ secrets.HEDERA_PRIVATE_KEY }}"
# ENV HEDERA_NETWORK="${{ secrets.HEDERA_NETWORK }}"
# ENV PINATA_API_KEY="${{ secrets.PINATA_API_KEY }}"
# ENV PINATA_API_SECRET="${{ secrets.PINATA_API_SECRET }}"

# CMD ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY . .

RUN ./mvnw package

FROM eclipse-temurin:21-jdk AS runner

WORKDIR /app

# Copy the packaged JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set environment variables
ENV HEDERA_ACCOUNT_ID=${HEDERA_ACCOUNT_ID}
ENV HEDERA_PRIVATE_KEY=${HEDERA_PRIVATE_KEY}
ENV HEDERA_NETWORK=${HEDERA_NETWORK}
ENV PINATA_API_KEY=${PINATA_API_KEY}
ENV PINATA_API_SECRET=${PINATA_API_SECRET}

EXPOSE 8000

# Run the application
CMD ["java", "-jar", "app.jar"]

