FROM openjdk:18-alpine

COPY target/E-commerce-Project-0.0.1-SNAPSHOT.jar /app/E-commerce-Project-0.0.1-SNAPSHOT.jar

WORKDIR /app


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "E-commerce-Project-0.0.1-SNAPSHOT.jar"]
