# Cursor AI rules for Java

## Stargazers over time
[![Stargazers over time](https://starchart.cc/jabrena/cursor-rules-java.svg?variant=light)](https://starchart.cc/jabrena/cursor-rules-java)

[![CI Builds](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml/badge.svg)](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml)

## Motivation

Modern Java IDEs, such as **Cursor AI**, provide ways to customize how the `Agent model` behaves using reusable and scoped instructions. In cursor, the way to do it is named `Cursor rule` and you could see it as a `System prompt` if you use a generic term. This repository provides a collection of Cursor rules designed for Java development.

## Goal

Provide a set of Interactive Cursor rules for Java that help software engineers in their daily work.

## Getting started

If you are interested in getting the benefits of this cursor rules for Java, you have different alternatives like: `Using this Git repository`, `Using the Zipped rules from latest release` or using a `JBang CLI` specialized in this task.

Read [the following document](./GETTING-STARTED.md) to start using this set of Cursor rules.

## What is a System prompt?

A system prompt is a set of instructions given to an AI model that defines how it should behave, what role it should take on, and what guidelines it should follow when responding to users. Think of it as the "operating manual" that shapes the AI's personality, capabilities, and boundaries.

## How to use the Cursor rules?

Using the Cursor rules is straightforward: simply `drag and drop` the cursor rule that you need into the chat textbox where you are typing your `User prompt`.

⚠️ Currently, the cursor rules are released with the [manual scope](https://docs.cursor.com/context/rules#rule-type) by design to mitigate potential negative performance impact on communications with **The Cursor platform**.

Review the following [sequence diagram](./docs/cursor-interaction-sequence.png) to understand the technical details.

## What is the structure of a System prompt?

According to the documentation from [Google Gemini](https://drive.google.com/file/d/1AbaBYbEa_EbPelsT40-vj64L-2IwUJHy/view), [Anthropic Claude](https://docs.anthropic.com/en/docs/build-with-claude/prompt-engineering/overview) & [OpenAI ChatGPT](https://chatgpt.com/share/686d1066-9e40-800b-ac7f-cc8df7e4c7d0), a prompt could be structured in the following way:

- Metadata
- Role
- Context
- Goal
- Tone
- Constraints
- Instructions
- Examples
- Output format
- Safeguards

With this structure in mind, the project uses an XML Schema to define the way that all System prompts are generated for Cursor AI. If you are interested in this area, you could review [the Schema](./generator/src/main/resources/pml.xsd).

**Note:** It is not necessary to add all parts in a prompt.

## Cursor Rules

Read the generated list of cursor rules for Java [here](./CURSOR-RULES-JAVA.md). The set of cursor rules covers aspects like `Build system based on Maven`, `Design`, `Coding`, `Testing`, `Refactoring`, `Performance testing with JMeter`, `Profiling with Async profiler, jps, jstack, jcmd & jstat` & `Documentation`.

## Constraints, Output format & Safety guards

The cursor rules in this repository follow [The Three-Node Quality Framework for AI Prompts](./docs/articles/prompt-quality-framework.md) which ensures both comprehensive responses and safe execution. This framework consists of three distinct pillars: **constraints**, **output-format** and **safeguards**. Each node operates at different phases of the AI interaction timeline, creating a defense-in-depth strategy.

The **constraints** act as gate-keeping mechanisms that define hard requirements and blocking conditions before any work begins - essentially asking "Can I start?" The **output-format** provides prescriptive guidance during execution, ensuring comprehensive coverage and organized responses by defining "What should I deliver?" Finally, **safeguards** implement protective measures throughout and after execution, continuously asking "Did it work safely?" This temporal flow from pre-execution validation to structured execution to continuous monitoring ensures quality at every stage.

This framework transforms AI from a general assistant into a specialized consultant with built-in quality controls and safety measures, making it particularly suitable for critical applications like Java software development. By embedding domain-specific expertise directly into the prompt structure, the cursor rules provide predictable, comprehensive, and safe interactions while reducing cognitive load for developers and ensuring system integrity throughout the development process.

## Limitations

### Lack of determinism

From the beginning, you need to know that results provided by the interaction with the different `Cursor rules` are not deterministic due to the models' nature, but this fact should not be considered negative. Software engineers do not always have the same idea to solve a problem, and you could find an analogy in this fact.

### Limits of interactions with Models

Models are able to generate code but they are not able to run code with your local data. In order to solve that limitation you can observe that a few prompts provide scripts to fix the gaps on the model side.

## Contribute

The whole set of cursor rules are autogenerated by XML files in the project `generator`.
[All XML files](./generator/src/main/resources/) use one Single XML Schema, [pml.xsd](./generator/src/main/resources/pml.xsd) and those XML documents are Transformed into Markdown with Frontmatter using a [Java class](./generator/src/main/java/info/jab/pml/CursorRulesGenerator.java) and a [XSL file](./generator/src/main/resources/cursor-rules.xsl) specialized in the Cursor rule format.

If you have the idea to contribute review the whole process in detail:

```bash
cd generator
./mvnw clean verify # Pass tests
./mvnw clean install # Pass test & copy new .mdc files into ./cursor/rules (The way to promote changes)
```

When you feel confident with the process, fork the repository and try to create new XML documents, Models will help you because a XML file is more rigid that natural lenguage and it has `a common vocabulary` to create prompts.

When you feel confident with the solution, send a PR.

## Examples

The rules were tested with the following examples:

- [General: Maven Java project](./examples/maven-demo/README.md)
- [Microservices: Spring Boot application](./examples/spring-boot-demo/implementation/README.md)
- [Microservices: Spring Boot application with Memory leaks](./examples/spring-boot-memory-leak-demo/README.md)
- [Microservices: Spring Boot application with Performance Bottleneck](./examples/spring-boot-performance-bottleneck-demo/README.md)
- [Microservices: Spring Boot application with JMeter Load Testing](./examples/spring-boot-jmeter-demo/README.md)
- [Microservices: Quarkus application](./examples/quarkus-demo/README.md)
- [Serverless: AWS Lambda](./examples/aws-lambda-hello-world/README.md)
- [Serverless: Azure Function](./examples/azure-function-hello-world/README.md)

[Here](./examples/README.md), you can see Scenarios using the Cursor rules for Java

## Architectural decision records, ADR

- [ADR-001: Generate Cursor Rules from XML Files](./docs/adr/ADR-001-generate-cursor-rules-from-xml-files.md)
- [ADR-002: Configure Cursor Rules Manual Scope](./docs/adr/ADR-002-configure-cursor-rules-manual-scope.md)

## Changelog

- Review the [CHANGELOG](./CHANGELOG.md) for further details

## Java JEPS from Java 8

Java uses JEPs as the vehicle to describe the new features to be added in the language. The repository continuously reviews which JEPs could improve any of the cursor rules present in this repository.

- [JEPS List](./docs/jeps/All-JEPS.md)

## References

- https://www.cursor.com/
- https://docs.cursor.com/context/rules
- https://docs.cursor.com/context/@-symbols/@-cursor-rules
- https://openjdk.org/jeps/0

## Cursor rules ecosystem

- https://github.com/jabrena/101-cursor
- https://github.com/jabrena/pml
- https://github.com/jabrena/cursor-rules-methodology
- https://github.com/jabrena/cursor-rules-agile
- https://github.com/jabrena/cursor-rules-java
- https://github.com/jabrena/cursor-rules-spring-boot
- https://github.com/jabrena/cursor-rules-examples
- https://github.com/jabrena/cursor-rules-sandbox
- https://github.com/jabrena/plantuml-to-png-cli
- https://github.com/jabrena/setup-cli
- https://github.com/jabrena/jbang-catalog

Powered by [Cursor](https://www.cursor.com/)
