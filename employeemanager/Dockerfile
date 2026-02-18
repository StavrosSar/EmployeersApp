# ---- Build the Spring Boot JAR ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src src
RUN mvn -q -DskipTests package

# ---- Runtime: MySQL + Java in ONE container ----
FROM mysql:8.4

# Install Java runtime (try Java 21, fallback to 17)
RUN set -eux; \
    if command -v apt-get >/dev/null 2>&1; then \
      apt-get update; \
      (apt-get install -y --no-install-recommends openjdk-21-jre-headless || apt-get install -y --no-install-recommends openjdk-17-jre-headless); \
      rm -rf /var/lib/apt/lists/*; \
    elif command -v microdnf >/dev/null 2>&1; then \
      (microdnf install -y java-21-openjdk-headless || microdnf install -y java-17-openjdk-headless); \
      microdnf clean all; \
    elif command -v yum >/dev/null 2>&1; then \
      (yum install -y java-21-openjdk-headless || yum install -y java-17-openjdk-headless); \
      yum clean all; \
    else \
      echo "No supported package manager found to install Java." >&2; \
      exit 1; \
    fi

WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

COPY start.sh /usr/local/bin/start.sh
RUN chmod +x /usr/local/bin/start.sh

EXPOSE 8080 3306
CMD ["/usr/local/bin/start.sh"]
