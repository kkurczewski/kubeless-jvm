FROM openjdk:8-jdk-slim

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

ADD target/lib /lib

ARG JAR_FILE

ADD target/${JAR_FILE} /app.jar

ENV MOD_ROOT_PATH "/kubeless"