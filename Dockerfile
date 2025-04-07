# FROM alpine/java:21-jdk
# ARG JAR_FILE=target/*.jar
# COPY ${JAR_FILE} app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]

# Use a base image that supports Java 21
FROM openjdk:21-jdk-slim AS build

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Build the project and create the JAR file
RUN mvn clean package

# Use a minimal image with Java to run the application
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
