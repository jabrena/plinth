# Azure Function Hello World

A simple Java-based Azure Function that demonstrates HTTP-triggered functions with "Hello World" functionality.

## Deployment

### Deploy to Azure

1. Configure your Azure subscription:
   ```bash
   az login
   ```

2. Deploy the function:
   ```bash
   mvn clean package azure-functions:deploy
   ```

## Configuration

- **Function App Name**: Configured in `pom.xml` as `azure-function-hello-world-${maven.build.timestamp}`
- **Resource Group**: `java-functions-group`
- **Region**: `westus`
- **Runtime**: Java 17 on Linux

## Resources

- [Azure Functions Java Developer Guide](https://docs.microsoft.com/en-us/azure/azure-functions/functions-reference-java)
- [Azure Functions Maven Plugin](https://github.com/microsoft/azure-maven-plugins/wiki/Azure-Functions)
- [Azure Functions Core Tools](https://docs.microsoft.com/en-us/azure/azure-functions/functions-run-local)