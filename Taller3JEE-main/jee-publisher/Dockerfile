FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM quay.io/wildfly/wildfly:27.0.1.Final-jdk17
COPY --from=build /build/target/jee-publisher.war /opt/jboss/wildfly/standalone/deployments/jee-publisher.war
