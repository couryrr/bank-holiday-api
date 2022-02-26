FROM openjdk:17-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} bank-holiday.jar
ENTRYPOINT ["java","-jar","/bank-holiday.jar"]
