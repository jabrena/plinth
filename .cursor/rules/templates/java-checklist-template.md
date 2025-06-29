# Java Development Guide

Use the following process to improve the java development in some areas if required using the following set of Java Cursor Rules.

## Process Overview

### Step 1: Review the build system (Maven)

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [110-java-maven-best-practices](.cursor/rules/110-java-maven-best-practices.mdc) | Maven Best Practices | `Help me to review the pom.xml following the best practices for dependency management and directory structure use the cursor rule @110-java-maven-best-practices` | Add in the context the `pom.xml` which you want to generate the documentation |
| [111-java-maven-deps-and-plugins](.cursor/rules/111-java-maven-deps-and-plugins.mdc) | Maven Dependencies & Plugins | `Can you improve the pom.xml using the cursor rule @111-java-maven-deps-and-plugins` | Add in the context the `pom.xml` which you want to generate the documentation. Conversational approach |
| [112-java-maven-documentation](.cursor/rules/112-java-maven-documentation.mdc) | Maven Documentation | `Generate developer documentation with essential Maven commands using @112-java-maven-documentation` | Add in the context the `pom.xml` which you want to generate the documentation |

### Step 2: Design Principles

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [123-java-general-guidelines](.cursor/rules/123-java-general-guidelines.mdc) | General Java Guidelines | `Review my code for general Java best practices using the cursor rule @123-java-general-guidelines` | |
| [124-java-secure-coding](.cursor/rules/124-java-secure-coding.mdc) | Secure Java Coding | `Check my code for security issues using the cursor rule @124-java-secure-coding` | |
| [125-java-concurrency](.cursor/rules/125-java-concurrency.mdc) | Concurrency | `Review my code for concurrency best practices using the cursor rule @125-java-concurrency` | |
| [126-java-logging](.cursor/rules/126-java-logging.mdc) | Logging Guidelines | `Help me improve logging using the cursor rule @126-java-logging` | |

### Step 3: Coding Guidelines

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [123-java-general-guidelines](.cursor/rules/123-java-general-guidelines.mdc) | General Java Guidelines | `Review my code for general Java best practices using the cursor rule @123-java-general-guidelines` | |
| [124-java-secure-coding](.cursor/rules/124-java-secure-coding.mdc) | Secure Java Coding | `Check my code for security issues using the cursor rule @124-java-secure-coding` | |
| [125-java-concurrency](.cursor/rules/125-java-concurrency.mdc) | Concurrency | `Review my code for concurrency best practices using the cursor rule @125-java-concurrency` | |
| [126-java-logging](.cursor/rules/126-java-logging.mdc) | Logging Guidelines | `Help me improve logging using the cursor rule @126-java-logging` | |

### Step 4: Testing

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [131-java-unit-testing](.cursor/rules/131-java-unit-testing.mdc) | Unit Testing | `Can improve the unit tests using the cursor rule @131-java-unit-testing` | Add in the context a Test Class or the package |

### Step 5: Refactoring

| Cursor Rule | Description | Prompt | Notes |
|-------------|-------------|--------|-------|
| [141-java-refactoring-with-modern-features](.cursor/rules/141-java-refactoring-with-modern-features.mdc) | Add Modern Java Features | `Refactor my code to use modern Java features using the cursor rule @141-java-refactoring-with-modern-features` | |
| [142-java-functional-programming](.cursor/rules/142-java-functional-programming.mdc) | Functional Programming | `Refactor my code to use functional programming using the cursor rule @142-java-functional-programming` | |
| [143-java-data-oriented-programming](.cursor/rules/143-java-data-oriented-programming.mdc) | Data Oriented Programming | `Refactor my code to use data oriented programming using the cursor rule @143-java-data-oriented-programming` | |

### Step 6: Performance (Jmeter)

