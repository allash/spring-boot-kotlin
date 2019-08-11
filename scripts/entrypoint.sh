#!/usr/bin/env sh

/usr/bin/java -XX:+UseContainerSupport -XX:MetaspaceSize=100m -jar /deploy/app.jar

