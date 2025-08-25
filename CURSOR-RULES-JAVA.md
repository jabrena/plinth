# Cursor rules Java

Use the following set of Java Cursor Rules to improve your Java development.

## Build system rules (Maven)

| Cursor Rule | Description | Prompt | Notes |
|----|----|-----|----|
| [110-java-maven-best-practices](.cursor/rules/110-java-maven-best-practices.mdc) | Analyze your `pom.xml` and apply Maven best practices | **Prompt:** `Review the pom.xml following the best practices showing several alternatives thanks to the cursor rule @110-java-maven-best-practices` **Note:** Add in the context the `pom.xml` which you want to generate the documentation. | Using an Interactive approach, the rule will add pom.xml best practices. |
| [111-java-maven-dependencies](.cursor/rules/111-java-maven-dependencies.mdc) | Add Maven dependencies for improved code quality | **Prompt:** `Add essential Maven dependencies for code quality using @111-java-maven-dependencies` **Note:** Add in the context the `pom.xml` which you want to enhance with quality dependencies. | Using a Interactive approach, the Software engineer will interact with the cursor rule to selectively add JSpecify, Error Prone, NullAway and VAVR dependencies based on project needs. |
| [112-java-maven-plugins](.cursor/rules/112-java-maven-plugins.mdc) | Update your `pom.xml` with Maven Dependencies & Plugins | **Prompt:** `Can you improve the pom.xml using the cursor rule @112-java-maven-plugins` **Note:** Add in the context the `pom.xml` which you want to generate the documentation. |  Using a Interactive approach, the Software engineer will interact with the cursor rule to update the `pom.xml`. |
| [113-java-maven-documentation](.cursor/rules/113-java-maven-documentation.mdc) | Create a Maven Documentation with the file `README-DEV.md` | **Prompt:** `Generate developer documentation with essential Maven commands using @113-java-maven-documentation` **Note:** Add in the context the `pom.xml` which you want to generate the documentation. | This cursor rule is applied automatically without any interaction with the Software engineer. |

## Design rules

| Cursor Rule | Description | Prompt | Notes |
|----|----|-----|----|
| [121-java-object-oriented-design](.cursor/rules/121-java-object-oriented-design.mdc) | Take another point of view with an Object Oriented Design of your development | **Prompt:** `Review my code for object-oriented design showing several alternatives thanks to the cursor rule @121-java-object-oriented-design` **Note:** Add in the context a package to improve the design. | Using an Interactive approach, the rule will propose multiple alternatives to improve the OOP design. |
| [122-java-type-design](.cursor/rules/122-java-type-design.mdc) | Review the Type Design in your development | **Prompt:** `Review my code for type design showing several alternatives thanks to the cursor rule @122-java-type-design` **Note:** Add in the context a package to improve the design. | Using an Interactive approach, the rule will propose multiple alternatives to improve the Type design.  |

## Coding rules

| Cursor Rule | Description | Prompt | Notes |
|----|----|-----|----|
| [123-java-general-guidelines](.cursor/rules/123-java-general-guidelines.mdc) | Apply general purpose Java guidelines | **Prompt:** `Review my code for general Java best practices showing several alternatives thanks to the cursor rule @123-java-general-guidelines` **Note:** Add a package in the context. | Interactive cursor rule. |
| [124-java-secure-coding](.cursor/rules/124-java-secure-coding.mdc) | Review my code for Secure Java Coding rules | **Prompt:** `Review my code for secure coding showing several alternatives thanks to the cursor rule @124-java-secure-coding` **Note:** Add a package in the context. | Interactive cursor rule. |
| [125-java-concurrency](.cursor/rules/125-java-concurrency.mdc) | Improve your code with Concurrency rules | **Prompt:** `Review my code for concurrency best practices showing several alternatives thanks to the cursor rule @125-java-concurrency` **Note:** Add a class or package which consider that it could bye improved by the cursor rule. | Interactive cursor rule. |
| [126-java-logging](.cursor/rules/126-java-logging.mdc) | Apply logging guidelines in your development | **Prompt:** `Review my code for logging showing several alternatives thanks to the cursor rule @126-java-logging` **Note:** Add a class or package which consider that it could bye improved by the cursor rule. | Interactive cursor rule. |
| [127-java-functional-exception-handling](.cursor/rules/127-java-functional-exception-handling.mdc) | Apply functional programming approaches for error handling using Optional and VAVR Either types | **Prompt:** `Review my code for functional exception handling showing several alternatives thanks to the cursor rule @127-java-functional-exception-handling` **Note:** Add a class or package that uses traditional exception handling for business logic failures. | Interactive cursor rule. It promotes using monads like Optional<T> and Either<L,R> instead of exceptions for predictable failures. |

## Testing rules

| Cursor Rule | Description | Prompt | Notes |
|----|----|-----|----|
| [131-java-unit-testing](.cursor/rules/131-java-unit-testing.mdc) | Apply Unit Testing best practices | **Prompt:** `Review my testing code for unit testing showing several alternatives thanks to the cursor rule @131-java-unit-testing` **Note:** Add a class or package which consider that it could bye improved by the cursor rule. | Interactive cursor rule. |

## Refactoring rules

