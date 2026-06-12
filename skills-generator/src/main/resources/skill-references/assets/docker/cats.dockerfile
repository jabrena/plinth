# CATS OpenAPI fuzzer — trusted local uberjar on Temurin JRE.
FROM eclipse-temurin:25-jre-jammy

ARG CATS_JAR=cats.jar
RUN mkdir -p /opt/cats
COPY ${CATS_JAR} /opt/cats/cats.jar

WORKDIR /workspace
ENTRYPOINT ["java", "-jar", "/opt/cats/cats.jar"]
