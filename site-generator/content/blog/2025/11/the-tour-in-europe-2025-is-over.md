title=The European tour 2025 is over
date=2025-11-10
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## Introduction

Over the last few weeks, I had the privilege of sharing with the `Java community` a few ideas that could be very useful for `Java Software engineers` in their daily work or for organizations enhancing their `pipelines`.

Using these lines, I would like to acknowledge `Stephan Janssen` from `Devoxx`, `Luis Fabrício De Llamas` from `DevConverge`, `Christina Bergh` from `W-JAX` and the entire `Madrid JUG` team for their guidance during this tour 2025.

During those events, I presented ideas about a new generation of developer tools powered by AI and new concepts related to development.

In the different sessions, I talked generally about the `Cursor portfolio` and specifically about some products/services like `Cursor AI Desktop`, `Cursor Web`, `Cursor Cloud Agents API`, `Cursor Rules`, and the new frontier model `Cursor Composer 1` and I would not like to miss the opportunity to mention `Claude Code` as CLI tool and the models `Claude Sonnet 4` & `Claude Sonnet 4.5` which I used almost all the time during 2025.

Videos about the talks:

- https://www.youtube.com/watch?v=UJPWIz8eobc (30 Min)
- https://www.youtube.com/watch?v=-OSYyY17VzE (30 Min)
- https://entwickler.de/reader/player/w-jax-2025 (60 min)

and the associated Slides:

- https://jabrena.github.io/cursor-rules-java/dvbe25/index.html
- https://jabrena.github.io/101-cursor/

## Emerging new solutions from the Gemba

During the last 9 months using the previous tools mentioned before, a few ideas emerged which little by little are evolving and were introduced in the latest talks.

### PML

`PML, Prompt Markup Language`, an `XML Schema`, which is able to help software engineers in modeling User prompts & System prompts. Once the prompt is modelled in XML, it can be converted into a regular format frontier models use, Markdown. Using the guidelines by PML Schema, it is easier to not miss something important to be defined in a prompt for production environments. Recently, a new XML Schema was added to model Workflows.

**Problem it tries to solve:** Design good `User Prompts` & `System prompts` to mitigate ambiguity in Models' execution.

PML project has 2 different schemas, one schema which defines the diferent parts of any Prompt:

```xml
<!-- Root element for a prompt -->
<xs:element name="prompt">
    <xs:complexType>
        <xs:sequence>
            <xs:element ref="metadata" minOccurs="0"/>
            <xs:element ref="title" minOccurs="0"/>
            <xs:element ref="role" minOccurs="0"/>
            <xs:element ref="tone" minOccurs="0"/>
            <xs:element ref="context" minOccurs="0"/>
            <xs:element ref="goal"/>
            <xs:element ref="constraints" minOccurs="0"/>
            <xs:element ref="steps" minOccurs="0" />
            <xs:element ref="examples" minOccurs="0"/>
            <xs:element ref="output-format" minOccurs="0"/>
            <xs:element ref="safeguards" minOccurs="0"/>
            <xs:element ref="acceptance-criteria" minOccurs="0"/>
        </xs:sequence>
    </xs:complexType>
</xs:element>
```