| Cursor Rule | Description | Prompt | Notes |
|----|----|-----|----|
| [141-java-refactoring-with-modern-features](.cursor/rules/141-java-refactoring-with-modern-features.mdc) | Add Modern Java Features in your development | **Prompt:** `Review my code for using modern Java features showing several alternatives thanks to the cursor rule @141-java-refactoring-with-modern-features` **Note:** Add a class or package which consider that it could bye improved by the cursor rule. | Interactive cursor rule. |
| [142-java-functional-programming](.cursor/rules/142-java-functional-programming.mdc) | Add Functional Programming style in your development |  **Prompt:** `Review my code for using functional programming showing several alternatives thanks to the cursor rule @142-java-functional-programming` **Note:** Add a class or package which consider that it could bye improved by the cursor rule. | Interactive cursor rule. |
| [143-java-data-oriented-programming](.cursor/rules/143-java-data-oriented-programming.mdc) | Add Data Oriented Programmin in your development |  **Prompt:** `Review my code for using data oriented programming showing several alternatives thanks to the cursor rule @143-java-data-oriented-programming` **Note:** Add a class or package which consider that it could bye improved by the cursor rule. | Interactive cursor rule. |
| - | Improve a Java class method using results from a JMH analysis | Add JMH support using the cursor rule `@112-java-maven-plugins` (For Maven projects without modules). In Order to design a JMH Benchmark use the following User prompt: `Can you create a JMH benchmark in order to know what is the best implementation?` and add in the context the Java class that you want to benchmark. Once you execute the Benchmark and you have generated the JSON file, you can analyze the results with: `Can you explain the JMH results and advice about the best implementation?` Add in the context the JMH report in JSON format **Note:** | Interactive rule & User prompts |

## Performance rule (Jmeter)

| Activity | Description | Prompt | Notes |
|----|---|-----|----|
| [151-java-performance-jmeter](.cursor/rules/151-java-performance-jmeter.mdc) | Run a peformance test with Jmeter | **Prompt:** `Add JMeter performance testing to this project using @151-java-performance-jmeter.mdc` **Note:** You could ask the model to create a JMeter based on a RestController/Resource. Example: `Can you create a Jmeter file based on the restcontroller in the path src/test/resources/jmeter/load-test.jmx?` | This cursor rule is applied automatically without any interaction with the Software engineer. If you create a Jmeter file with the model, review the generation, sometimes it is necessary to hammer a bit. |

## Profiling rules (Async profiler, jps, jstack, jcmd & jstat)

| Activity | Description | Prompt | Notes |
|----|----|-----|----|
| [161-java-profiling-detect](.cursor/rules/161-java-profiling-detect.mdc) | Profile your development in runtime and collect evidences to be analyzed later. | **Prompt:** `My Java application has performance issues - help me set up comprehensive profiling process using @161-java-profiling-detect.mdc and use the location YOUR-DEVELOPMENT/profiler` **Note:** Replace YOUR-DEVELOPMENT with your actual development path. Example: examples/spring-boot-memory-leak-demo/profiler | Non conversational cursor rule. The Cursor rule will generate 2 scripts. One script designed to run your development with the right JVM flags for profiling and the second scripts will ask few questions about what problem do you want to solve/analyze over one particular PID. **Step 1:** execute `./run-with-profiler.sh --help` **Step2:** execute `./run-jmeter.sh --help` **Step 3:** execute `./profiler/scripts/java-profile.sh` |
| [162-java-profiling-analyze](.cursor/rules/162-java-profiling-analyze.mdc) | Analyze results from previous step and generate reports with the analysis results.| **Prompt:** `Analyze the results located in YOUR-DEVELOPMENT/profiler and use the cursor rule @162-java-profiling-analyze` **Note:** Replace YOUR-DEVELOPMENT with your actual development path. Example: examples/spring-boot-memory-leak-demo/profiler | Non conversational cursor rule. |
| - | Code Refactoring from suggestions from analysis | `Can you apply the solutions from @profiling-solutions-yyyymmdd.md in @/info to mitigate bottlenecks` | Make a refactoring with the notes from the analysis |
| [164-java-profiling-compare](.cursor/rules/164-java-profiling-compare.mdc) | Compare results comparing results before and after applying changes in the code | **Prompt:** `Review if the problems was solved with last refactoring using the reports located in @/results with the cursor rule 154-java-profiling-compare.mdc` **Note:**  Put in the context the folder with the results | This cursor rule is applied automatically without any interaction with the Software engineer. |

## Documentation rule

| Activity | Description | Prompt | Notes |
|----|----|-----|----|
| [170-java-documentation](.cursor/rules/170-java-documentation.mdc) | Generate Java project documentation & diagrams including README.md and package-info.java files using a modular step-based approach | **Prompt:** `Generate technical documentation & diagrams about the project with the cursor rule @170-java-documentation.mdc`  **Note:** Add in the context the folder to generate the documentation. The rule will analyze existing documentation and ask for user preferences before generating anything. Ensures project validation with Maven before proceeding. | Interactive cursor rule. |

---

**Note:** This guide is self-contained and portable. Copy it into any Java project to get started with Cursor Rules for Java development.