| Activity | Description | Prompt | Notes |
|----------|------|--------|-------|
| [151-java-performance-jmeter](.cursor/rules/151-java-performance-jmeter.mdc) | Run a peformance test with Jmeter | `Add JMeter performance testing to this project using @151-java-performance-jmeter.mdc` | You could ask the model to create a JMeter based on a RestController/Resource. Example: `Can you create a Jmeter file based on the restcontroller in the path src/test/resources/jmeter/load-test.jmx?` |

### Step 7: Profiling (Async profiler)

| Activity | Description | Prompt | Notes |
|----------|------|--------|-------|
| [161-java-profiling-detect](.cursor/rules/161-java-profiling-detect.mdc) | Measure problems | `My Java application has performance issues - help me set up comprehensive profiling process using @151-java-profiling-detect.mdc and use the location YOUR-DEVELOPMENT/profiler` | Replace YOUR-DEVELOPMENT with your actual development path. Example: examples/spring-boot-memory-leak-demo/profiler |
| [162-java-profiling-analyze](.cursor/rules/162-java-profiling-analyze.mdc) | Analyze results | `Analyze the results located in YOUR-DEVELOPMENT/profiler and use the cursor rule @152-java-profiling-analyze` | Replace YOUR-DEVELOPMENT with your actual development path. Example: examples/spring-boot-memory-leak-demo/profiler |
| - | Code Refactoring | `Can you apply the solutions from @profiling-solutions-yyyymmdd.md in @/info to mitigate bottlenecks` | Make a refactoring with the notes from the analysis |
| [164-java-profiling-compare](.cursor/rules/162-java-profiling-compare.mdc) | Analyze results | `Review if the problems was solved with last refactoring using the reports located in @/results with the cursor rule 154-java-profiling-compare.mdc` | Put in the context the folder with the results |

## Reference Table: Java Cursor Rules

| Rule Name | Cursor Rule | Description |
|-----------|-------------|-------------|
| Maven Best Practices | @110-java-maven-best-practices | Best practices for Maven dependency management and project structure |
| Maven Dependencies & Plugins | @111-java-maven-deps-and-plugins | Improve pom.xml with recommended plugins and dependencies |
| Object Oriented Design | @121-java-object-oriented-design | Object-oriented design principles and review |
| Type Design | @122-java-type-design | Best practices for type design in Java |
| General Java Guidelines | @123-java-general-guidelines | General Java coding best practices |
| Secure Java Coding | @124-java-secure-coding | Secure coding practices for Java |
| Concurrency | @125-java-concurrency | Best practices for concurrency in Java |
| Logging Guidelines | @126-java-logging | Logging best practices for Java applications |
| Unit Testing | @131-java-unit-testing | Guidelines for writing unit tests in Java |
| Modern Java Features | @141-java-refactoring-with-modern-features | Refactoring with modern Java (Java 8+) features |
| Functional Programming | @142-java-functional-programming | Applying functional programming in Java |
| Data Oriented Programming | @143-java-data-oriented-programming | Data-oriented programming style in Java |
| Performance testing | @151-java-performance-jmeter | Run a Jmeter script |
| Java Profiling | @161-java-profiling-detect | Generate profiling data |
| Java Profiling | @162-java-profiling-analyze | Analyze profiling data |
| Java Profiling | @164-java-profiling-compare | Compare results after refactoring |

## Tips & Best Practices

- Use the checklists above to track your progress through each phase.
- Use the provided prompts directly in Cursor or your LLM-enabled IDE for best results.
- Review each rule's documentation for detailed examples and anti-patterns.
- Regularly update your dependencies and plugins for security and performance.
- Apply secure coding and logging practices throughout your codebase.
- Use modern Java features and refactor legacy code incrementally.

## Progress Tracking

- [ ] Step 1: Build System (Maven)
- [ ] Step 2: Design Principles
- [ ] Step 3: Coding Guidelines
- [ ] Step 4: Testing
- [ ] Step 5: Refactoring
- [ ] Step 6: Database

---

**Note:** This guide is self-contained and portable. Copy it into any Java project to get started with Cursor Rules for Java development. 