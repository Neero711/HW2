
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY config-server/pom.xml config-server/
COPY config-server/src/ config-server/src/
RUN mvn clean package -pl config-server -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/config-server/target/config-server-*.jar /app/config-server.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "config-service.jar"]