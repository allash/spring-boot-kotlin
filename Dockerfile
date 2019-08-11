FROM openjdk:11.0-jdk

#RUN mkdir -p /deploy
#COPY scripts/entrypoint.sh /deploy/entrypoint.sh
#RUN chmod 755 /deploy/entrypoint.sh

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MetaspaceSize=100m"

COPY build/libs/piperbike-server-0.1.0.jar /deploy/app.jar
CMD java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /deploy/app.jar