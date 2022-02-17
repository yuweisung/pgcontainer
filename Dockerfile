
FROM openjdk:11-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ARG JAR_FILE=build/libs/*.jar
ENV JAVA_OPTS=$JAVA_OPTS -Djava.security.egd=file:/dev/./urandom
ADD ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar pgcontainer.jar
