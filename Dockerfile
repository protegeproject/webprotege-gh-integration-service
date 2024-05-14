FROM openjdk:21
MAINTAINER protege.stanford.edu

ARG JAR_FILE
COPY target/${JAR_FILE} webprotege-gh-integration-service.jar
ENTRYPOINT ["java","-jar","/webprotege-gh-integration-service.jar"]