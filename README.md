# Cursor AI rules for Java

## Stargazers over time
[![Stargazers over time](https://starchart.cc/jabrena/cursor-rules-java.svg?variant=light)](https://starchart.cc/jabrena/cursor-rules-java)

## Motivation

Modern Java IDEs, such as **Cursor AI**, provide ways to customize how the `Agent model` behaves using reusable and scoped instructions. In cursor, the way to do it is named `Cursor rule` and you could see it as a `System prompt` if you use a generic term. This repository provides a collection of Cursor rules designed for Java development.

## What is a System prompt?

A system prompt is a set of instructions given to an AI model that defines how it should behave, what role it should take on, and what guidelines it should follow when responding to users. Think of it as the "operating manual" that shapes the AI's personality, capabilities, and boundaries.

[Further information](./docs/cursor-interaction-sequence.png)

## How to use the Cursor rules?

Using the Cursor rules is straightforward: simply `drag and drop` the cursor rule that you need into the chat textbox where you are typing your `User prompt`.

⚠️ Currently, the cursor rules are released with the [manual scope](https://docs.cursor.com/context/rules#rule-type) on purpose by design to mitigate potential negative performance impact in communications with **The Cursor platform**.

## Cursor Rules

### Build system rules (Maven)

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [110-java-maven-best-practices](.cursor/rules/110-java-maven-best-practices.mdc) | Analyze your `pom.xml` and apply Maven best practices | **Prompt:** `Help me to review the pom.xml following the best practices for dependency management and directory structure use the cursor rule @110-java-maven-best-practices` **Note:** Add in the context the `pom.xml` which you want to generate the documentation. | This cursor rule is applied automatically without any interaction with the Software engineer. |
| [111-java-maven-deps-and-plugins](.cursor/rules/111-java-maven-deps-and-plugins.mdc) | Update your `pom.xml` with Maven Dependencies & Plugins | **Prompt:** `Can you improve the pom.xml using the cursor rule @111-java-maven-deps-and-plugins` **Note:** Add in the context the `pom.xml` which you want to generate the documentation. |  Using a Conversational approach, the Software engineer will interact with the cursor rule to update the `pom.xml`. |
| [112-java-maven-documentation](.cursor/rules/112-java-maven-documentation.mdc) | Create a Maven Documentation with the file `README-DEV.md` | **Prompt:** `Generate developer documentation with essential Maven commands using @112-java-maven-documentation` **Note:** Add in the context the `pom.xml` which you want to generate the documentation. | This cursor rule is applied automatically without any interaction with the Software engineer. |

### Design rules

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [121-java-object-oriented-design](.cursor/rules/121-java-object-oriented-design.mdc) | Take another point of view with an Object Oriented Design of your development | **Prompt:** `Review my code for object-oriented design using the cursor rule @121-java-object-oriented-design` **Note:** Add in the context a package to improve the design. | This cursor rule is applied automatically without any interaction with the Software engineer. It is an interesting prompt to see what alternatives offer the model for your package. |
| [122-java-type-design](.cursor/rules/122-java-type-design.mdc) | Review the Type Design in your development | **Prompt:** `Help me improve my type design using the cursor rule @122-java-type-design` **Note:** Add in the context a package to improve the design. | This cursor rule is applied automatically without any interaction with the Software engineer. It is an interesting prompt to see what alternatives offer the model for your package. |

### Coding rules

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [123-java-general-guidelines](.cursor/rules/123-java-general-guidelines.mdc) | Apply general purpose Java guidelines | **Prompt:** `Review my code for general Java best practices using the cursor rule @123-java-general-guidelines` **Note:** Add a package in the context. | Non conversational cursor rule. |
| [124-java-secure-coding](.cursor/rules/124-java-secure-coding.mdc) | Review your coide with Secure Java Coding rules | **Prompt:** `Check my code for security issues using the cursor rule @124-java-secure-coding` **Note:** Add a package in the context. | Non conversational cursor rule. |
| [125-java-concurrency](.cursor/rules/125-java-concurrency.mdc) | Improve your code with Concurrency rules | **Prompt:** `Review my code for concurrency best practices using the cursor rule @125-java-concurrency` **Note:** Add a class or package which consider that it could bye improved by the cursor rule. | Non conversational cursor rule. |
| [126-java-logging](.cursor/rules/126-java-logging.mdc) | Apply logging guidelines in your development | **Prompt:** `Help me improve logging using the cursor rule @126-java-logging` **Note:** Add a class or package which consider that it could bye improved by the cursor rule. | This cursor rule is applied automatically without any interaction with the Software engineer. |

### Testing rules

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [131-java-unit-testing](.cursor/rules/131-java-unit-testing.mdc) | Apply Unit Testing best practices | **Prompt:** `Can improve the unit tests using the cursor rule @131-java-unit-testing` **Note:** Add a class or package which consider that it could bye improved by the cursor rule. | This cursor rule is applied automatically without any interaction with the Software engineer. |

### Refactoring rules

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [141-java-refactoring-with-modern-features](.cursor/rules/141-java-refactoring-with-modern-features.mdc) | Add Modern Java Features in your development | **Prompt:** `Refactor my code to use modern Java features using the cursor rule @141-java-refactoring-with-modern-features` **Note:** Add a class or package which consider that it could bye improved by the cursor rule. | Non conversational cursor rule. |
| [142-java-functional-programming](.cursor/rules/142-java-functional-programming.mdc) | Add Functional Programming style in your development |  **Prompt:** `Refactor my code to use functional programming using the cursor rule @142-java-functional-programming` **Note:** Add a class or package which consider that it could bye improved by the cursor rule. | This cursor rule is applied automatically without any interaction with the Software engineer. |
| [143-java-data-oriented-programming](.cursor/rules/143-java-data-oriented-programming.mdc) | Add Data Oriented Programmin in your development |  **Prompt:** `Refactor my code to use data oriented programming using the cursor rule @143-java-data-oriented-programming` **Note:** Add a class or package which consider that it could bye improved by the cursor rule. | This cursor rule is applied automatically without any interaction with the Software engineer. |

### Performance rule (Jmeter)

| Activity | Description | Prompt | Notes |
|----------|------|--------|-------|
| [151-java-performance-jmeter](.cursor/rules/151-java-performance-jmeter.mdc) | Run a peformance test with Jmeter | **Prompt:** `Add JMeter performance testing to this project using @151-java-performance-jmeter.mdc` **Note:** You could ask the model to create a JMeter based on a RestController/Resource. Example: `Can you create a Jmeter file based on the restcontroller in the path src/test/resources/jmeter/load-test.jmx?` | This cursor rule is applied automatically without any interaction with the Software engineer. If you create a Jmeter file with the model, review the generation, sometimes it is necessary to hammer a bit. |

### Profiling rules (Async profiler)

| Activity | Description | Prompt | Notes |
|----------|-------------|--------|-------|
| [161-java-profiling-detect](.cursor/rules/161-java-profiling-detect.mdc) | Profile your development in runtime and collect evidences to be analyzed later. | **Prompt:** `My Java application has performance issues - help me set up comprehensive profiling process using @161-java-profiling-detect.mdc and use the location YOUR-DEVELOPMENT/profiler` **Note:** Replace YOUR-DEVELOPMENT with your actual development path. Example: examples/spring-boot-memory-leak-demo/profiler | Non conversational cursor rule. The Cursor rule will generate 2 scripts. One script designed to run your development with the right JVM flags for profiling and the second scripts will ask few questions about what problem do you want to solve/analyze over one particular PID.  |
| [162-java-profiling-analyze](.cursor/rules/162-java-profiling-analyze.mdc) | Analyze results from previous step and generate reports with the analysis results.| **Prompt:** `Analyze the results located in YOUR-DEVELOPMENT/profiler and use the cursor rule @162-java-profiling-analyze` **Note:** Replace YOUR-DEVELOPMENT with your actual development path. Example: examples/spring-boot-memory-leak-demo/profiler | Non conversational cursor rule. |
| - | Code Refactoring from suggestions from analysis | `Can you apply the solutions from @profiling-solutions-yyyymmdd.md in @/info to mitigate bottlenecks` | Make a refactoring with the notes from the analysis |
| [164-java-profiling-compare](.cursor/rules/164-java-profiling-compare.mdc) | Compare results comparing results before and after applying changes in the code | **Prompt:** `Review if the problems was solved with last refactoring using the reports located in @/results with the cursor rule 154-java-profiling-compare.mdc` **Note:**  Put in the context the folder with the results | This cursor rule is applied automatically without any interaction with the Software engineer. |

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

## Changelog

- Review the [CHANGELOG](./CHANGELOG.md) for further details

## Examples

The rules was tested with the following examples:

- [General: Maven Java project](./examples/maven-demo/README.md)
- [Microservices: Spring Boot application](./examples/spring-boot-demo/implementation/README.md)
- [Microservices: Spring Boot application with Memory leaks](./examples/spring-boot-memory-leak-demo/README.md)
- [Microservices: Spring Boot application with Performance Bottleneck](./examples/spring-boot-performance-bottleneck-demo/README.md)
- [Microservices: Spring Boot application with JMeter Load Testing](./examples/spring-boot-jmeter-demo/README.md)
- [Microservices: Quarkus application](./examples/quarkus-demo/README.md)
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
- https://github.com/jabrena/spml
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