You can see PML in action [here](https://github.com/jabrena/cursor-rules-java/tree/main/system-prompts-generator/src/main/resources).

Finally, here is a novel Schema dedicated to Workflows:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<pml-workflow xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:noNamespaceSchemaLocation="https://jabrena.github.io/pml/schemas/0.3.0-SNAPSHOT/pml-workflow.xsd">
    model="default" repository="https://github.com/jabrena/wjax25-demos"
    timeout="5m" fallback-src="fallback-prompt.xml">
        <prompt src="pml-java25-installation.xml" />
        <prompt src="pml-hello-world-java.xml" />
    </sequence>
</pml-workflow>
```

This second Schema was designed as a consequence of the evolution of Churrera`s project.

https://github.com/jabrena/pml

### Cursor rules for Java

`System prompts for Java (Aka Cursor rules for Java)` is a collection of System prompts for Java that help software engineers in their daily programming work & pipelines. The available System prompts for Java cover aspects like Maven-based build systems, Design, Coding, Testing, Refactoring & JMH Benchmarking, Performance testing with JMeter, Profiling with Async profiler/OpenJDK tools, Documentation & Diagrams.

This project has good traction in the JVM community with a special popularity in Asia.

```bash
╔══════════════════════════════════════════════════════════════╗
║ ┌──────────────────────────────────────────────────────────┐ ║
║ │ Tu feedback es importante para evolucionar este proyecto │ ║
║ │    Your feedback is important to evolve this project     │ ║
║ │                您的反馈对本项目的发展至关重要                 │ ║
║ │           https://forms.gle/TpNXENjmu45wuXoi6            │ ║
║ └──────────────────────────────────────────────────────────┘ ║
╚══════════════════════════════════════════════════════════════╝
```
https://forms.gle/TpNXENjmu45wuXoi6

Recently, this technique was identified in the latest `Thoughtworks Radar #33`: **Curated shared instructions for software teams**

[![](/cursor-rules-java/images/11/thoughtworks-radar-system-prompts.jpg)](https://www.thoughtworks.com/radar/techniques/curated-shared-instructions-for-software-teams)

**Problem it tries to solve:** Maintain a specialized collection of system prompts for Software development in Java.

This project uses PML under the hood to model all System prompts in homogeneous Markdown syntax. Read the file [CURSOR-RULES-JAVA.md](https://github.com/jabrena/cursor-rules-java/blob/main/CURSOR-RULES-JAVA.md) to understand all possibilities.

https://github.com/jabrena/cursor-rules-java

### Churrera, tasks like churros!

Churrera is a CLI tool designed to operate with `Cursor Cloud Agents API` easily. The Cloud Agents API (Beta) allows you to programmatically create and manage AI-powered coding agents that work autonomously on your repositories.

![](/cursor-rules-java/images/11/churrera-2.png)

**Problem it tries to solve:** Enhance your pipelines with `AI Glue`.

**Use cases:** `Automate repetitive Java coding tasks` (with TDD if required), `Continuous documentation`, `Continuous Profiling`. You could refactor the results using `System prompts` from the project `Cursor rules for Java` or use your own System prompts. Your creativity defines your limits.

**Note:** At the moment, the unique service which provides this kind of APIs is `Cursor Cloud agents API`. If new alternatives exist in the future, I would be happy to implement a factory to support more alternatives.

https://github.com/jabrena/churrera

## Observations in Devoxx 2025

![](/cursor-rules-java/images/11/devoxx-be-room-4.jpg)

In 2025, the entire JVM industry is moving around AI and if you attended the different sessions in Devoxx, you may feel it. In the conference, I listened to the different proposals from the most used frameworks `Spring` & `Quarkus` in the Java industry and in this year, I observed that there exists a real parity between Spring ecosystem and Quarkus ecosystem in terms of talks. Congratulations to `Mario Fusco`, `Georgios Andrianakis`, `Clement Escoffier`, `Rod Johnson` & `Christian Tzolov` for their hard work. I would not like to forget the nice proposal from `Akka` which shared the new features oriented to `Agents`.

![](/cursor-rules-java/images/11/devoxx-quarkus-agentic-talk.png)

**Can Spring AI or Langchain4j use PML or Cursor rules for Java?**

Yes, for sure. Happy to talk with `Spring AI` team or `Quarkus` team in the future.

### What talks I loved in Devoxx BE 2025?

**Agentic AI:**

- https://m.devoxx.com/events/dvbe25/talks/6211/from-llm-orchestration-to-autonomous-agents-agentic-ai-patterns-with-langchain4j
- https://m.devoxx.com/events/dvbe25/talks/6210/agentic-ai-patterns
- https://m.devoxx.com/events/dvbe25/talks/5912/panel-discussion-langchain4j-turns-two-what-weve-learned-and-whats-next
- https://m.devoxx.com/events/dvbe25/talks/24552/build-an-mcp-server-with-java
- https://m.devoxx.com/events/dvbe25/talks/4715/the-power-of-cursor-rules-in-java-enterprise-development
- https://m.devoxx.com/events/dvbe25/talks/47103/akka-an-enterprise-runtime-for-resilient-agentic-ai
- https://m.devoxx.com/events/dvbe25/talks/3261/bootiful-spring-ai

**Java:**

- https://m.devoxx.com/events/dvbe25/talks/22144/loom-in-jdk-25-virtual-threads-structured-concurrency-and-scoped-values
- https://m.devoxx.com/events/dvbe25/talks/23186/weather-the-storm-how-value-classes-will-enhance-java-performance
- https://m.devoxx.com/events/dvbe25/talks/23195/pattern-matching-under-the-microscope
- https://m.devoxx.com/events/dvbe25/talks/46055/serialization-20-a-marshalling-update
- https://m.devoxx.com/events/dvbe25/talks/4716/structured-concurrency-in-action

**Frameworks:**

- https://m.devoxx.com/events/dvbe25/talks/6238/whats-new-in-spring-modulith

## Observations in W-JAX-2025

![](/cursor-rules-java/images/11/wjax-atlanta-room.png)

In this case, I visited the conference for a shorter time so my observations were limited, but in the conference the talks put focus on AI also. In the another hand, I observed less presence of Quarkus talks, in Germain Speakers continue with more experience in Spring. Gathering feedback from my talk, many `Java Software engineers` in Germany are starting to use this kind of modern tools but in general, the usage is not massive.

![](/cursor-rules-java/images/11/crossing-the-chasm.png)

### What talks I loved in W-JAX 2025?

**Cloud:**

- https://jax.de/cloud-container-serverless/ingress-istio-kubernetes-gateway-api/

**Observability:**

- https://jax.de/serverside-enterprise-java/observability-spring-boot-actuator-micrometer-opentelemetry/

**Security:**

- https://jax.de/performance-security/spring-authorization-server/
- https://jax.de/performance-security/saml-oauth-openid-connect

**Web:**

- https://jax.de/web-development-javascript/server-side-web-applications-htmx-spring-boot

**Testing:**

- https://jax.de/performance-security/test-doubles-mocking-frameworks

**Java:**

- https://jax.de/core-java-jvm-languages/exploring-java-util-concurrent
- https://jax.de/core-java-jvm-languages/virtual-threads-java-workshop

**Note:** Remember that you can register [here](https://entwickler.de/reader/player/w-jax-2025) to watch the sessions.

## Conclusions

It was a great experience to talk to approximately 500 Java software engineers in Benelux & Germany about Java & AI. Throughout the different sessions, I was able to receive useful feedback about the talks, topics, and the related open source projects. Now it is time to return to the desk to think and evolve these solutions.

Many thanks 🙏🙏🙏

## References

- https://m.devoxx.com/events/dvbe25/schedule
- https://m.devoxx.com/events/dvbe25/talks/4708/101-cursor-ai-learning-to-use-for-java-enterprise-projects
- https://m.devoxx.com/events/dvbe25/talks/4715/the-power-of-cursor-rules-in-java-enterprise-development
- https://www.devconvergeeurope.com/
- https://jax.de/munich/program-munich/
- https://jax.de/generative-ai-ecosystem/cursor-ai-101-java-enterprise/
- https://www.thoughtworks.com/radar/techniques/curated-shared-instructions-for-software-teams
