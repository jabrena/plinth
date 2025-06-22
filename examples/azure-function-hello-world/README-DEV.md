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

./mvnw clean package

# Test in local
brew tap azure/functions
brew install azure-functions-core-tools@4

./mvnw clean package
cd target/azure-function/azure-function-hello-world-2025-06-22T10:02:45Z
func start --port 7071 --enableLogging --logLevel information
curl -s http://localhost:7071/api/HelloWorld
curl "http://localhost:7071/api/HttpExample?name=Developer"
``` 