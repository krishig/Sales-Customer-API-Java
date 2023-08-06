FROM maven:3.8.7 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install -DskipTests=true
FROM openjdk:8-jdk-alpine
COPY --from=build /home/app/target/KrishiG-0.0.1-SNAPSHOT.jar /usr/local/lib/KrishiG-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/KrishiG-0.0.1-SNAPSHOT.jar"]
