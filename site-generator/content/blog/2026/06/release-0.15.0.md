title=What's new in Cursor rules for Java 0.15.0?
date=2026-06-01
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What is the purpose?

An opinionated AI-native workflow for evolving modern Java Enterprise `SDLC` practices through reusable `Skills`, `Agents`, and `MCP servers`.

Where you could answer all `Five Whys` when your team need evolve your product/serve based on Java:

![](/cursor-rules-java/images/2026/6/scope.png)

And once you have clear the idea, you could implement in a structured way:

![](/cursor-rules-java/images/2026/6/workflow.png)

Thanks to our community members in `Singapore`, `Hanoi`, `Hong Kong`, `Milan` and `New York`. 👋👋👋

**Note:** [Remember that if you love this project and you use it, you could support it in order to pay the Token bills.](https://github.com/sponsors/jabrena)

## What's new in this release?

### Improved Security validations in the CI pipeline

All Skills are validated per commit using a multi-scanner workflow:

- **[`skill-check@latest`](https://github.com/thedaviddias/skill-check) by David Dias** validates the structure and formatting of every generated `SKILL.md` file. It runs as a required CI gate and fails the workflow when a skill does not follow the expected skill format.
- **[`Cisco AI Skill Scanner`](https://github.com/cisco-ai-defense/skill-scanner) by Cisco AI Defense** analyzes every generated skill recursively with behavioral scanning enabled. It runs as a required CI gate using the `strict` policy and fails the workflow on `high` severity findings.
- **[`SkillSpector`](https://github.com/NVIDIA/SkillSpector) by NVIDIA** performs an additional static security review of the generated skills.

ADD RULES FROM ALL 3 SCANNERS. PENDING

### Improvements in Java Enterprise Frameworks

In this new release, the project continue adding new features to improve the support for `Spring Boot`, `Quarkus` & `Micronaut`. The new skills added for all Frameworks cover the following aspects:

- **Data Validation:** [`@303-frameworks-spring-boot-validation`](https://www.skills.sh/jabrena/cursor-rules-java/303-frameworks-spring-boot-validation), [`@403-frameworks-quarkus-validation`](https://www.skills.sh/jabrena/cursor-rules-java/403-frameworks-quarkus-validation) & [`@503-frameworks-micronaut-validation`](https://www.skills.sh/jabrena/cursor-rules-java/503-frameworks-micronaut-validation)
- **Security:** [`@304-frameworks-spring-boot-security`](https://www.skills.sh/jabrena/cursor-rules-java/304-frameworks-spring-boot-security), [`@404-frameworks-quarkus-security`](https://www.skills.sh/jabrena/cursor-rules-java/404-frameworks-quarkus-security) & [`@504-frameworks-micronaut-security`](https://www.skills.sh/jabrena/cursor-rules-java/504-frameworks-micronaut-security)
- **Kafka:** [`@314-frameworks-spring-kafka`](https://www.skills.sh/jabrena/cursor-rules-java/314-frameworks-spring-kafka), [`@414-frameworks-quarkus-kafka`](https://www.skills.sh/jabrena/cursor-rules-java/414-frameworks-quarkus-kafka) & [`@514-frameworks-micronaut-kafka`](https://www.skills.sh/jabrena/cursor-rules-java/514-frameworks-micronaut-kafka)
- **MongoDB:** [`@315-frameworks-spring-mongodb`](https://www.skills.sh/jabrena/cursor-rules-java/315-frameworks-spring-mongodb), [`@415-frameworks-quarkus-mongodb`](https://www.skills.sh/jabrena/cursor-rules-java/415-frameworks-quarkus-mongodb) & [`@515-frameworks-micronaut-mongodb`](https://www.skills.sh/jabrena/cursor-rules-java/515-frameworks-micronaut-mongodb)

### Improvements in Observability

Observability is an essential aspect about any development:

[![](/cursor-rules-java/images/2026/6/ms-concerns.webp)](https://developers.redhat.com/blog/2016/12/09/spring-cloud-for-microservices-compared-to-kubernetes)

In this release, we have completed the support for all main concepts related to observability, `Logging`, `Metrics` & `Tracing`:

- **Logging:** [`@181-java-observability-logging`](https://www.skills.sh/jabrena/cursor-rules-java/181-java-observability-logging)
- **Metrics:** [`@182-java-observability-metrics-micrometer`](https://www.skills.sh/jabrena/cursor-rules-java/182-java-observability-metrics-micrometer)
- **Tracing:** [`@183-java-observability-tracing-opentelemetry`](https://www.skills.sh/jabrena/cursor-rules-java/183-java-observability-tracing-opentelemetry)

Now, using any Framework supported + OTEL, you could send Continous Profiling easily to Graphana:

![](/cursor-rules-java/images/2026/6/piroscope-demo.png)

https://grafana.com/oss/pyroscope/

### Improvements in Testing

In this release, Fuzzing testing was improved a bit to simplify the usage. `@703-technologies-fuzzing-testing`.

![](/cursor-rules-java/images/2026/6/cats.png)

https://github.com/Endava/cats

### Better documentation 

Now the project provide support the documentation in 3 languages:

- `English`
- `Chinese`
- `Spanish`

## Using the project in IDEs

### Codex

![](/cursor-rules-java/images/2026/6/codex.png)

Recently, I was testing Codex running on `VSCode` and the experience was fantastic, Codex understands `AGENTS.md` and Skills located in `.agents/skills`

Using Codex, you can run the `Agents` provided by the project without any change.

### Magic Quadrant update

Recently, Gartner updated the Magic quadrant

![](/cursor-rules-java/images/2026/6/magic-cuadrant-agents.jpg)

https://cursor.com/lp/2026-gartner-mq

## The project in events

### The experience in Codemotion Madrid 2026

In last Month, the project was explained and used in the tech conference `Codemotion Madrid 2026`. The workshop had `sold out` few weeks before and the participants scored the session with 4.5/5.

[![](/cursor-rules-java/images/2026/6/codemotion-madrid-workshop.jpg)](https://conferences.codemotion.com/madrid/)

**Slides:** https://jabrena.github.io/cursor-rules-java/codemotion-madrid-2026/index.html

### The experience in JMad 2026

Annualy `MadridJUG` organize `JMad` a Java Tech Event using `OpenSpace`.

[![](/cursor-rules-java/images/2026/6/IMG_8268.jpg)](https://jmad.madridjug.es/)

`JMad` is a nice opportunity to exchange ideas an run a good debate about topics that Java people want to talk.

This year, the Agenda was:

![](/cursor-rules-java/images/2026/6/IMG_2584.webp)

In this year, several topics about AI was present and one of the relevant topics was about `the AI Governance` and how to manage `the Skills` in your team/unit/company.