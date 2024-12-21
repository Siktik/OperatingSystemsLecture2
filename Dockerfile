# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/InterProcessOne-jar-with-dependencies.jar /app/app.jar


# Define the entrypoint command
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
