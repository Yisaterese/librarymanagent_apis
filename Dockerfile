# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file dynamically (any JAR in the target folder)
COPY target/*.jar /app/app.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "/app/app.jar"]
