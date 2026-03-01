---
# These are optional metadata elements. Feel free to remove any of them.
status: "implemented"
date: 2025-09-16
decision-makers: Juan Antonio Breña Mora / Project Lead
consulted: N/A
informed: N/A
---

# Website Generation with JBake

## Context and Problem Statement

The project requires a new website generation solution to publish web documents in an easy way using a solution which doesn't require resources like databases. The need is for a static site generator (SSG) that can handle the publishing of articles and documentation efficiently. The solution must be capable of choosing the right approach to publish web documents without the overhead of traditional database-driven content management systems.

## Decision Drivers

* Java solution compatibility
* Easy integration with Maven execution
* Static Site Generator functionality
* Easy customization of templates
* Capacity to use multiple input document formats (Markdown, HTML)
* Relatively fast execution performance

## Considered Options

* JBake
* Jekyll

## Decision Outcome

Chosen option: "JBake", because it has good template engines and is easy to integrate with Maven, which aligns with our Java-based development environment and build process requirements.

### Consequences

* Good, because it enables easy integration with Maven and later verification of integrity in GitHub Actions
* Good, because it provides support for popular Java template engines
* Good, because it offers relatively fast site generation performance
* Good, because it integrates seamlessly with our existing Java-based toolchain

### Confirmation

The solution has been deployed for several weeks without any issues, demonstrating its reliability and effectiveness in meeting the project's website generation requirements.

## Pros and Cons of the Options

### JBake

Java-based static site generator that integrates well with Maven-based projects.

* Good, because it provides easy integration with Maven
* Good, because it supports popular Java template engines
* Good, because it offers relatively fast site generation
* Bad, because the project has not been updated frequently in recent years
* Bad, because there are limited template options available

### Jekyll

Ruby-based static site generator with extensive community support.

* Good, because it is a popular solution with widespread adoption
* Good, because it has a huge community and extensive ecosystem
* Bad, because it is a non-Java solution that doesn't align with our technology stack
* Bad, because it is not easy to integrate with Maven build processes

## More Information

This decision supports the project's requirement for a lightweight, database-free solution for publishing technical documentation and articles. The implementation has proven successful with stable operation over several weeks of deployment.
