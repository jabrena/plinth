title=What's new in Cursor rules for Java 0.15.0?
date=2026-06-01
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What is the purpose?

An opinionated, AI-native workflow for evolving modern Java Enterprise `SDLC` practices through reusable `Skills`, `Agents`, and `MCP servers`.

It helps you answer the `Five Whys` when your team needs to evolve a Java-based product or service:

![](/cursor-rules-java/images/2026/6/scope.png)

Once the idea is clear, you can implement it in a structured way:

![](/cursor-rules-java/images/2026/6/workflow.png)

Thanks to our community members in `Singapore`, `Hanoi`, `Hong Kong`, `Milan`, and `New York`. 👋👋👋

**Note:** [If you love this project and use it, you can support it to help pay the token bills.](https://github.com/sponsors/jabrena)

## What's new in this release?

### Improved security validations in the CI pipeline

All skills are validated on every commit using a multi-scanner workflow:

- **[`skill-check@latest`](https://github.com/thedaviddias/skill-check) by David Dias** validates the structure and formatting of every generated `SKILL.md` file. It runs as a required CI gate and fails the workflow when a skill does not follow the expected skill format.
- **[`Cisco AI Skill Scanner`](https://github.com/cisco-ai-defense/skill-scanner) by Cisco AI Defense** analyzes every generated skill recursively with behavioral scanning enabled. It runs as a required CI gate using the `strict` policy and fails the workflow on `high` severity findings.
- **[`SkillSpector`](https://github.com/NVIDIA/SkillSpector) by NVIDIA** performs an additional static security review of the generated skills.

Rules derived from all three scanners are pending.

### Improvements in Java Enterprise Frameworks

In this release, the project continues adding new features to improve support for `Spring Boot`, `Quarkus` & `Micronaut`. The new skills added for all frameworks cover the following aspects:

- **Data Validation:** [`@303-frameworks-spring-boot-validation`](https://www.skills.sh/jabrena/cursor-rules-java/303-frameworks-spring-boot-validation), [`@403-frameworks-quarkus-validation`](https://www.skills.sh/jabrena/cursor-rules-java/403-frameworks-quarkus-validation) & [`@503-frameworks-micronaut-validation`](https://www.skills.sh/jabrena/cursor-rules-java/503-frameworks-micronaut-validation)
- **Security:** [`@304-frameworks-spring-boot-security`](https://www.skills.sh/jabrena/cursor-rules-java/304-frameworks-spring-boot-security), [`@404-frameworks-quarkus-security`](https://www.skills.sh/jabrena/cursor-rules-java/404-frameworks-quarkus-security) & [`@504-frameworks-micronaut-security`](https://www.skills.sh/jabrena/cursor-rules-java/504-frameworks-micronaut-security)
- **Kafka:** [`@314-frameworks-spring-kafka`](https://www.skills.sh/jabrena/cursor-rules-java/314-frameworks-spring-kafka), [`@414-frameworks-quarkus-kafka`](https://www.skills.sh/jabrena/cursor-rules-java/414-frameworks-quarkus-kafka) & [`@514-frameworks-micronaut-kafka`](https://www.skills.sh/jabrena/cursor-rules-java/514-frameworks-micronaut-kafka)
- **MongoDB:** [`@315-frameworks-spring-mongodb`](https://www.skills.sh/jabrena/cursor-rules-java/315-frameworks-spring-mongodb), [`@415-frameworks-quarkus-mongodb`](https://www.skills.sh/jabrena/cursor-rules-java/415-frameworks-quarkus-mongodb) & [`@515-frameworks-micronaut-mongodb`](https://www.skills.sh/jabrena/cursor-rules-java/515-frameworks-micronaut-mongodb)

### Improvements in Observability

Observability is an essential aspect of any application:

[![](/cursor-rules-java/images/2026/6/ms-concerns.webp)](https://developers.redhat.com/blog/2016/12/09/spring-cloud-for-microservices-compared-to-kubernetes)

In this release, we completed support for the main concepts related to observability: `Logging`, `Metrics` & `Tracing`:

- **Logging:** [`@181-java-observability-logging`](https://www.skills.sh/jabrena/cursor-rules-java/181-java-observability-logging)
- **Metrics:** [`@182-java-observability-metrics-micrometer`](https://www.skills.sh/jabrena/cursor-rules-java/182-java-observability-metrics-micrometer)
- **Tracing:** [`@183-java-observability-tracing-opentelemetry`](https://www.skills.sh/jabrena/cursor-rules-java/183-java-observability-tracing-opentelemetry)

Now, using any supported framework with OTEL, you can easily send continuous profiling data to Grafana:

![](/cursor-rules-java/images/2026/6/piroscope-demo.png)

https://grafana.com/oss/pyroscope/

### Improvements in Testing

In this release, fuzz testing was improved to make it easier to use: `@703-technologies-fuzzing-testing`.

![](/cursor-rules-java/images/2026/6/cats.png)

https://github.com/Endava/cats

### Better documentation 

The project now provides documentation in three languages:

- `English`
- `Chinese`
- `Spanish`

## Evolution of this project in Vercel's Skills registry

The ecosystem is large, so you will need your own criteria beyond whether this project fits your preferences.

This project focuses mainly on the following categories, and that scope continues to evolve:

- Maven, https://skills.sh/?q=maven
- Architecture ADR, https://skills.sh/?q=architecture+adr
- Java, https://skills.sh/?q=java
- Spring Boot, https://skills.sh/?q=spring-boot
- Quarkus, https://skills.sh/?q=quarkus
- Micronaut, https://skills.sh/?q=micronaut

## Using the project in IDEs

### Codex

![](/cursor-rules-java/images/2026/6/codex.png)

Recently, I tested Codex running on `VSCode`, and the experience was fantastic. Codex understands `AGENTS.md` and skills located in `.agents/skills`.

Using Codex, you can run the `Agents` provided by the project without any changes.

### Magic Quadrant update

Recently, Gartner updated the Magic Quadrant.

![](/cursor-rules-java/images/2026/6/magic-cuadrant-agents.jpg)

https://cursor.com/lp/2026-gartner-mq

## The project in events

### The experience in Codemotion Madrid 2026

Last month, the project was explained and used at the tech conference `Codemotion Madrid 2026`. The workshop had sold out a few weeks before, and participants scored the session 4.5/5.

[![](/cursor-rules-java/images/2026/6/codemotion-madrid-workshop.jpg)](https://conferences.codemotion.com/madrid/)

**Slides:** https://jabrena.github.io/cursor-rules-java/codemotion-madrid-2026/index.html

### The experience in JMad 2026

Every year, `MadridJUG` organizes `JMad`, a Java tech event using `OpenSpace`.

[![](/cursor-rules-java/images/2026/6/IMG_8268.jpg)](https://jmad.madridjug.es/)

`JMad` is a nice opportunity to exchange ideas and have good debates about the topics Java people want to discuss.

This year, the agenda was:

![](/cursor-rules-java/images/2026/6/IMG_2584.webp)

This year, several AI topics were discussed, including `AI Governance` and how to manage `Skills` in your team, unit, or company.

Further information: https://jmad.madridjug.es/
