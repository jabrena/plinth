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
``` 