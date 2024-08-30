FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/oncall-invext-0.0.1-SNAPSHOT.jar /app/oncall-invext-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/oncall-invext-0.0.1-SNAPSHOT.jar"]
