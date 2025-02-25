# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/librarymanagementsystem.jar /app/librarymanagementsystem.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "/app/libraryManagementSystem.jar"]
