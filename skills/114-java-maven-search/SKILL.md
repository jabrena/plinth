---
name: 114-java-maven-search
description: Provides comprehensive guidance for searching and retrieving Maven components from Maven Central (repo1.maven.org): Search API queries, maven-metadata.xml, direct POM/JAR/sources/javadoc URLs, coordinate validation, and dependency insight from POMs. Use when the user needs to find, verify, or retrieve Maven dependencies, check versions, or work with groupId, artifactId, and version. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.14.0-SNAPSHOT
---
# Maven Central search and coordinates

Help users search Maven Central, resolve **groupId:artifactId:version**, read version history, and build correct download URLs. **What is covered:**

- Maven Central Search API (`solrsearch/select`) — text and coordinate queries
- Direct repository layout and `maven-metadata.xml`
- POM, JAR, `-sources.jar`, `-javadoc.jar` URL patterns
- Parsing POMs for direct dependencies; transitive trees via Maven/Gradle on the consumer project
- Output format: structured coordinates, tables, and verifiable HTTPS links

## Constraints

Verify coordinates against the Search API or repository responses before asserting availability. Prefer release versions unless snapshots are explicitly required.

- **VERIFY**: Do not invent GAVs — confirm via Search API or successful GET of metadata/POM
- **FORMAT**: Always express full coordinates as `groupId:artifactId:version` when a version is fixed
- **BEFORE APPLYING**: Read the reference for step-by-step workflows, query syntax, and URL patterns

## When to use this skill

- Search Maven Central
- Find Maven dependency
- Maven coordinates
- groupId artifactId version
- Latest version Maven
- maven-metadata.xml
- Download JAR from Maven Central
- Dependency tree transitive

## Reference

For detailed guidance, examples, and constraints, see [references/114-java-maven-search.md](references/114-java-maven-search.md).
