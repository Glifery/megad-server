FROM openjdk:8-jdk-alpine
COPY target/smarthome-0.0.1-SNAPSHOT.jar app.jar
COPY google-tokens google-tokens
ENTRYPOINT ["java","-jar","/app.jar"]