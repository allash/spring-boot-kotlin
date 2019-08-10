FROM openjdk:11.0-jdk

ENV JAVA_OPTS=""

COPY build/libs/piperbike-server-0.1.0.jar /deploy/app.jar
ENTRYPOINT java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /deploy/app.jar