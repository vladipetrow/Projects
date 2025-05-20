# Use Ubuntu as the build stage
FROM ubuntu:22.04 as builder

# Download & extract Zulu JRE (Use x86_64 if needed)
ADD https://cdn.azul.com/zulu/bin/zulu17.54.21-ca-crac-jre17.0.13-linux_x64.tar.gz /opt/
RUN cd /opt/ && tar -xzf zulu17.54.21-ca-crac-jre17.0.13-linux_x64.tar.gz && rm -rf zulu17.54.21-ca-crac-jre17.0.13-linux_x64.tar.gz

# Production stage
FROM ubuntu:22.04

# Copy JRE from the builder stage
COPY --from=builder /opt/zulu17.54.21-ca-crac-jre17.0.13-linux_x64 /opt/zulu-jre

# Set environment variables
ENV JAVA_HOME=/opt/zulu-jre
ENV PATH="$JAVA_HOME/bin:$PATH"

# Install dependencies
RUN apt-get -qq update && \
    apt-get -qq -y --no-install-recommends install locales tzdata && \
    echo "en_US.UTF-8 UTF-8" >> /etc/locale.gen && \
    locale-gen en_US.UTF-8 && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy Spring Boot JAR file (Make sure your JAR is built in the `target/` folder)
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
