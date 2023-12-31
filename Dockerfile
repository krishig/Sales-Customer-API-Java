FROM maven:3.8.7 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install -DskipTests=true
FROM openjdk:17-jdk-alpine
COPY --from=build /home/app/target/KrishiG-0.0.1-SNAPSHOT.jar /usr/local/lib/KrishiG-0.0.1-SNAPSHOT.jar
ADD newrelic /home/app
ENV NEW_RELIC_APP_NAME="Java-API"
ENV NEW_RELIC_LOG_FILE_NAME="STDOUT"
ENTRYPOINT ["java","-javaagent:/home/app/newrelic.jar","-jar","/usr/local/lib/KrishiG-0.0.1-SNAPSHOT.jar"]
