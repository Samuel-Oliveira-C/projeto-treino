# Stage 1: build with Maven (uses project in demo/)
FROM maven:3-openjdk-17 AS builder
WORKDIR /workspace
COPY demo/pom.xml ./pom.xml
COPY demo/src ./src
RUN mvn -B -DskipTests package

# Stage 2: smaller runtime image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
EXPOSE 8092
COPY --from=builder /workspace/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]