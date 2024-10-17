FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw package

FROM eclipse-temurin:21-jdk AS runner
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENV HEDERA_ACCOUNT_ID="${{ secrets.HEDERA_ACCOUNT_ID }}"
ENV HEDERA_PRIVATE_KEY="${{ secrets.HEDERA_PRIVATE_KEY }}"
ENV HEDERA_NETWORK="${{ secrets.HEDERA_NETWORK }}"
ENV PINATA_API_KEY="${{ secrets.PINATA_API_KEY }}"
ENV PINATA_API_SECRET="${{ secrets.PINATA_API_SECRET }}"

CMD ["java", "-jar", "app.jar"]
