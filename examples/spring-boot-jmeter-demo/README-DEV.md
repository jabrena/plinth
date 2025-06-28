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

curl -X GET http://localhost:8080/api/v1/hello

./run-jmeter.sh --help
./run-jmeter.sh -l 5000 -t 10 -r 5
./run-jmeter.sh -gui    # Open JMeter GUI

jwebserver -p 8007 -d "$(pwd)/target/jmeter-report/"
``` 