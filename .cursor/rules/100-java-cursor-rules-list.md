---
author: Juan Antonio Breña Moral
version: 0.11.0
---
# Create a Checklist with all Java steps to use with cursor rules for Java

## Role

You are a Senior software engineer with extensive experience in Java software development

## Goal

Your task is to create a comprehensive step-by-step guide that follows the exact format
and structure defined in the embedded template below. Create a markdown file named
`CURSOR-RULES-JAVA.md` with the following content:

```markdown
# Cursor rules Java

Use the following collection of System prompts of Java to improve your Java development.

## Generate this list of System prompts for Java

| Cursor Rule | Description | User Prompt | Notes |
|----|----|-----|-----|
| [100-java-cursor-rules-list](.cursor/rules/100-java-cursor-rules-list.md) | Generate list of System Prompts for Java | **User Prompt:** `Create a document with all System prompts for Java using @100-java-cursor-rules-list` | |

## Build system rules (Maven)

| Cursor Rule | Description | Prompt | Notes |
|----|----|-----|----|
| [110-java-maven-best-practices](.cursor/rules/110-java-maven-best-practices.md) | Analyze your `pom.xml` and apply Maven best practices | **User Prompt:** `Apply in the pom.xml the rule @110-java-maven-best-practices` **Note:** Add in the context the `pom.xml` which you want to generate the documentation. **User Prompt with Consultative Interactive Behaviour:** `Apply in the pom.xml the rule @110-java-maven-best-practices with the behaviour @behaviour-consultative-interaction`| You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. |
| [111-java-maven-dependencies](.cursor/rules/111-java-maven-dependencies.md) | Add Maven dependencies for improved code quality | **Interactive User Prompt:** `Add essential Maven dependencies for code quality using @111-java-maven-dependencies` **User Prompt:** `Add VAVR dependency with the help of@111-java-maven-dependencies and not make any question` (Example)**Note:** Add in the context the `pom.xml` which you want to enhance with quality dependencies. | It is possible to apply the System prompt in an interactive and non interactive way. Using the interactive approach, the Software engineer will interact with the cursor rule to selectively add JSpecify, Error Prone, NullAway and VAVR dependencies based on project needs. |
| [112-java-maven-plugins](.cursor/rules/112-java-maven-plugins.md) | Update your `pom.xml` with Maven Dependencies & Plugins | **Interactive User Prompt:** `Improve the pom.xml using the cursor rule @112-java-maven-plugins` **User Prompt:** `Add Maven Enforcer plugin only from the rule @112-java-maven-plugins without any question` (Example) **Note:** Add in the context the `pom.xml` which you want to generate the documentation. |  It is possible to apply the System prompt in an interactive and non interactive way. Using the interactive approach, the Software engineer will interact with the cursor rule to update the `pom.xml`. |
| [113-java-maven-documentation](.cursor/rules/113-java-maven-documentation.md) | Create a Maven Documentation with the file `README-DEV.md` | **User Prompt:** `Generate developer documentation with essential Maven commands using @113-java-maven-documentation` **Note:** Add in the context the `pom.xml` which you want to generate the documentation. | This cursor rule is applied automatically without any interaction with the Software engineer. |

## Design rules

| Cursor Rule | Description | Prompt | Notes |
|----|----|-----|----|
| [121-java-object-oriented-design](.cursor/rules/121-java-object-oriented-design.md) | Take another point of view with an Object Oriented Design of your development | **User prompt:** `Improve the class/classes added in the context applying the system prompt @121-java-object-oriented-design`(Example) **Note:** Add in the context a class/classes to improve the design. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @121-java-object-oriented-design with the behaviour @behaviour-consultative-interaction` | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. |
| [122-java-type-design](.cursor/rules/122-java-type-design.md) | Review the Type Design in your development | **User prompt:** `Improve the class/classes added in the context applying the system prompt @122-java-type-design` (Example) **Note:** Add in the context a class/classes to improve the design. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @122-java-type-design with the behaviour @behaviour-consultative-interaction` | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour.  |

## Coding rules

| Cursor Rule | Description | Prompt | Notes |
|----|----|-----|----|
| [123-java-general-guidelines](.cursor/rules/123-java-general-guidelines.md) | Apply general purpose Java guidelines | **User Prompt:** `Improve the class/classes added in the context applying the system prompt @123-java-general-guidelines` (Example) **Note:** Add in the context a class/classes. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @123-java-general-guidelines with the behaviour @behaviour-consultative-interaction` | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. |
| [124-java-secure-coding](.cursor/rules/124-java-secure-coding.md) | Review my code for Secure Java Coding rules | **User Prompt:** `Improve the class/classes added in the context applying the system prompt @124-java-secure-coding` (Example) **Note:** Add in the context a class/classes. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @124-java-secure-coding with the behaviour @behaviour-consultative-interaction` | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. |
| [125-java-concurrency](.cursor/rules/125-java-concurrency.md) | Improve your code with Concurrency rules | **User Prompt:** `Improve the class/classes added in the context applying the system prompt @125-java-concurrency` (Example) **Note:** Add in the context a class/classes. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @125-java-concurrency with the behaviour @behaviour-consultative-interaction` | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. |
| [126-java-logging](.cursor/rules/126-java-logging.md) | Apply logging guidelines in your development | **User Prompt:** `Improve the class/classes added in the context applying the system prompt @126-java-logging` (Example) **Note:** Add in the context a class/classes. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @126-java-logging with the behaviour @behaviour-consultative-interaction` | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. |
| [127-java-exception-handling](.cursor/rules/127-java-exception-handling.md) | Add Exception handling | **User Prompt:** `Improve the class/classes added in the context applying the system prompt @127-java-exception-handling` (Example) **Note:** Add in the context a class/classes. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @127-java-exception-handling with the behaviour @behaviour-consultative-interaction` | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. |
| [128-java-generics](.cursor/rules/128-java-generics.md) | Apply generics in a class | **User Prompt:** `Improve the class/classes added in the context applying the system prompt @128-java-generics` (Example) **Note:** Add in the context a class/classes. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @128-java-generics with the behaviour @behaviour-consultative-interaction` **User Prompt with Training behaviour:** `Create a course about @128-java-generics.md using the behavior @behaviour-progressive-learning.md and put the course here` **Note:** Add in the context the location to add the course. | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. |

## Unit Testing rules

| Cursor Rule | Description | Prompt | Notes |
|----|----|-----|----|
| [131-java-unit-testing](.cursor/rules/131-java-unit-testing.md) | Apply Unit Testing best practices | **User Prompt:** `Improve the class/classes added in the context applying the system prompt @131-java-unit-testing` (Example) **Note:** Add in the context a class/classes. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @131-java-unit-testing with the behaviour @behaviour-consultative-interaction` | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. |

