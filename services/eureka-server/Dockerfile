FROM maven:3.9.9-eclipse-temurin-21-alpine as BUILDER

WORKDIR /build

COPY pom.xml .

COPY src/ ./src/

RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app

COPY --from=BUILDER /build/target/*.jar ./app.jar
RUN apk add --no-cache curl
EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]