# CATS OpenAPI fuzzer (https://github.com/Endava/cats) — uberjar on Temurin JRE.
ARG CATS_VERSION=13.8.0
FROM eclipse-temurin:25-jre-jammy

ARG CATS_VERSION
RUN mkdir -p /opt/cats \
  && apt-get update \
  && apt-get install -y --no-install-recommends curl ca-certificates \
  && rm -rf /var/lib/apt/lists/* \
  && curl -fsSL "https://github.com/Endava/cats/releases/download/cats-${CATS_VERSION}/cats_uberjar_${CATS_VERSION}.tar.gz" \
  | tar -xz -C /opt/cats

WORKDIR /workspace
ENTRYPOINT ["java", "-jar", "/opt/cats/cats.jar"]
