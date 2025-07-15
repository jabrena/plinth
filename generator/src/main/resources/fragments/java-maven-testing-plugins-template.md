# Java Maven Testing Plugins Template

This template provides Maven testing plugin configurations that are conditionally added based on user selections.

**Usage**: Referenced from Step 5 of the modular Maven plugins rule.

**Purpose**: Configure testing-related plugins only when specific testing features are selected.

**Dependencies**: Only execute if user selected testing features in Step 2.

## Conditional Testing Plugins

### 1. maven-failsafe-plugin

**When to add**: Only if user selected "Integration testing (Failsafe)" in Step 2.

**Purpose**: Configure integration testing with proper file patterns and execution phases.

Update the pom.xml with this plugin in the `<build><plugins>` section:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>${maven-plugin-failsafe.version}</version>
    <configuration>
        <includes>
            <include>**/*IT.java</include>
        </includes>
        <excludes>
            <exclude>**/*Test.java</exclude>
        </excludes>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 2. Test Reports (HTML)

**When to add**: Only if user wants HTML test reports generated.

**Purpose**: Generate HTML test reports and add source code links for better test result analysis.

Update the pom.xml with this `<reporting>` section (add at the same level as `<build>`):

```xml
<reporting>
    <plugins>
        <!-- Generates HTML test reports -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-report-plugin</artifactId>
            <version>${maven-plugin-surefire.version}</version>
            <configuration>
                <outputName>junit-report</outputName>
                <showSuccess>true</showSuccess>
            </configuration>
        </plugin>

        <!-- Adds links to source code in reports -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jxr-plugin</artifactId>
            <version>${maven-plugin-jxr.version}</version>
        </plugin>
    </plugins>
</reporting>
```

## Implementation Guidelines

### For Integration Testing

1. **Add failsafe plugin** only if integration testing was explicitly selected
2. **Verify file patterns**: Ensure `*IT.java` files are included and `*Test.java` files are excluded
3. **Test execution**: Integration tests run during `verify` phase
4. **Example integration test**: Create a sample `*IT.java` file to verify configuration

### For HTML Test Reports

1. **Add reporting section** only if HTML reports were requested
2. **Verify plugin versions**: Ensure version properties are defined in Step 3
3. **Generate reports**: Reports are generated during `site` phase
4. **View reports**: HTML reports will be available in `target/site/` directory

## Usage Examples

### Running Integration Tests

```bash
# Run only unit tests
./mvnw test

# Run both unit and integration tests
./mvnw verify

# Run only integration tests
./mvnw failsafe:integration-test
```

### Generating HTML Test Reports

```bash
# Generate test reports
./mvnw site

# View reports in browser
open target/site/junit-report.html

# Or serve reports locally
./mvnw site:run
```

## File Structure Examples

### Integration Test Example

Create integration tests following this pattern:

```
src/test/java/
├── com/example/myapp/
│   ├── ServiceTest.java          # Unit test (runs with surefire)
│   └── ServiceIT.java            # Integration test (runs with failsafe)
```

### Example Integration Test Class

```java
package com.example.myapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceIT {

    @Test
    void integrationTestExample() {
        // Integration test logic here
    }
}
```

## Validation

After adding testing plugins, verify the configuration:

```bash
# Validate plugin configuration
./mvnw validate

# Run tests to verify configuration
./mvnw clean verify

# Generate reports if configured
./mvnw clean verify site
```

## Expected Outcomes

### With Integration Testing
- Unit tests (`*Test.java`) run during `test` phase
- Integration tests (`*IT.java`) run during `integration-test` phase
- Both types can be executed together with `./mvnw verify`

### With HTML Reports
- Test reports generated in `target/site/junit-report.html`
- Source code cross-references available
- Test success/failure details with stack traces
- Reports accessible via Maven site
