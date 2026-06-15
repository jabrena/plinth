Feature: Validate changes from usage of Java Docker container skill

Background:
  Given the skill "706-technologies-containers-docker"
  And the folder "examples/frameworks/quarkus" has no git changes

@acceptance-test
Scenario: Review Quarkus Java Dockerfiles for secure container defaults
  Given the Dockerfile "examples/frameworks/quarkus/src/main/docker/Dockerfile.jvm"
  And the Docker ignore file "examples/frameworks/quarkus/.dockerignore"
  And the user request is "Review this Java Dockerfile for security, reproducibility, and runtime readiness"
  And the local generated skill path ".agents/skills/706-technologies-containers-docker"
  And the folder "examples/frameworks/quarkus" has no git changes
  When the skill ".agents/skills/706-technologies-containers-docker" is applied to the Docker artifacts
  Then the skill reads "references/706-technologies-containers-docker.md"
  And the skill keeps recommendations at the Dockerfile, image-build, and runtime-container layer
  And the skill reviews multi-stage build structure, Maven dependency caching, deterministic artifact copies, "jlink" suitability, and ".dockerignore" coverage
  And the skill reviews base image selection, micro runtime distribution trade-offs, pinned version or digest policy, image labels, and explicit entrypoints
  And the skill reviews JVM container memory flags, CPU limits, startup behavior, graceful shutdown signals, time zones, and observable startup failures
  And the skill rejects secrets in image layers, labels, build arguments, Maven settings, or logs
  And the skill recommends non-root execution, least-privilege file permissions, and reduced runtime attack surface
  And the skill reports vulnerability scanning, SBOM, health check, logging, image size, and CI verification follow-ups
  And the skill asks a clarifying question before editing container artifacts if Java version, build tool, deployment platform, base-image policy, registry policy, or scanner requirements are material and missing
  And the folder "examples/frameworks/quarkus" has no git changes unless the user explicitly requested Docker artifact edits
  And any git changes produced during skill execution and verification are reset
