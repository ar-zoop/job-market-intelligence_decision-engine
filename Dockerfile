# Build stage: produce executable JAR with Maven
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY pom.xml .
# Download dependencies (cached unless pom changes)
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -B -DskipTests

# Run stage: only the JAR
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /build/target/job-market-intelligence_decision-engine-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
