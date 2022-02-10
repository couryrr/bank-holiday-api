FROM openjdk:17-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} bank-holiday-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/bank-holiday-0.0.1-SNAPSHOT.jar"]