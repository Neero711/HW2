
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/
COPY api-gateway/src/ api-gateway/src/
RUN mvn clean package -pl api-gateway-DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/api-gateway/target/api-gateway-*.jar /app/api-gateway.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]