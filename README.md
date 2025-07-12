# Cursor AI rules for Java

## Stargazers over time
[![Stargazers over time](https://starchart.cc/jabrena/cursor-rules-java.svg?variant=light)](https://starchart.cc/jabrena/cursor-rules-java)

[![CI Builds](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml/badge.svg)](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml)

## Motivation

Modern Java IDEs, such as **Cursor AI**, provide ways to customize how the `Agent model` behaves using reusable and scoped instructions. In cursor, the way to do it is named `Cursor rule` and you could see it as a `System prompt` if you use a generic term. This repository provides a collection of Cursor rules designed for Java development.

## Goal

Provide a set of Interactive Cursor rules for Java that help software engineers in their daily work.

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
- Instructions
- Constraints
- Examples
- Output format
- Safeguards

With this structure in mind, the project uses an XML Schema to define the way that all System prompts are generated for Cursor AI. If you are interested in this area, you could review [the Schema](./generator/src/main/resources/pml.xsd).

**Note:** It is not necessary to add all parts in a prompt.

## Cursor Rules

Read the generated list of cursor rules for Java [here](./CURSOR-RULES-JAVA.md). The set of cursor rules covers aspects like `Build system based on Maven`, `Design`, `Coding`, `Testing`, `Refactoring`, `Performance testing with JMeter` & `Profiling with Async Profiler`.

## Getting started

If you are interested in getting the benefits of these cursor rules for Java, you can manually download this repository and copy the './cursor' folder and paste it into your repository, download the rules from [the last release](https://github.com/jabrena/cursor-rules-java/releases) in zip format or just delegate this task to a specific command-line tool based on **Jbang**:

```bash
sdk install jbang
# Add cursor rules for Java in ./cursor/rules
jbang --fresh setup@jabrena init --cursor https://github.com/jabrena/cursor-rules-java
```

Once you have installed the cursor rules:

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [100-java-cursor-rules-list](.cursor/rules/100-java-cursor-rules-list.mdc) | Create a comprehensive step-by-step guide for using cursor rules for Java | `Create an java development guide using the cursor rule @100-java-cursor-rules-list` | This cursor rule is applied automatically without any interaction with the Software engineer. |

Type the following prompt in the cursor chat:

![](./docs/getting-started-prompt.png)

```bash
Create an java development guide using the cursor rule @100-java-cursor-rules-list
```

## Limitations & Opportunities

From the beginning, you need to know that results provided by the interaction with the different `Cursor rules` are not deterministic due to the models' nature, but this fact should not be considered in a negative way because you could see it as an opportunity to run the rules multiple times and see how models offer additional points of view about your repository, package, or class in different ways to improve your Java software.

## Safety guards

The modern rules are designed with safety in mind so any change proposed by the models will be verified:

```xml
<safeguards>
    <safeguards-list>
        <safeguards-item>verify changes with the command: `mvn validate` or `./mvnw validate`</safeguards-item>
    </safeguards-list>
</safeguards>
```

And changes later will be reviewed by the Software engineer.

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

## Contribute

If you have new ideas to improve any of the current Cursor rules or add a new one, please fork the repo and send a PR.

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
