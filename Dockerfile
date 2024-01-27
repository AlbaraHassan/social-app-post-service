FROM maven:3.8.5-openjdk-17 as build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/post-service-0.0.1-SNAPSHOT.jar post-service.jar
EXPOSE 8081

ENTRYPOINT ["java","-jar","post-service.jar"]
