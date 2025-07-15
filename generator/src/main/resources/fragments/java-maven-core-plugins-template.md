# Java Maven Core Plugins Template

This template provides Maven core plugin configurations that are always needed, regardless of project type or selections.

**Usage**: Referenced from Step 4 of the modular Maven plugins rule.

**Purpose**: Configure essential plugins that every Maven project should have for build consistency and reliability.

**CRITICAL PRESERVATION RULE**: Only ADD plugins that don't already exist. Never REPLACE or REMOVE existing plugins.

## Pre-Implementation Checks

**BEFORE adding any plugin, check if it already exists in the pom.xml:**

1. **maven-enforcer-plugin**: If exists, ask user if they want to enhance existing configuration
2. **maven-compiler-plugin**: If exists, ask user if they want to add missing configuration elements
3. **maven-surefire-plugin**: If exists, ask user if they want to enhance existing configuration

**If plugin already exists**: Skip adding it unless user explicitly requests configuration enhancement.

## Core Plugins (Only Add If Missing)

### 1. maven-enforcer-plugin

**Purpose**: Enforce dependency convergence, prevent circular dependencies, and ensure consistent Maven/Java versions.

**ADD this plugin to the `<build><plugins>` section ONLY if it doesn't already exist:**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-enforcer-plugin</artifactId>
    <version>${maven-plugin-enforcer.version}</version>
    <dependencies>
        <dependency>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>extra-enforcer-rules</artifactId>
            <version>${extra-enforcer-rules.version}</version>
        </dependency>
    </dependencies>
    <executions>
        <execution>
            <id>enforce</id>
            <configuration>
                <rules>
                    <banCircularDependencies/>
                    <dependencyConvergence />
                    <banDuplicatePomDependencyVersions />
                    <requireMavenVersion>
                        <version>${maven.version}</version>
                    </requireMavenVersion>
                    <requireJavaVersion>
                        <version>${java.version}</version>
                    </requireJavaVersion>
                    <bannedDependencies>
                        <excludes>
                            <exclude>org.projectlombok:lombok</exclude>
                        </excludes>
                    </bannedDependencies>
                </rules>
                <fail>true</fail>
            </configuration>
            <goals>
                <goal>enforce</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 2. maven-compiler-plugin (Basic Configuration)

**Purpose**: Configure Java compilation with proper version settings and basic compiler warnings.

**ADD this plugin to the `<build><plugins>` section ONLY if it doesn't already exist:**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>${maven-plugin-compiler.version}</version>
    <configuration>
        <release>${java.version}</release>
        <compilerArgs>
            <arg>-Xlint:all</arg>
            <arg>-Werror</arg>
        </compilerArgs>
    </configuration>
</plugin>
```

### 3. maven-surefire-plugin

**Purpose**: Configure unit testing with proper includes/excludes and failure handling.

**ADD this plugin to the `<build><plugins>` section ONLY if it doesn't already exist:**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>${maven-plugin-surefire.version}</version>
    <configuration>
        <skipAfterFailureCount>1</skipAfterFailureCount>
        <includes>
            <include>**/*Test.java</include>
        </includes>
        <excludes>
            <exclude>**/*IT.java</exclude>
        </excludes>
    </configuration>
</plugin>
```

## Implementation Guidelines

1. **Add plugins in order**: enforcer → compiler → surefire
2. **Verify properties**: Ensure all property references (e.g., `${maven-plugin-enforcer.version}`) are defined in Step 3
3. **Update package names**: Replace `REPLACE_WITH_ACTUAL_PACKAGE` with the project's actual base package
4. **Test configuration**: Run `./mvnw clean compile` to verify compiler configuration
5. **Enhanced analysis**: Only use enhanced compiler configuration if specifically selected in Step 2

## Validation

After adding these plugins, verify the configuration:

```bash
# Validate plugin configuration
./mvnw validate

# Test compilation
./mvnw clean compile

# Run unit tests
./mvnw test
```