## Refactoring rules

| Cursor Rule | Description | Prompt | Notes |
|----|----|-----|----|
| [141-java-refactoring-with-modern-features](.cursor/rules/141-java-refactoring-with-modern-features.md) | Add Modern Java Features in your development | **User Prompt:** `Improve the class/classes added in the context applying the system prompt @141-java-refactoring-with-modern-features` (Example) **Note:** Add in the context a class/classes. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @141-java-refactoring-with-modern-features with the behaviour @behaviour-consultative-interaction` | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. |
| [142-java-functional-programming](.cursor/rules/142-java-functional-programming.md) | Add Functional Programming style in your development |  **User Prompt:** `Improve the class/classes added in the context applying the system prompt @142-java-functional-programming` (Example) **Note:** Add in the context a class/classes. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @142-java-functional-programming with the behaviour @behaviour-consultative-interaction` | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. |
| [143-java-functional-exception-handling](.cursor/rules/143-java-functional-exception-handling.md) | Apply functional programming approaches for error handling using Optional and VAVR Either types | **User Prompt:** `Improve the class/classes added in the context applying the system prompt @143-java-functional-exception-handling` (Example) **Note:** Add in the context a class/classes. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @143-java-functional-exception-handling with the behaviour @behaviour-consultative-interaction` | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. It promotes using Monads like Optional<T> and Either<L,R> instead of exceptions for predictable failures. |
| [144-java-data-oriented-programming](.cursor/rules/144-java-data-oriented-programming.md) | Add Data Oriented Programming in your development |  **User Prompt:** `Improve the class/classes added in the context applying the system prompt @144-java-data-oriented-programming` (Example) **Note:** Add in the context a class/classes. **User Prompt with Consultative Interactive Behaviour:** `Improve the class/classes added in the context applying the system prompt @144-java-data-oriented-programming with the behaviour @behaviour-consultative-interaction` | You can use the System prompt in a purist way or add the Behaviours to customize the final behaviour. |
| - | Improve a Java class method using results from a JMH analysis | **User Prompt:** `Add JMH support using the cursor rule @112-java-maven-plugins and not make any question` (For Maven projects without modules). In Order to design a JMH Benchmark use the following **User Prompt:** `Can you create a JMH benchmark in order to know what is the best implementation?` **Note:** add in the context the Java class that you want to benchmark. Once you execute the Benchmark and you have generated the JSON file, you can analyze the results with the following **User Prompt:** `Can you explain the JMH results and advice about the best implementation?` Add in the context the JMH report in JSON format **Note:** | User prompts |

## Performance rule (Jmeter)

| Activity | Description | Prompt | Notes |
|----|---|-----|----|
| [151-java-performance-jmeter](.cursor/rules/151-java-performance-jmeter.md) | Run a performance test with Jmeter | **User Prompt:** `Add JMeter performance testing to this project using @151-java-performance-jmeter` **Note:** You could ask the model to create a JMeter based on a RestController/Resource. Example: `Can you create a Jmeter file based on the restcontroller in the path src/test/resources/jmeter/load-test.jmx?` | This cursor rule is applied automatically without any interaction with the Software engineer. If you create a Jmeter file with the model, review the generation, sometimes it is necessary to hammer a bit. |

## Profiling rules (Async profiler, jps, jstack, jcmd & jstat)

| Activity | Description | Prompt | Notes |
|----|----|-----|----|
| [161-java-profiling-detect](.cursor/rules/161-java-profiling-detect.md) | Profile your development in runtime and collect evidences to be analyzed later. | **Prompt:** `My Java application has performance issues - help me set up comprehensive profiling process using @161-java-profiling-detect and use the location YOUR-DEVELOPMENT/profiler` **Note:** Replace YOUR-DEVELOPMENT with your actual development path. Example: examples/spring-boot-memory-leak-demo/profiler | Non conversational cursor rule. The Cursor rule will generate 2 scripts. One script designed to run your development with the right JVM flags for profiling and the second scripts will ask few questions about what problem do you want to solve/analyze over one particular PID. **Step 1:** execute `./run-with-profiler.sh --help` **Step2:** execute `./run-jmeter.sh --help` **Step 3:** execute `./profiler/scripts/java-profile.sh` |
| [162-java-profiling-analyze](.cursor/rules/162-java-profiling-analyze.md) | Analyze results from previous step and generate reports with the analysis results.| **Prompt:** `Analyze the results located in YOUR-DEVELOPMENT/profiler and use the cursor rule @162-java-profiling-analyze` **Note:** Replace YOUR-DEVELOPMENT with your actual development path. Example: examples/spring-boot-memory-leak-demo/profiler | Non conversational cursor rule. |
| - | Code Refactoring from suggestions from analysis | `Can you apply the solutions from @profiling-solutions-yyyymmdd.md in @/info to mitigate bottlenecks` | Make a refactoring with the notes from the analysis |
| [164-java-profiling-compare](.cursor/rules/164-java-profiling-compare.md) | Compare results comparing results before and after applying changes in the code | **Prompt:** `Review if the problems was solved with last refactoring using the reports located in @/results with the cursor rule 154-java-profiling-compare` **Note:**  Put in the context the folder with the results | This cursor rule is applied automatically without any interaction with the Software engineer. |

## Documentation rules

| Activity | Description | Prompt | Notes |
|----|----|-----|----|
| [170-java-documentation](.cursor/rules/170-java-documentation.md) | Generate Java project documentation including README.md, package-info.java files, and Javadoc using a modular step-based approach | **Interactive User Prompt:** `Generate technical documentation about the project with the cursor rule @170-java-documentation` **User Prompt:** `Generate README.md with @170-java-documentation without any question` (Example) **Note:** Add in the context the folder to generate the documentation. The rule will analyze existing documentation and ask for user preferences before generating anything. Ensures project validation with Maven before proceeding. | Focused on documentation generation only. For diagrams, use @171-java-diagrams |
| [171-java-diagrams](.cursor/rules/171-java-diagrams.md) | Generate Java project diagrams including UML sequence, class, state-machine diagrams and C4 model diagrams using a modular step-based approach | **Interactive User Prompt:** `Generate diagrams about the project with the cursor rule @171-java-diagrams` **User Prompt:** `Create UML class diagrams with @171-java-diagrams without any question` (Example) **Note:** Add in the context the folder to generate the diagrams. The rule will analyze the codebase and ask for user preferences before generating anything. Ensures project validation with Maven before proceeding. | Focused on diagram generation only. I recommend to run the JBang tool `jbang puml-to-png@jabrena --watch .` in order to generate diagrams in PNG format on the fly |

---

**Note:** This guide is self-contained and portable. Copy it into any Java project to get started with Cursor Rules for Java development.

```

## Constraints

**MANDATORY REQUIREMENT**: Follow the embedded template EXACTLY - do not add, remove, or modify any steps, sections, or cursor rules that are not explicitly shown in the template. ### What NOT to Include:

- **DO NOT** create additional steps beyond what's shown in the template
- **DO NOT** add cursor rules that are not explicitly listed in the embedded template
- **DO NOT** expand or elaborate on sections beyond what the template shows
- **ONLY** use cursor rules that appear in the embedded template
- **ONLY** use the exact wording and structure from the template
- If a cursor rule exists in the workspace but is not in the template, **DO NOT** include it

## Output Format

- **File Creation**: Generate the complete markdown file named `CURSOR-RULES-JAVA.md` in the project root directory
- **Template Adherence**: Follow the embedded template structure and content exactly - no additions, modifications, or omissions
- **File Handling**: If `CURSOR-RULES-JAVA.md` already exists, overwrite it completely with the new generated content