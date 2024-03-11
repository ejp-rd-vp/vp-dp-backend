FROM openjdk:18-jdk-alpine
EXPOSE 8095
RUN apk add sudo
RUN addgroup -S ejp && adduser -S ejp -G ejp
USER ejp:ejp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /opt/app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /opt/app.jar"]
