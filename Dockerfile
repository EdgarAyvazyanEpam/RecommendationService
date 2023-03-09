FROM openjdk:17-oracle
COPY target/recommendation-service-0.0.1-SNAPSHOT.jar recommendation-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "recommendation-service-0.0.1-SNAPSHOT.jar"]
MAINTAINER Edgar Ayvazyan <edgar_ayvazyan@epam.com>
