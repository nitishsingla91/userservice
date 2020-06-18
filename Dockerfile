FROM docker.io/openjdk:8

ENV AB_ENABLED off
ENV AB_JOLOKIA_AUTH_OPENSHIFT true
ENV JAVA_OPTIONS -Xmx256m 

EXPOSE 8110

RUN mkdir /deployments/ \
  && chmod -R 777 /deployments/
ADD target/user-service-0.0.1-SNAPSHOT.jar /deployments/
