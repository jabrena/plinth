# Cursor AI rules for Java

## Stargazers over time
[![Stargazers over time](https://starchart.cc/jabrena/cursor-rules-java.svg?variant=light)](https://starchart.cc/jabrena/cursor-rules-java)

## Motivation

Modern Java IDEs, such as **Cursor AI**, provide ways to customize how the `Agent model` behaves using reusable and scoped instructions. This repository offers a collection of such Cursor rules specifically for Java development.

This collection of Cursor Rules for Java development, tries to enrich the developer experience when the Software engineer interact with LLMs daily.

## Cursor Rules

### Build systems rules

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [110-java-maven-best-practices](.cursor/rules/110-java-maven-best-practices.mdc) | Maven Best Practices | `Help me to review the pom.xml following the best practices for dependency management and directory structure use the cursor rule @110-java-maven-best-practices` | Add in the context the `pom.xml` which you want to generate the documentation |
| [111-java-maven-deps-and-plugins](.cursor/rules/111-java-maven-deps-and-plugins.mdc) | Maven Dependencies & Plugins | `Can you improve the pom.xml using the cursor rule @111-java-maven-deps-and-plugins` | Add in the context the `pom.xml` which you want to generate the documentation. Conversational approach |
| [112-java-maven-documentation](.cursor/rules/112-java-maven-documentation.mdc) | Maven Documentation | `Generate developer documentation with essential Maven commands using @112-java-maven-documentation` | Add in the context the `pom.xml` which you want to generate the documentation |

### Design rules

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [121-java-object-oriented-design](.cursor/rules/121-java-object-oriented-design.mdc) | Object Oriented Design | `Review my code for object-oriented design using the cursor rule @121-java-object-oriented-design` | Add in the context a package to improve the design |
| [122-java-type-design](.cursor/rules/122-java-type-design.mdc) | Type Design | `Help me improve my type design using the cursor rule @122-java-type-design` | Add in the context a package to improve the design |

### Coding rules

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [123-java-general-guidelines](.cursor/rules/123-java-general-guidelines.mdc) | General Java Guidelines | `Review my code for general Java best practices using the cursor rule @123-java-general-guidelines` | |
| [124-java-secure-coding](.cursor/rules/124-java-secure-coding.mdc) | Secure Java Coding | `Check my code for security issues using the cursor rule @124-java-secure-coding` | |
| [125-java-concurrency](.cursor/rules/125-java-concurrency.mdc) | Concurrency | `Review my code for concurrency best practices using the cursor rule @125-java-concurrency` | |
| [126-java-logging](.cursor/rules/126-java-logging.mdc) | Logging Guidelines | `Help me improve logging using the cursor rule @126-java-logging` | |

### Testing rules

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [131-java-unit-testing](.cursor/rules/131-java-unit-testing.mdc) | Unit Testing | `Can improve the unit tests using the cursor rule @131-java-unit-testing` | Add in the context a Test Class or the package |

### Refactoring rules

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [141-java-refactoring-with-modern-features](.cursor/rules/141-java-refactoring-with-modern-features.mdc) | Add Modern Java Features | `Refactor my code to use modern Java features using the cursor rule @141-java-refactoring-with-modern-features` | |
| [142-java-functional-programming](.cursor/rules/142-java-functional-programming.mdc) | Functional Programming | `Refactor my code to use functional programming using the cursor rule @142-java-functional-programming` | |
| [143-java-data-oriented-programming](.cursor/rules/143-java-data-oriented-programming.mdc) | Data Oriented Programming | `Refactor my code to use data oriented programming using the cursor rule @143-java-data-oriented-programming` | |

## Getting started

If you are interested in getting the benefits from these cursor rules, you can manually download this repository and copy the './cursor' folder and paste it into your repository, or delegate this task to a specific command-line tool based on **Jbang**:

```bash
sdk install jbang
# Add cursor rules for Java in ./cursor/rules
jbang --fresh setup@jabrena init --cursor https://github.com/jabrena/cursor-rules-java
```

Once you have installed the cursor rules:

| Phase | Role | Cursor Rule | Description |
|-------|------|-------------|-------------|
| Getting Started | All | [Create Java Development Guide](.cursor/rules/100-java-checklist-guide.mdc) | Cursor rule designed to help the user when using the whole set of cursor rules for Java in an easy way |

Type the following prompt in the cursor chat:

![](./docs/getting-started-prompt.png)

```bash
Create an java development guide using the cursor rule @100-java-checklist-guide
```

## Examples

The rules was tested with the following examples:

- [General: Maven Java project](./examples/maven-demo/README.md)
- [Microservices: Spring Boot application](./examples/spring-boot-demo/implementation/README.md)
- [Serverless: AWS Lambda](./examples/aws-lambda-hello-world/README.md)
- [Serverless: Azure Function](./examples/azure-function-hello-world/README.md)

## Java JEPS from Java 8

Java use JEPS as the vehicle to describe the new features to be added in the language. The repository review in a continuous way what JEPS could be improved any of the cursor rules present in this repository.

- [JEPS List](./docs/All-JEPS.md)

## What is the structure of a Cursor rule?

Review the [template](./docs/000-cursor-rule-template.md) for details.

## Contribute

If you have new ideas to improve any of the current Cursor rules or add a new one, please fork the repo and send a PR.

## References

- https://www.cursor.com/
- https://docs.cursor.com/context/rules
- https://docs.cursor.com/context/@-symbols/@-cursor-rules
- https://openjdk.org/jeps/0

## Cursor rules ecosystem

- https://github.com/jabrena/101-cursor
- https://github.com/jabrena/cursor-rules-methodology
- https://github.com/jabrena/cursor-rules-agile
- https://github.com/jabrena/cursor-rules-java
- https://github.com/jabrena/cursor-rules-spring-boot
- https://github.com/jabrena/cursor-rules-examples
- https://github.com/jabrena/cursor-rules-sandbox
- https://github.com/jabrena/plantuml-to-png-cli
- https://github.com/jabrena/setup-cli
- https://github.com/jabrena/jbang-catalog
