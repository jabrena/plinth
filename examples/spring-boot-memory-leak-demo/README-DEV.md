# Spring Boot Memory Leak Demo

## Controller Configuration

This demo includes two controllers that can be conditionally enabled based on the `coco` property:

- **CocoController**: Contains intentional memory leaks for demonstration purposes
- **WithoutCocoController**: Proper implementation with resource management

### Configuration Options

Set the `coco` property in your application properties:

```properties
# Enable CocoController (with memory leaks for demo purposes)
coco=true

# Enable WithoutCocoController (proper resource management)
coco=false
```

### Profile-Specific Configuration

- **Default profile** (`application.properties`): `coco=true` (enables CocoController)
- **Virtual Threads profile** (`application-vt.properties`): `coco=false` (enables WithoutCocoController)

### Usage Examples

```bash
# Run with CocoController (memory leaks)
./mvnw spring-boot:run

# Run with WithoutCocoController (proper resource management)
./mvnw spring-boot:run -Dspring.profiles.active=vt

# Override property at runtime
./mvnw spring-boot:run -Dcoco=false
```

## Essential Maven Goals:

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

My Java application has performance issues - help me set up comprehensive profiling process using @151-java-profiling-detect.md and use the location examples/spring-boot-memory-leak-demo/profiler

Analyze the results located in examples/spring-boot-memory-leak-demo/profiler and use the cursor rule @152-java-profiling-analyze

Review if the problems was solved with last refactoring using the reports located in @/results with the cursor rule 154-java-profiling-compare.md
```
