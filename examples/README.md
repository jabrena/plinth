# Examples

## Scenarios availables to test the Cursor rules for Java

| Example            | Prompt |
|--------------------|--------|
| Maven demo         | Help me to review the pom.xml following the best practices for dependency management and directory structure use the cursor rule @110-java-maven-best-practices |
| Maven demo         | Can you improve the pom.xml using the cursor rule @111-java-maven-deps-and-plugins |
| Maven demo         | Generate developer documentation with essential Maven commands using @112-java-maven-documentation |
| Spring boot demo   | Review my code for object-oriented design using the cursor rule @121-java-object-oriented-design |
| Spring boot demo   | Review my code for general Java best practices using the cursor rule @123-java-general-guidelines |
| Spring boot demo   | Check my code for security issues using the cursor rule @124-java-secure-coding |
| Spring boot demo   | Review my code for concurrency best practices using the cursor rule @125-java-concurrency |
| Spring boot demo   | Help me improve logging using the cursor rule @126-java-logging |
| Spring boot demo   | Can improve the unit tests using the cursor rule @131-java-unit-testing |
| Spring boot demo   | Refactor my code to use modern Java features using the cursor rule @141-java-refactoring-with-modern-features  |
| Spring boot demo   | Refactor my code to use functional programming using the cursor rule @142-java-functional-programming |
| Spring boot demo   | Refactor my code to use functional programming using the cursor rule @142-java-functional-programming |

## List of examples

| Example  | Notes |
|----------|-------|
| [Maven demo](maven-demo/README.md) | Simple Maven demo generated with `jbang setup@jabrena init --maven`. Used to test the behaviour of Cursor rules for Java. |
| [Spring Boot demo](spring-boot-demo/implementation/README.md) | Simple Maven demo generated with `jbang setup@jabrena init --spring-boot`. Used to test the behaviour of Cursor rules for Java & Spring Boot. |
| [Spring Boot JMeter demo](spring-boot-jmeter-demo/README.md) | Spring Boot application with JMeter load testing capabilities. Includes automated test scripts and HTML report generation for performance testing. |
| [Spring Boot Memory Leak demo](spring-boot-memory-leak-demo/README.md) | Spring Boot application with intentional memory leaks. Used to demonstrate profiling and memory leak analysis techniques with JFR and flamegraphs. |
| [Spring Boot Performance Bottleneck demo](spring-boot-performance-bottleneck-demo/README.md) | Spring Boot application demonstrating common performance anti-patterns with O(n²) and O(n³) algorithms. Includes load testing and profiling tools for performance analysis. |
| [Quarkus demo](quarkus-demo/README.md) | Simple Quarkus REST application demonstrating the Supersonic Subatomic Java Framework. Used to test the behaviour of Cursor rules for Java & Quarkus. |
| [AWS lambda Hello World](aws-lambda-hello-world/README.md) | Simple AWS Lambda. Used to test the behaviour of Cursor rules for Java & AWS Lambda. |
| [Azure function Hello World](azure-function-hello-world/README.md) | Simple Azure function. Used to test the behaviour of Cursor rules for Java & Azure function. |
