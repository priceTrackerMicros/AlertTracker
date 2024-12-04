# Step 1: Use OpenJDK 21 image as the base image
FROM openjdk:21-jdk-slim

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the Spring Boot JAR file into the container
COPY target/*.jar alert-tracker.jar

# Step 4: Expose the port on which the application runs (default for Spring Boot is 8080)
EXPOSE 8080

# Step 5: Run the Spring Boot application
CMD ["java", "-jar", "alert-tracker.jar"]
