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

## Development

### Adding New Functions

1. Add a new method to the `App.java` class
2. Annotate it with `@FunctionName("YourFunctionName")`
3. Add appropriate trigger annotations (e.g., `@HttpTrigger`)
4. Write corresponding unit tests

### Local Settings

The `local.settings.json` file contains settings for local development:
- `AzureWebJobsStorage`: Set to use development storage
- `FUNCTIONS_WORKER_RUNTIME`: Set to `java`
- `FUNCTIONS_EXTENSION_VERSION`: Set to `~4`


## Resources

- [Azure Functions Java Developer Guide](https://docs.microsoft.com/en-us/azure/azure-functions/functions-reference-java)
- [Azure Functions Maven Plugin](https://github.com/microsoft/azure-maven-plugins/wiki/Azure-Functions)
- [Azure Functions Core Tools](https://docs.microsoft.com/en-us/azure/azure-functions/functions-run-local)