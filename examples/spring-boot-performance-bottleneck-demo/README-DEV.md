# Essential Maven Goals:

```bash
# Analyze dependencies
./mvnw dependency:tree
./mvnw dependency:analyze
./mvnw dependency:resolve

./mvnw clean validate -U
./mvnw buildplan:list-phase
./mvnw license:third-party-report

# Clean the project
./mvnw clean

# Clean and package in one command
./mvnw clean package

# Run integration tests
./mvnw verify

# Check for dependency updates
./mvnw versions:display-property-updates
./mvnw versions:display-dependency-updates
./mvnw versions:display-plugin-updates

# Generate project reports
./mvnw site
jwebserver -p 8005 -d "$(pwd)/target/site/"

./mvnw clean spring-boot:run
http://localhost:8080/swagger-ui/index.html

# WIP
./mvnw clean verify -P jmeter-from-oas

./load-test.sh --help
./load-test.sh -e both -n 500000 -c 10



jwebserver -p 8005 -d "$(pwd)/examples/spring-boot-memory-leak-demo/profiler/results"

My Java application has performance issues - help me set up comprehensive profiling process using @151-java-profiling-detect.mdc and use the location examples/spring-boot-memory-leak-demo/profiler

Analyze the results located in examples/spring-boot-memory-leak-demo/profiler and use the cursor rule @152-java-profiling-analyze

Review if the problems was solved with last refactoring using the reports located in @/results with the cursor rule 154-java-profiling-compare.mdc
``` 