FROM maven:3.9.11-amazoncorretto-21-alpine AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

FROM maven:3.9.11-amazoncorretto-21-alpine AS runner
WORKDIR /app
COPY --from=builder /app/target/zlschat-api*.jar ./zlschat-api.jar
EXPOSE 8080
CMD ["java", "-jar", "zlschat-api.jar"]