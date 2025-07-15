# Java Maven Build Enhancement Plugins Template

This template provides Maven build enhancement plugin configurations that are conditionally added based on user selections.

**Usage**: Referenced from Step 7 of the modular Maven plugins rule.

**Purpose**: Configure optional build enhancement plugins only when specific features are selected.

**Dependencies**: Only execute if user selected build enhancement features in Step 2.

**CRITICAL PRESERVATION RULE**: Only ADD plugins that don't already exist. Never REPLACE or REMOVE existing plugins.

## Pre-Implementation Plugin Check

**BEFORE adding any plugin, check if it already exists in the `<build><plugins>` section:**

1. **Scan existing plugins** in the pom.xml
2. **Compare with planned additions** from the templates below
3. **Ask user for conflicts**: "Plugin X already exists. Skip adding duplicate? (y/n)"
4. **Skip conflicting plugins** unless user explicitly requests configuration enhancement
5. **Only add NEW plugins** that don't already exist

## Conditional Build Enhancement Plugins (Only Add If Missing)

### 1. Spotless Maven Plugin

**When to add**: Only if user selected "Format source code (Spotless)" in Step 2.

**Purpose**: Automatically format and enforce code style consistency.

Update the pom.xml with this plugin in the `<build><plugins>` section:

```xml
<plugin>
    <groupId>com.diffplug.spotless</groupId>
    <artifactId>spotless-maven-plugin</artifactId>
    <version>${maven-plugin-spotless.version}</version>
    <configuration>
        <encoding>UTF-8</encoding>
        <java>
            <removeUnusedImports />
            <importOrder>
                <order>,\#</order>
            </importOrder>
            <endWithNewline />
            <trimTrailingWhitespace />
            <indent>
                <spaces>true</spaces>
                <spacesPerTab>4</spacesPerTab>
            </indent>
        </java>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>check</goal>
            </goals>
            <phase>process-sources</phase>
        </execution>
    </executions>
</plugin>
```

### 2. Versions Maven Plugin

**When to add**: Only if user selected "Version management" in Step 2.

**Purpose**: Help manage and update dependency and plugin versions systematically.

Update the pom.xml with this plugin in the `<build><plugins>` section:

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>versions-maven-plugin</artifactId>
    <version>${maven-plugin-versions.version}</version>
    <configuration>
        <allowSnapshots>false</allowSnapshots>
    </configuration>
</plugin>
```

### 3. Git Commit ID Plugin

**When to add**: Only if user wants to include Git commit information in the build.

**Purpose**: Include Git commit information in the build artifacts for traceability.

Update the pom.xml with this plugin in the `<build><plugins>` section:

```xml
<plugin>
    <groupId>pl.project13.maven</groupId>
    <artifactId>git-commit-id-plugin</artifactId>
    <version>${maven-plugin-git-commit-id.version}</version>
    <executions>
        <execution>
            <id>get-the-git-infos</id>
            <goals>
                <goal>revision</goal>
            </goals>
            <phase>initialize</phase>
        </execution>
    </executions>
    <configuration>
        <generateGitPropertiesFile>true</generateGitPropertiesFile>
        <generateGitPropertiesFilename>${project.build.outputDirectory}/git.properties</generateGitPropertiesFilename>
        <commitIdGenerationMode>full</commitIdGenerationMode>
    </configuration>
</plugin>
```

### 4. Flatten Maven Plugin

**When to add**: Only if user selected "Java Library" as project nature in Step 2.

**Purpose**: Flatten POM files for library publishing to Maven repositories.

Update the pom.xml with this plugin in the `<build><plugins>` section:

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>flatten-maven-plugin</artifactId>
    <version>${maven-plugin-flatten.version}</version>
    <configuration>
    </configuration>
    <executions>
        <execution>
            <id>flatten</id>
            <phase>process-resources</phase>
            <goals>
                <goal>flatten</goal>
            </goals>
        </execution>
        <execution>
            <id>flatten.clean</id>
            <phase>clean</phase>
            <goals>
                <goal>clean</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Implementation Guidelines

### Plugin Selection Logic

1. **Spotless**: Add only if code formatting was selected
2. **Versions**: Add only if version management was selected
3. **Git Commit ID**: Add only if build information tracking was selected
4. **Flatten**: Add only if project nature is "Java Library"

### Property Dependencies

1. **Verify version properties**: Ensure all plugin version properties are defined in Step 3
2. **Plugin versions**: Use consistent property naming convention `${maven-plugin-*.version}`

### Build Integration

1. **Spotless**: Runs during `process-sources` phase, fails build on formatting violations
2. **Versions**: Available as goals for dependency management tasks
3. **Git Commit ID**: Runs during `initialize` phase, creates git.properties file
4. **Flatten**: Runs during `process-resources` and `clean` phases

## Usage Examples

### Code Formatting with Spotless

```bash
# Check code formatting
./mvnw spotless:check

