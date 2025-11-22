###############################################
# Stage 1: Build the Spring Boot Application
###############################################

# Use Maven with Java 17 to build the application
FROM maven:3.8.8-eclipse-temurin-17 AS build

# Set working directory inside the image
WORKDIR /app

# Copy the Maven configuration file
COPY pom.xml .

# Copy the complete source code
COPY src ./src

# Build the application JAR (skips tests for faster build)
RUN mvn -B -DskipTests clean package


###############################################
# Stage 2: Runtime Environment for Spring Boot
###############################################

# Use a lightweight Java 17 runtime for running the app
FROM eclipse-temurin:17-jre

# Working directory where the JAR will run
WORKDIR /app

# Copy the JAR file from the build stage into runtime stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the Spring Boot application will run on
EXPOSE 8085

# Command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