# Apply code formatting
./mvnw spotless:apply

# Integration with build
./mvnw clean compile  # Will fail if formatting violations exist
```

### Version Management with Versions Plugin

```bash
# Check for dependency updates
./mvnw versions:display-dependency-updates

# Check for plugin updates
./mvnw versions:display-plugin-updates

# Check for property updates
./mvnw versions:display-property-updates

# Update to next snapshot versions
./mvnw versions:set -DnextSnapshot=true

# Update specific dependency
./mvnw versions:use-latest-versions -Dincludes=org.junit.jupiter:*
```

### Git Commit Information

```bash
# Build with git information
./mvnw clean package

# Git properties will be available at runtime
cat target/classes/git.properties

# Access in Java code
Properties gitProps = new Properties();
gitProps.load(getClass().getResourceAsStream("/git.properties"));
String commitId = gitProps.getProperty("git.commit.id");
String buildTime = gitProps.getProperty("git.build.time");
```

### Library Publishing with Flatten Plugin

```bash
# Build library with flattened POM
./mvnw clean package

# The flattened POM will be in target/
cat target/.flattened-pom.xml

# Deploy to repository
./mvnw clean deploy

# Clean flattened files
./mvnw flatten:clean
```

## Configuration Examples

### Custom Spotless Configuration

For more advanced formatting rules, enhance the Spotless configuration:

```xml
<plugin>
    <groupId>com.diffplug.spotless</groupId>
    <artifactId>spotless-maven-plugin</artifactId>
    <version>${maven-plugin-spotless.version}</version>
    <configuration>
        <java>
            <googleJavaFormat>
                <version>1.15.0</version>
                <style>GOOGLE</style>
            </googleJavaFormat>
            <removeUnusedImports />
            <formatAnnotations />
        </java>
        <pom>
            <sortPom>
                <expandEmptyElements>false</expandEmptyElements>
            </sortPom>
        </pom>
    </configuration>
</plugin>
```

### Git Commit ID with Custom Properties

For additional git information in the build:

```xml
<plugin>
    <groupId>pl.project13.maven</groupId>
    <artifactId>git-commit-id-plugin</artifactId>
    <version>${maven-plugin-git-commit-id.version}</version>
    <configuration>
        <generateGitPropertiesFile>true</generateGitPropertiesFile>
        <includeOnlyProperties>
            <includeOnlyProperty>git.commit.id</includeOnlyProperty>
            <includeOnlyProperty>git.commit.time</includeOnlyProperty>
            <includeOnlyProperty>git.branch</includeOnlyProperty>
            <includeOnlyProperty>git.build.time</includeOnlyProperty>
        </includeOnlyProperties>
        <commitIdGenerationMode>full</commitIdGenerationMode>
        <dateFormat>yyyy-MM-dd'T'HH:mm:ssZ</dateFormat>
    </configuration>
</plugin>
```

## Integration with Other Steps

### With Quality Analysis (Step 6)

Spotless can be integrated with quality checks:

```bash
# Run formatting and quality checks
./mvnw clean verify -Pjacoco spotless:check

# Fix formatting, then run quality checks
./mvnw spotless:apply clean verify -Pjacoco
```

### With CI/CD Pipelines

These plugins enhance CI/CD integration:

```yaml
# GitHub Actions example
- name: Check code formatting
  run: ./mvnw spotless:check

- name: Build with git info
  run: ./mvnw clean package

- name: Check for dependency updates
  run: ./mvnw versions:display-dependency-updates
```

## Validation

After adding build enhancement plugins, verify the configuration:

```bash
# Validate plugin configuration
./mvnw validate

# Test Spotless if configured
./mvnw spotless:check

# Test versions plugin if configured
./mvnw versions:display-plugin-updates

# Test build with all plugins
./mvnw clean package
```

## Expected Outcomes

### With Spotless
- Consistent code formatting across the project
- Build fails on formatting violations
- Automatic import organization and cleanup

### With Versions Plugin
- Easy dependency update management
- Visibility into outdated dependencies
- Systematic version upgrade workflows

### With Git Commit ID
- Build artifacts contain commit information
- Runtime access to build metadata
- Enhanced traceability and debugging

### With Flatten Plugin
- Clean POM files for library consumers
- Proper dependency resolution in published artifacts
- Compatible with Maven Central publishing requirements

## Troubleshooting

### Common Issues

1. **Spotless Formatting Violations**: Run `./mvnw spotless:apply` to fix
2. **Git Plugin Fails**: Ensure project is in a git repository
3. **Versions Plugin Slow**: Large projects may take time to analyze dependencies
4. **Flatten Plugin Conflicts**: Ensure proper phase ordering with other plugins
