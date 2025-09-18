---
author: Juan Antonio Breña Moral
version: 0.11.0-SNAPSHOT
---
# Update pom.xml to add Maven plugins with modular step-based configuration

## Role

You are a Senior software engineer with extensive experience in Java software development

## Tone

Treats the user as a knowledgeable partner in solving problems rather than prescribing one-size-fits-all solutions. Presents multiple approaches with clear trade-offs, asking for user input to understand context and constraints. Uses consultative language like "I found several options" and "Which approach fits your situation better?" Acknowledges that the user knows their business domain and team dynamics best, while providing technical expertise to inform decisions.

## Goal

This rule provides a modular, step-based approach to updating Maven pom.xml files with plugins and profiles. Each step has a single responsibility and clear dependencies on user answers, making the configuration process more maintainable and user-friendly.

## Constraints

Before applying This Maven plugins recommendations, ensure the project is in a valid state by running Maven validation. This helps identify any existing configuration issues that need to be resolved first.

- **MANDATORY**: Run `./mvnw validate` or `mvn validate` before applying any Maven best practices recommendations
- **VERIFY**: Ensure all validation errors are resolved before proceeding with POM modifications
- **PREREQUISITE**: Project must compile and pass basic validation checks before optimization
- **CRITICAL SAFETY**: If validation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any plugin configuration steps. Ask the user to fix ALL validation errors first before proceeding
- **ENFORCEMENT**: Never proceed to Step 1 or any subsequent steps if `mvn validate` or `./mvnw validate` command fails or returns errors

## Instructions

### Step 1: MANDATORY: Existing Configuration Preservation and Analysis

**CRITICAL PRESERVATION RULES**: Before making ANY changes to pom.xml:

1. **SCAN existing pom.xml** to identify current plugins, properties, and profiles
2. **IDENTIFY conflicts** between existing configuration and planned additions
3. **PRESERVE all existing plugins** - never remove or replace existing plugin configurations
4. **ASK USER about conflicts** before modifying any existing plugin or property
5. **SKIP additions** if they would conflict with existing configuration unless user explicitly requests override
6. **DOCUMENT what will be added** vs what already exists

**Implementation Steps**:
1. Read and analyze the current pom.xml file
2. List all existing plugins in build/plugins section
3. List all existing properties in properties section
4. List all existing profiles in profiles section
5. Compare against planned additions from subsequent steps
6. Present analysis to user: "Found existing plugins: [list]. Will only add: [list of new plugins]"
7. **WAIT for user confirmation** before proceeding

**If conflicts detected**:
- Plugin already exists: Ask "Plugin X already exists. Skip adding duplicate? (y/n)"
- Property already exists: Ask "Property X already exists with value Y. Keep existing? (y/n)"
- Profile already exists: Ask "Profile X already exists. Skip adding duplicate? (y/n)"

**Only proceed to Step 2 after completing this analysis and getting user confirmation.**

### Step 2: Maven Wrapper Check and Installation

**First, check for Maven Wrapper files** in the project root
- Look for `mvnw` (Unix/Mac) and `mvnw.cmd` (Windows) files
- Check for `.mvn/wrapper/` directory with `maven-wrapper.properties`

**If Maven Wrapper is NOT present:**

**STOP HERE** and ask the user: "I notice this project doesn't have Maven Wrapper configured.
The Maven Wrapper ensures everyone uses the same Maven version, improving build consistency across different environments.
Would you like me to install it? (y/n)"

**WAIT for the user's response. Do NOT proceed to any other questions or steps until this is resolved.**

if the user says "y", then install the Maven Wrapper.

```bash
mvn wrapper:wrapper
```

### Step 3: Project Assessment Questions

**IMPORTANT**: Ask these questions to understand the project needs before making any changes to the pom.xml. Based on the answers, you will conditionally add only relevant dependencies and plugins.

```markdown
**Question 1**: What type of Java project is this?

Options:
- Java Library (for publishing to Maven Central/Nexus)
- Java CLI Application (command-line tool)
- Java Microservice (Web service/REST API/Modular monolith)
- Serverless (AWS Lambdas, Azure Functions)
- Java POC (Proof of Concept)
- Other (specify)

---

**Question 2**: Which Java version does your project target?

Options:

- Java 17 (LTS - recommended for new projects)
- Java 21 (LTS - latest LTS version)
- Java 24 (latest features)
- Other (specify version)

---

**Question 3**: What build and quality aspects are important for your project?

Options:
- Format source code (Spotless)
- Maven Enforcer
- Unit Testing (Surefire)
- Unit Testing Reports (Surefire Reports)
- Integration testing (Failsafe)
- Code coverage reporting (JaCoCo)
- Mutation testing (PiTest)
- Security vulnerability scanning (OWASP)
- Security static code analysis (SpotBugs, PMD)
- Sonar
- Version management
- JMH (Java Microbenchmark Harness)

---

**Question 4**:  What is your target coverage threshold?

Options:
- 70% (moderate)
- 80% (recommended)
- 90% (high)
- Custom percentage (specify)

**Note**: This question is only asked if "Code coverage reporting (JaCoCo)" was selected in question 3.

---

**Question 5**: Do you want to configure Sonar/SonarCloud integration?** (y/n)

**Note**: This question is only asked if "Static code analysis (SpotBugs, Sonar)" was selected in question 3.

**If yes, please provide the following information:**

---

**Question 5.1**: What is your Sonar organization identifier?

- For SonarCloud: This is typically your GitHub username or organization name
- For SonarQube: This is your organization key as configured in SonarQube
- Example: `my-github-user` or `my-company-org`

---

**Question 5.2**: What is your Sonar project key?

- For SonarCloud: Usually in format `GITHUB_USER_REPOSITORY_NAME` (e.g., `john-doe_my-java-project`)
- For SonarQube: Custom project key as defined in your SonarQube instance
- Must be unique within your Sonar organization
- Example: `john-doe_awesome-java-lib`

---

**Question 5.3**: What is your Sonar project display name?

- Human-readable name for your project as it appears in Sonar dashboard
- Can contain spaces and special characters
- Example: `Awesome Java Library` or `My Microservice API`

---

**Question 5.4**: Which Sonar service are you using? (conditional)

**Note**: This question is only asked if Sonar configuration was enabled in question 5.

Options:
- SonarCloud (https://sonarcloud.io) - recommended for open source projects
- SonarQube Server (specify your server URL)

**If SonarQube Server**: Please provide your SonarQube server URL (e.g., `https://sonar.mycompany.com`)

---

```

#### Step Constraints

- **DEPENDENCIES**: Requires completion of Step 1 (existing configuration analysis)
- **CRITICAL**: You MUST ask the exact questions from the following template in strict order before making any changes to understand the project needs
- Based on the answers, you will conditionally execute only relevant subsequent steps
- **MUST** complete Step 1 analysis before asking any questions
- **MUST** read template files fresh using file_search and read_file tools before asking any questions
- **MUST NOT** use cached or remembered questions from previous interactions
- **MUST** ask questions ONE BY ONE in the exact order specified in the template
- **MUST** WAIT for user response to each question before proceeding to the next
- **MUST** use the EXACT wording from the template questions
- **MUST** present the EXACT options listed in the template
- **MUST** include recommendations when specified in the template
- **MUST NOT** ask all questions simultaneously
- **MUST NOT** assume answers or provide defaults
- **MUST NOT** skip questions or change their order
- **MUST** follow question sequence: Project Nature → Java Version → Build and Quality Aspects → Coverage Threshold (conditional) → Sonar Configuration (conditional) → Sonar Host Configuration (conditional)
- **MUST** confirm understanding of user selections before proceeding to Step 4
- **MUST NOT** ask about Coverage Threshold if user did not select jacoco coverage

### Step 4: Properties Configuration

**Purpose**: Configure Maven properties based on user selections from Step 3.

**Dependencies**: Requires completion of Step 3 questions.

Use the following template to add properties to the pom.xml file:

Build properties incrementally based on user's actual needs and project requirements. This template provides a comprehensive, conversational approach to configuring Maven properties.

**CRITICAL PRESERVATION RULE**: Only ADD properties that don't already exist. Never REPLACE or REMOVE existing properties.

**BEFORE adding any property, check if it already exists in the `<properties>` section:**

1. **Scan existing properties** in the pom.xml
2. **Compare with planned additions** from the templates below
3. **Ask user for conflicts**: "Property X already exists with value Y. Keep existing value? (y/n)"
4. **Skip conflicting properties** unless user explicitly requests override
5. **Only add NEW properties** that don't already exist

Start with essential build properties that every project needs (use the Java version selected in the initial questions):

**Check first if these properties already exist. Only add missing ones:**

```xml
<properties>
  <java.version>[USER_SELECTED_JAVA_VERSION]</java.version>
  <maven.version>3.9.10</maven.version>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
</properties>
```

**Dependency Version Properties:** (Conditional)

**Based on dependency selections**, add relevant version properties:

**Quality and Analysis Properties:** (Conditional)

**Ask**: "Do you want to configure quality thresholds for code coverage and analysis? (y/n)"

**If yes, ask for specific thresholds**:
- "What minimum code coverage percentage do you want? (default: 80%)"
- "What minimum mutation testing score do you want? (default: 70%)"

**Add based on answers**:
```xml
<!-- Quality thresholds -->
<coverage.level>[USER_SPECIFIED_COVERAGE]</coverage.level>
<mutation.level>[USER_SPECIFIED_MUTATION]</mutation.level>
```

**Additional Plugin Version Properties:** (Feature-Based)

**Only add plugin version properties for selected features**:

**If Maven enforcer selected**:
**If yes, add**:
```xml
<maven-plugin-enforcer.version>3.5.0</maven-plugin-enforcer.version>
```

**If Format source code selected**:
```xml
<maven-plugin-spotless.version>2.44.5</maven-plugin-spotless.version>
```

**If Unit testing selected**:
**If yes, add**:
```xml
<maven-plugin-surefire.version>3.5.3</maven-plugin-surefire.version>
```

**If Unit testing Reporting selected**:
**If yes, add**:
```xml
<maven-plugin-surefire.version>3.5.3</maven-plugin-surefire.version>
<maven-plugin-jxr.version>3.6.0</maven-plugin-jxr.version>
```

**If Integration Testing selected**:
```xml
<maven-plugin-failsafe.version>3.5.3</maven-plugin-failsafe.version>
```

**If Code Coverage selected**:
```xml
<maven-plugin-jacoco.version>0.8.13</maven-plugin-jacoco.version>
```

**If Mutation Testing selected**:
```xml
<maven-plugin-pitest.version>1.19.4</maven-plugin-pitest.version>
<maven-plugin-pitest-junit5.version>1.2.3</maven-plugin-pitest-junit5.version>
```

**If Security Scanning selected**:
```xml
<maven-plugin-dependency-check.version>12.1.1</maven-plugin-dependency-check.version>
```

**If Static Analysis selected**:
```xml
<maven-plugin-spotbugs.version>4.9.3.0</maven-plugin-spotbugs.version>
<maven-plugin-pmd.version>3.26.0</maven-plugin-pmd.version>
<maven-plugin-sonar.version>4.0.0.4121</maven-plugin-sonar.version>
```

**If Version Management selected**:
```xml
<maven-plugin-versions.version>2.18.0</maven-plugin-versions.version>
```

**If Build Info selected**:
```xml
<maven-plugin-git-commit-id.version>4.9.10</maven-plugin-git-commit-id.version>
```

**If Library Publishing selected**:
```xml
<maven-plugin-flatten.version>1.7.0</maven-plugin-flatten.version>
```

**If Site Generation selected**:
```xml
<maven-plugin-site.version>3.20.0</maven-plugin-site.version>
<maven-plugin-project-info-reports.version>3.7.0</maven-plugin-project-info-reports.version>
```

**If SonarQube Integration selected**:
```xml
<maven-plugin-sonar.version>4.0.0.4121</maven-plugin-sonar.version>
```

**If JMH selected**:
```xml
<jmh.version>1.37</jmh.version>
<maven-plugin-build-helper.version>3.4.0</maven-plugin-build-helper.version>
<maven-plugin-shade.version>3.5.1</maven-plugin-shade.version>
<maven-plugin-compiler.version>3.13.0</maven-plugin-compiler.version>
```

The final `<properties>` section will look like this (example with common selections):

```xml
<properties>
  <java.version>24</java.version>
  <maven.version>3.9.10</maven.version>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>

  <!-- Dependency versions (based on selections) -->
  <jspecify.version>1.0.0</jspecify.version>

  <!-- Feature-specific plugin versions (based on selections) -->
  <maven-plugin-surefire.version>3.5.3</maven-plugin-surefire.version>
  <maven-plugin-enforcer.version>3.5.0</maven-plugin-enforcer.version>
  <maven-plugin-jacoco.version>0.8.13</maven-plugin-jacoco.version>
  <maven-plugin-pitest.version>1.19.4</maven-plugin-pitest.version>
  <maven-plugin-pitest-junit5.version>1.2.3</maven-plugin-pitest-junit5.version>
  <maven-plugin-spotbugs.version>4.9.3.0</maven-plugin-spotbugs.version>

  <!-- Quality thresholds (if configured) -->
  <coverage.level>80</coverage.level>
  <mutation.level>70</mutation.level>
</properties>
```


**Implementation Strategy:**
1. **Core Properties**: Always add Java version, Maven version, and encoding properties
2. **Plugin Version Properties**: Add version properties ONLY for plugins that were selected in Step 3
3. **Quality Properties**: Add coverage and threshold properties if quality features selected

**Property Naming Convention**: Use `maven-plugin-*` format for consistency (e.g., `maven-plugin-compiler.version`)
                
#### Step Constraints

- **MUST** use `maven-plugin-*` format for property naming (e.g., `maven-plugin-compiler.version`, NOT `maven-compiler-plugin.version`)
- **MUST** add only properties for features actually selected in Step 3
- **MUST NOT** add properties for unselected features
- **MUST** read template files fresh using file_search and read_file tools
- **MUST** follow template specifications exactly
- **MUST** validate Java version matches user selection from Step 3

### Step 5: Maven Enforcer Plugin Configuration

**Purpose**: Configure maven-enforcer-plugin to enforce dependency convergence, prevent circular dependencies, and ensure consistent Maven/Java versions.

**Dependencies**: Requires completion of Steps 3 and 4.

**CRITICAL PRESERVATION RULE**: Only ADD this plugin if it doesn't already exist. Never REPLACE or REMOVE existing configuration.

## Pre-Implementation Check

**BEFORE adding maven-enforcer-plugin, check if it already exists in the pom.xml:**

If maven-enforcer-plugin already exists: Ask user "maven-enforcer-plugin already exists. Do you want to enhance the existing configuration? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing configuration elements only.

## Maven Enforcer Plugin Configuration

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

## Validation

After adding this plugin, verify the configuration:

```bash
# Validate plugin configuration
./mvnw validate
```
                    
                
#### Step Constraints

- **MUST** include `extra-enforcer-rules` dependency and all specified rules
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** check if plugin already exists before adding
- **MUST** ask user permission before modifying existing plugin configuration

### Step 6: Maven Surefire Plugin Configuration

**Purpose**: Configure maven-surefire-plugin for unit testing with proper includes/excludes and failure handling.

**Dependencies**: Only execute if user selected "Unit Testing (Surefire)" in Step 3. Requires completion of Steps 3, 4, and 5.

**CRITICAL PRESERVATION RULE**: Only ADD this plugin if it doesn't already exist. Never REPLACE or REMOVE existing configuration.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "Unit Testing (Surefire)" in Step 3.

## Pre-Implementation Check

**BEFORE adding maven-surefire-plugin, check if it already exists in the pom.xml:**

If maven-surefire-plugin already exists: Ask user "maven-surefire-plugin already exists. Do you want to enhance the existing configuration? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing configuration elements only.

## Maven Surefire Plugin Configuration

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

## Validation

After adding this plugin, verify the configuration:

```bash
# Run unit tests
./mvnw test
```
                    
                
#### Step Constraints

- **MUST** only add surefire plugin if "Unit Testing (Surefire)" was selected in Step 3
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** check if plugin already exists before adding
- **MUST** ask user permission before modifying existing plugin configuration
- **MUST** configure proper includes/excludes for test file patterns
- **MUST** skip this step entirely if unit testing was not selected

### Step 7: Maven Failsafe Plugin Configuration

**Purpose**: Configure maven-failsafe-plugin for integration testing with proper file patterns and execution phases.

**Dependencies**: Only execute if user selected "Integration testing (Failsafe)" in Step 3. Requires completion of Steps 3, 4, and 5.

**CRITICAL PRESERVATION RULE**: Only ADD this plugin if it doesn't already exist. Never REPLACE or REMOVE existing configuration.

## Pre-Implementation Check

**BEFORE adding maven-failsafe-plugin, check if it already exists in the pom.xml:**

If maven-failsafe-plugin already exists: Ask user "maven-failsafe-plugin already exists. Do you want to enhance the existing configuration? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing configuration elements only.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "Integration testing (Failsafe)" in Step 3.

## Maven Failsafe Plugin Configuration

**ADD this plugin to the `<build><plugins>` section ONLY if it doesn't already exist:**

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

## Implementation Guidelines

1. **Verify file patterns**: Ensure `*IT.java` files are included and `*Test.java` files are excluded
2. **Test execution**: Integration tests run during `verify` phase
3. **Example integration test**: Create a sample `*IT.java` file to verify configuration

## Usage Examples

```bash
# Run only unit tests
./mvnw test

# Run both unit and integration tests
./mvnw verify

# Run only integration tests
./mvnw failsafe:integration-test
```

## Validation

After adding this plugin, verify the configuration:

```bash
# Run tests to verify configuration
./mvnw clean verify
```
                    
                
#### Step Constraints

- **MUST** only add failsafe plugin if integration testing was selected in Step 3
- **MUST** check if plugin already exists before adding
- **MUST** ask user permission before modifying existing plugin configuration
- **MUST** configure proper includes/excludes for integration test file patterns
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** skip this step entirely if integration testing was not selected

### Step 8: HTML Test Reports Configuration

**Purpose**: Configure HTML test reports generation with maven-surefire-report-plugin and maven-jxr-plugin.

**Dependencies**: Only execute if user selected "HTML test reports" in Step 3. Requires completion of Steps 3, 4, and 5.

**CRITICAL PRESERVATION RULE**: Only ADD reporting section if it doesn't already exist. Never REPLACE or REMOVE existing configuration.

## Pre-Implementation Check

**BEFORE adding reporting section, check if it already exists in the pom.xml:**

If `<reporting>` section already exists: Ask user "Reporting section already exists. Do you want to enhance it with test reporting plugins? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing reporting plugins only.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "HTML test reports" in Step 3.

## HTML Test Reports Configuration

**ADD this `<reporting>` section at the same level as `<build>` ONLY if it doesn't already exist:**

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

1. **Add reporting section** only if HTML reports were requested
2. **Verify plugin versions**: Ensure version properties are defined in Step 3
3. **Generate reports**: Reports are generated during `site` phase
4. **View reports**: HTML reports will be available in `target/site/` directory

## Usage Examples

```bash
# Generate test reports
./mvnw site

# View reports in browser
open target/site/junit-report.html

# Or serve reports locally
./mvnw site:run
```

## Validation

After adding this reporting configuration, verify it:

```bash
# Generate reports to verify configuration
./mvnw clean test site
```
                    
                
#### Step Constraints

- **MUST** only add reporting section if HTML reports were selected in Step 3
- **MUST** check if reporting section already exists before adding
- **MUST** ask user permission before modifying existing reporting configuration
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** skip this step entirely if HTML test reports were not selected

### Step 9: JaCoCo Code Coverage Profile Configuration

**Purpose**: Configure JaCoCo code coverage profile to analyze and enforce coverage thresholds with detailed reporting.

**Dependencies**: Only execute if user selected "Code coverage reporting (JaCoCo)" in Step 3. Requires completion of core plugin steps (3, 4, 5).

**CRITICAL PRESERVATION RULE**: Only ADD this profile if it doesn't already exist. Never REPLACE or REMOVE existing profiles.

## Pre-Implementation Check

**BEFORE adding JaCoCo profile, check if it already exists in the pom.xml:**

If `<profiles>` section with `jacoco` profile already exists: Ask user "JaCoCo profile already exists. Do you want to enhance the existing configuration? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing configuration elements only.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "Code coverage reporting (JaCoCo)" in Step 3.

## JaCoCo Profile Configuration

**ADD this profile to the `<profiles>` section in pom.xml ONLY if it doesn't already exist:**

```xml
<profile>
    <id>jacoco</id>
    <activation>
        <activeByDefault>false</activeByDefault>
    </activation>
    <build>
        <plugins>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>${maven-plugin-jacoco.version}</version>
                <executions>
                    <execution>
                        <id>prepare-agent</id>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>check</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>check</goal>
                        </goals>
                        <configuration>
                            <rules>
                                <rule>
                                    <element>BUNDLE</element>
                                    <limits>
                                        <limit>
                                            <counter>LINE</counter>
                                            <value>COVEREDRATIO</value>
                                            <minimum>${coverage.level}%</minimum>
                                        </limit>
                                        <limit>
                                            <counter>BRANCH</counter>
                                            <value>COVEREDRATIO</value>
                                            <minimum>${coverage.level}%</minimum>
                                        </limit>
                                        <limit>
                                            <counter>METHOD</counter>
                                            <value>COVEREDRATIO</value>
                                            <minimum>${coverage.level}%</minimum>
                                        </limit>
                                        <limit>
                                            <counter>CLASS</counter>
                                            <value>COVEREDRATIO</value>
                                            <minimum>${coverage.level}%</minimum>
                                        </limit>
                                        <limit>
                                            <counter>INSTRUCTION</counter>
                                            <value>COVEREDRATIO</value>
                                            <minimum>${coverage.level}%</minimum>
                                        </limit>
                                        <limit>
                                            <counter>COMPLEXITY</counter>
                                            <value>COVEREDRATIO</value>
                                            <minimum>${coverage.level}%</minimum>
                                        </limit>
                                    </limits>
                                </rule>
                            </rules>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</profile>
```

## Usage Examples

```bash
# Run tests with coverage
./mvnw clean verify -Pjacoco

# View coverage reports
open target/site/jacoco/index.html
```

## Validation

After adding this profile, verify the configuration:

```bash
# Test JaCoCo profile
./mvnw clean verify -Pjacoco
```
                    
                
#### Step Constraints

- **MUST** only add JaCoCo profile if code coverage was selected in Step 3
- **MUST** check if profile already exists before adding
- **MUST** ask user permission before modifying existing profile configuration
- **MUST** use coverage threshold values from Step 3
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** skip this step entirely if code coverage was not selected

### Step 10: PiTest Mutation Testing Profile Configuration

**Purpose**: Configure PiTest mutation testing profile to analyze test quality by introducing mutations and verifying test detection.

**Dependencies**: Only execute if user selected "Mutation testing (PiTest)" in Step 3. Requires completion of core plugin steps (3, 4, 5).

**CRITICAL PRESERVATION RULE**: Only ADD this profile if it doesn't already exist. Never REPLACE or REMOVE existing profiles.

## Pre-Implementation Check

**BEFORE adding PiTest profile, check if it already exists in the pom.xml:**

If `<profiles>` section with `pitest` profile already exists: Ask user "PiTest profile already exists. Do you want to enhance the existing configuration? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing configuration elements only.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "Mutation testing (PiTest)" in Step 3.

## PiTest Profile Configuration

**ADD this profile to the `<profiles>` section in pom.xml ONLY if it doesn't already exist:**

```xml
<profile>
    <id>pitest</id>
    <activation>
        <activeByDefault>false</activeByDefault>
    </activation>
    <build>
        <plugins>
            <plugin>
                <groupId>org.pitest</groupId>
                <artifactId>pitest-maven</artifactId>
                <version>${maven-plugin-pitest.version}</version>
                <configuration>
                    <targetClasses>
                        <param>REPLACE_WITH_ACTUAL_PACKAGE.*</param>
                    </targetClasses>
                    <targetTests>
                        <param>REPLACE_WITH_ACTUAL_PACKAGE.*</param>
                    </targetTests>
                    <outputFormats>
                        <outputFormat>HTML</outputFormat>
                        <outputFormat>XML</outputFormat>
                    </outputFormats>
                    <mutationThreshold>${coverage.level}</mutationThreshold>
                    <coverageThreshold>${coverage.level}</coverageThreshold>
                    <timestampedReports>false</timestampedReports>
                    <verbose>false</verbose>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>org.pitest</groupId>
                        <artifactId>pitest-junit5-plugin</artifactId>
                        <version>${maven-plugin-pitest-junit5.version}</version>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <id>pitest-mutation-testing</id>
                        <goals>
                            <goal>mutationCoverage</goal>
                        </goals>
                        <phase>verify</phase>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</profile>
```

## Implementation Guidelines

1. **Update package names**: Replace `REPLACE_WITH_ACTUAL_PACKAGE` with the project's actual base package
2. **Configure thresholds**: Use coverage threshold values from Step 2

## Usage Examples

```bash
# Run mutation testing
./mvnw clean verify -Ppitest

# View mutation test reports
open target/pit-reports/index.html
```

## Validation

After adding this profile, verify the configuration:

```bash
# Test PiTest profile
./mvnw clean verify -Ppitest
```
                    
                
#### Step Constraints

- **MUST** only add PiTest profile if mutation testing was selected in Step 3
- **MUST** check if profile already exists before adding
- **MUST** ask user permission before modifying existing profile configuration
- **MUST** update targetClasses and targetTests to match actual project structure
- **MUST** use coverage threshold values from Step 3
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** skip this step entirely if mutation testing was not selected

### Step 11: Security Vulnerability Scanning Profile Configuration

**Purpose**: Configure OWASP Dependency Check profile to scan dependencies for known security vulnerabilities.

**Dependencies**: Only execute if user selected "Security vulnerability scanning (OWASP)" in Step 3. Requires completion of core plugin steps (3, 4, 5).

**CRITICAL PRESERVATION RULE**: Only ADD this profile if it doesn't already exist. Never REPLACE or REMOVE existing profiles.

## Pre-Implementation Check

**BEFORE adding Security profile, check if it already exists in the pom.xml:**

If `<profiles>` section with `security` profile already exists: Ask user "Security profile already exists. Do you want to enhance the existing configuration? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing configuration elements only.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "Security vulnerability scanning (OWASP)" in Step 3.

## Security Profile Configuration

**ADD this profile to the `<profiles>` section in pom.xml ONLY if it doesn't already exist:**

```xml
<profile>
    <id>security</id>
    <activation>
        <activeByDefault>false</activeByDefault>
    </activation>
    <build>
        <plugins>
            <plugin>
                <groupId>org.owasp</groupId>
                <artifactId>dependency-check-maven</artifactId>
                <version>${maven-plugin-dependency-check.version}</version>
                <configuration>
                    <outputDirectory>${project.build.directory}/dependency-check</outputDirectory>
                    <format>ALL</format>
                    <failBuildOnCVSS>7</failBuildOnCVSS>
                    <skipProvidedScope>false</skipProvidedScope>
                    <skipRuntimeScope>false</skipRuntimeScope>
                    <skipSystemScope>false</skipSystemScope>
                    <skipTestScope>false</skipTestScope>
                    <!-- Performance and reliability improvements -->
                    <nvdApiDelay>4000</nvdApiDelay>
                    <nvdMaxRetryCount>3</nvdMaxRetryCount>
                    <nvdValidForHours>24</nvdValidForHours>
                    <!-- Skip analyzers that might cause issues -->
                    <nodeAnalyzerEnabled>false</nodeAnalyzerEnabled>
                    <retireJsAnalyzerEnabled>false</retireJsAnalyzerEnabled>
                </configuration>
                <executions>
                    <execution>
                        <id>dependency-check</id>
                        <goals>
                            <goal>check</goal>
                        </goals>
                        <phase>verify</phase>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</profile>
```

## Usage Examples

```bash
# Run security scan
./mvnw clean verify -Psecurity

# View security reports
open target/dependency-check/dependency-check-report.html
```

## Validation

After adding this profile, verify the configuration:

```bash
# Test Security profile
./mvnw clean verify -Psecurity
```
                    
                
#### Step Constraints

- **MUST** only add Security profile if security scanning was selected in Step 3
- **MUST** check if profile already exists before adding
- **MUST** ask user permission before modifying existing profile configuration
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** skip this step entirely if security scanning was not selected

### Step 12: Static Code Analysis Profile Configuration

**Purpose**: Configure SpotBugs and PMD static analysis profile to detect potential bugs and code quality issues.

**Dependencies**: Only execute if user selected "Static code analysis (SpotBugs, PMD)" in Step 3. Requires completion of core plugin steps (3, 4, 5).

**CRITICAL PRESERVATION RULE**: Only ADD this profile if it doesn't already exist. Never REPLACE or REMOVE existing profiles.

## Pre-Implementation Check

**BEFORE adding Static Analysis profile, check if it already exists in the pom.xml:**

If `<profiles>` section with `find-bugs` profile already exists: Ask user "Static analysis profile already exists. Do you want to enhance the existing configuration? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing configuration elements only.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "Static code analysis (SpotBugs, PMD)" in Step 3.

## Static Analysis Profile Configuration

**ADD this profile to the `<profiles>` section in pom.xml ONLY if it doesn't already exist:**

```xml
<profile>
    <id>find-bugs</id>
    <activation>
        <activeByDefault>false</activeByDefault>
    </activation>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-pmd-plugin</artifactId>
                <version>${maven-plugin-pmd.version}</version>
            </plugin>
            <plugin>
                <groupId>com.github.spotbugs</groupId>
                <artifactId>spotbugs-maven-plugin</artifactId>
                <version>${maven-plugin-spotbugs.version}</version>
                <configuration>
                    <effort>Max</effort>
                    <threshold>Low</threshold>
                    <failOnError>true</failOnError>
                </configuration>
            </plugin>
        </plugins>
    </build>
    <reporting>
        <plugins>
            <!-- SpotBugs reporting for Maven site -->
            <plugin>
                <groupId>com.github.spotbugs</groupId>
                <artifactId>spotbugs-maven-plugin</artifactId>
                <version>${maven-plugin-spotbugs.version}</version>
                <configuration>
                    <effort>Max</effort>
                    <threshold>Low</threshold>
                    <includeFilterFile>src/main/spotbugs/spotbugs-include.xml</includeFilterFile>
                    <excludeFilterFile>src/main/spotbugs/spotbugs-exclude.xml</excludeFilterFile>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-pmd-plugin</artifactId>
                <version>${maven-plugin-pmd.version}</version>
            </plugin>
        </plugins>
    </reporting>
</profile>
```

## Usage Examples

```bash
# Run static analysis
./mvnw clean verify -Pfind-bugs

# Generate reports
./mvnw site -Pfind-bugs

# View reports
open target/site/spotbugs.html
open target/site/pmd.html
```

## Validation

After adding this profile, verify the configuration:

```bash
# Test Static Analysis profile
./mvnw clean verify -Pfind-bugs
```
                    
                
#### Step Constraints

- **MUST** only add Static Analysis profile if static analysis was selected in Step 3
- **MUST** check if profile already exists before adding
- **MUST** ask user permission before modifying existing profile configuration
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** skip this step entirely if static analysis was not selected

### Step 13: SonarQube/SonarCloud Profile Configuration

**Purpose**: Configure SonarQube/SonarCloud profile for comprehensive code quality analysis integration.

**Dependencies**: Only execute if user selected "Sonar" in Step 3 and provided Sonar configuration. Requires completion of core plugin steps (3, 4, 5).

**CRITICAL PRESERVATION RULE**: Only ADD this profile if it doesn't already exist. Never REPLACE or REMOVE existing profiles.

## Pre-Implementation Check

**BEFORE adding Sonar profile, check if it already exists in the pom.xml:**

If `<profiles>` section with `sonar` profile already exists: Ask user "Sonar profile already exists. Do you want to enhance the existing configuration? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing configuration elements only.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "Sonar" in Step 3 and provided Sonar configuration.

## Sonar Profile Configuration

**ADD this profile to the `<profiles>` section in pom.xml ONLY if it doesn't already exist:**

```xml
<profile>
    <id>sonar</id>
    <activation>
        <activeByDefault>false</activeByDefault>
    </activation>
    <properties>
        <!-- SonarCloud configuration -->
        <sonar.host.url>REPLACE_WITH_SONAR_HOST_URL</sonar.host.url>
        <sonar.organization>REPLACE_WITH_SONAR_ORGANIZATION</sonar.organization>
        <sonar.projectKey>REPLACE_WITH_SONAR_PROJECT_KEY</sonar.projectKey>
        <sonar.projectName>REPLACE_WITH_SONAR_PROJECT_NAME</sonar.projectName>
        <sonar.projectVersion>${project.version}</sonar.projectVersion>
        <sonar.sources>src/main/java</sonar.sources>
        <sonar.tests>src/test/java</sonar.tests>
        <sonar.java.binaries>target/classes</sonar.java.binaries>
        <sonar.java.test.binaries>target/test-classes</sonar.java.test.binaries>
        <sonar.jacoco.reportPath>target/jacoco.exec</sonar.jacoco.reportPath>
        <sonar.junit.reportPaths>target/surefire-reports</sonar.junit.reportPaths>
        <sonar.coverage.exclusions>**/*Test.java,**/*IT.java</sonar.coverage.exclusions>
        <sonar.java.source>${java.version}</sonar.java.source>
    </properties>
    <build>
        <plugins>
            <plugin>
                <groupId>org.sonarsource.scanner.maven</groupId>
                <artifactId>sonar-maven-plugin</artifactId>
                <version>${maven-plugin-sonar.version}</version>
            </plugin>
        </plugins>
    </build>
</profile>
```

## Implementation Guidelines

1. **Replace Sonar placeholders** with actual values from Step 3:
    - `REPLACE_WITH_SONAR_HOST_URL`
    - `REPLACE_WITH_SONAR_ORGANIZATION`
    - `REPLACE_WITH_SONAR_PROJECT_KEY`
    - `REPLACE_WITH_SONAR_PROJECT_NAME`

## Usage Examples

```bash
# Run Sonar analysis (requires token)
./mvnw clean verify sonar:sonar -Psonar -Dsonar.login=YOUR_TOKEN

# For SonarCloud with GitHub Actions
./mvnw clean verify sonar:sonar -Psonar -Dsonar.login=$SONAR_TOKEN

# Combined with JaCoCo
./mvnw clean verify sonar:sonar -Pjacoco,sonar -Dsonar.login=$SONAR_TOKEN
```

## Validation

After adding this profile, verify the configuration:

```bash
# Validate Sonar profile (requires token)
./mvnw clean verify sonar:sonar -Psonar -Dsonar.login=YOUR_TOKEN
```
                    
                
#### Step Constraints

- **MUST** only add Sonar profile if Sonar integration was selected in Step 3
- **MUST** check if profile already exists before adding
- **MUST** ask user permission before modifying existing profile configuration
- **MUST** configure Sonar properties with actual values from Step 3
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** skip this step entirely if Sonar was not selected

### Step 14: Spotless Code Formatting Plugin Configuration

**Purpose**: Configure Spotless Maven plugin to automatically format and enforce code style consistency.

**Dependencies**: Only execute if user selected "Format source code (Spotless)" in Step 3. Requires completion of core plugin steps (3, 4, 5).

**CRITICAL PRESERVATION RULE**: Only ADD this plugin if it doesn't already exist. Never REPLACE or REMOVE existing plugins.

## Pre-Implementation Check

**BEFORE adding Spotless plugin, check if it already exists in the pom.xml:**

If spotless-maven-plugin already exists: Ask user "Spotless plugin already exists. Do you want to enhance the existing configuration? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing configuration elements only.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "Format source code (Spotless)" in Step 3.

## Spotless Plugin Configuration

**ADD this plugin to the `<build><plugins>` section ONLY if it doesn't already exist:**

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

## Usage Examples

```bash
# Check code formatting
./mvnw spotless:check

# Apply code formatting
./mvnw spotless:apply

# Integration with build
./mvnw clean compile  # Will fail if formatting violations exist
```

## Validation

After adding this plugin, verify the configuration:

```bash
# Test Spotless configuration
./mvnw spotless:check
```
                    
                
#### Step Constraints

- **MUST** only add Spotless plugin if code formatting was selected in Step 3
- **MUST** check if plugin already exists before adding
- **MUST** ask user permission before modifying existing plugin configuration
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** skip this step entirely if code formatting was not selected

### Step 15: Versions Maven Plugin Configuration

**Purpose**: Configure Versions Maven plugin to help manage and update dependency and plugin versions systematically.

**Dependencies**: Only execute if user selected "Version management" in Step 3. Requires completion of core plugin steps (3, 4, 5).

**CRITICAL PRESERVATION RULE**: Only ADD this plugin if it doesn't already exist. Never REPLACE or REMOVE existing plugins.

## Pre-Implementation Check

**BEFORE adding Versions plugin, check if it already exists in the pom.xml:**

If versions-maven-plugin already exists: Ask user "Versions plugin already exists. Do you want to enhance the existing configuration? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing configuration elements only.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "Version management" in Step 3.

## Versions Plugin Configuration

**ADD this plugin to the `<build><plugins>` section ONLY if it doesn't already exist:**

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

## Usage Examples

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

## Validation

After adding this plugin, verify the configuration:

```bash
# Test Versions plugin configuration
./mvnw versions:display-plugin-updates
```
                    
                
#### Step Constraints

- **MUST** only add Versions plugin if version management was selected in Step 3
- **MUST** check if plugin already exists before adding
- **MUST** ask user permission before modifying existing plugin configuration
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** skip this step entirely if version management was not selected

### Step 16: Git Commit ID Plugin Configuration

**Purpose**: Configure Git Commit ID plugin to include Git commit information in the build artifacts for traceability.

**Dependencies**: Only execute if user selected "Build information tracking" in Step 3. Requires completion of core plugin steps (3, 4, 5).

**CRITICAL PRESERVATION RULE**: Only ADD this plugin if it doesn't already exist. Never REPLACE or REMOVE existing plugins.

## Pre-Implementation Check

**BEFORE adding Git Commit ID plugin, check if it already exists in the pom.xml:**

If git-commit-id-plugin already exists: Ask user "Git Commit ID plugin already exists. Do you want to enhance the existing configuration? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing configuration elements only.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "Build information tracking" in Step 3.

## Git Commit ID Plugin Configuration

**ADD this plugin to the `<build><plugins>` section ONLY if it doesn't already exist:**

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

## Usage Examples

```bash
# Build with git information
./mvnw clean package

# Git properties will be available at runtime
cat target/classes/git.properties
```

**Access in Java code:**
```java
Properties gitProps = new Properties();
gitProps.load(getClass().getResourceAsStream("/git.properties"));
String commitId = gitProps.getProperty("git.commit.id");
String buildTime = gitProps.getProperty("git.build.time");
```

## Validation

After adding this plugin, verify the configuration:

```bash
# Test Git Commit ID plugin
./mvnw clean package
cat target/classes/git.properties
```
                    
                
#### Step Constraints

- **MUST** only add Git Commit ID plugin if build information tracking was selected in Step 3
- **MUST** check if plugin already exists before adding
- **MUST** ask user permission before modifying existing plugin configuration
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** skip this step entirely if build information tracking was not selected
- **MUST** ensure project is in a git repository for proper functionality

### Step 17: Flatten Maven Plugin Configuration

**Purpose**: Configure Flatten Maven plugin to flatten POM files for library publishing to Maven repositories.

**Dependencies**: Only execute if user selected "Java Library" as project nature in Step 3. Requires completion of core plugin steps (3, 4, 5).

**CRITICAL PRESERVATION RULE**: Only ADD this plugin if it doesn't already exist. Never REPLACE or REMOVE existing plugins.

## Pre-Implementation Check

**BEFORE adding Flatten plugin, check if it already exists in the pom.xml:**

If flatten-maven-plugin already exists: Ask user "Flatten plugin already exists. Do you want to enhance the existing configuration? (y/n)"

If user says "n": Skip this step entirely.
If user says "y": Proceed with adding missing configuration elements only.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "Java Library" as project nature in Step 3.

## Flatten Plugin Configuration

**ADD this plugin to the `<build><plugins>` section ONLY if it doesn't already exist:**

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

## Usage Examples

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

## Validation

After adding this plugin, verify the configuration:

```bash
# Test Flatten plugin
./mvnw clean package
ls target/.flattened-pom.xml
```
                    
                
#### Step Constraints

- **MUST** only add Flatten plugin if "Java Library" was selected as project nature in Step 3
- **MUST** check if plugin already exists before adding
- **MUST** ask user permission before modifying existing plugin configuration
- **MUST** use properties configured in Step 4 for plugin versions
- **MUST** skip this step entirely if project nature is not "Java Library"

### Step 18: JMH (Java Microbenchmark Harness) Profile Configuration

**Purpose**: Configure JMH (Java Microbenchmark Harness) profile for performance benchmarking with proper source directories and build configuration.

**Dependencies**: Only execute if user selected "JMH" in Step 3. Requires completion of core plugin steps (3, 4, 5).

**CRITICAL PRESERVATION RULE**: Only ADD this profile if it doesn't already exist. Never REPLACE or REMOVE existing profiles.

**CRITICAL PREREQUISITE**: This step requires the project to be a single-module Maven project. Multi-module projects are not supported for JMH integration.

## Pre-Implementation Checks

**BEFORE adding JMH profile, perform these mandatory checks:**

1. **Check for multi-module configuration**: Scan pom.xml for `<modules>` section

   If `<modules>` section exists: **STOP IMMEDIATELY** and inform user: "JMH profile cannot be added to multi-module Maven projects. JMH requires a single-module project structure for proper benchmark execution. Please configure JMH in individual modules instead."

2. **Check for existing JMH profile**:

   If `<profiles>` section with `jmh` profile already exists: Ask user "JMH profile already exists. Do you want to enhance the existing configuration? (y/n)"

   If user says "n": Skip this step entirely.
   If user says "y": Proceed with adding missing configuration elements only.

**CONDITIONAL EXECUTION**: Only execute this step if user selected "JMH" in Step 3 AND project is single-module.

## JMH Profile Configuration

**ADD this profile to the `<profiles>` section in pom.xml ONLY if it doesn't already exist:**

```xml
<profile>
    <id>jmh</id>
    <activation>
        <activeByDefault>false</activeByDefault>
    </activation>
    <dependencies>
        <dependency>
            <groupId>org.openjdk.jmh</groupId>
            <artifactId>jmh-core</artifactId>
            <version>${jmh.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjdk.jmh</groupId>
            <artifactId>jmh-generator-annprocess</artifactId>
            <version>${jmh.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <!-- Add benchmark source directory -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <version>${maven-plugin-build-helper.version}</version>
                <executions>
                    <execution>
                        <id>add-jmh-source</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>add-source</goal>
                        </goals>
                        <configuration>
                            <sources>
                                <source>src/jmh/java</source>
                            </sources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <!-- Compile JMH benchmarks -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-plugin-compiler.version}</version>
                <configuration>
                    <release>${java.version}</release>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.openjdk.jmh</groupId>
                            <artifactId>jmh-generator-annprocess</artifactId>
                            <version>${jmh.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>

            <!-- Create executable benchmark JAR -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>${maven-plugin-shade.version}</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <finalName>jmh-benchmarks</finalName>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>org.openjdk.jmh.Main</mainClass>
                                </transformer>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                            </transformers>
                            <filters>
                                <filter>
                                    <!-- Exclude signatures -->
                                    <artifact>*:*</artifact>
                                    <excludes>
                                        <exclude>META-INF/*.SF</exclude>
                                        <exclude>META-INF/*.DSA</exclude>
                                        <exclude>META-INF/*.RSA</exclude>
                                        <exclude>META-INF/MANIFEST.MF</exclude>
                                    </excludes>
                                </filter>
                            </filters>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</profile>
```

## Directory Structure Setup

**After adding the profile, create the benchmark source directory:**

```bash
# Create JMH source directory
mkdir -p src/jmh/java

# Create sample benchmark directory structure based on main package
# Example: if main package is com.example.demo, create:
mkdir -p src/jmh/java/com/example/demo/benchmarks
```

## Implementation Guidelines

1. **Verify single-module structure**: Ensure no `<modules>` section exists in pom.xml
2. **Create benchmark source directory**: `src/jmh/java` for benchmark classes
3. **Follow JMH naming conventions**: Benchmark classes should end with `Benchmark` suffix
4. **Package structure**: Mirror main source package structure in `src/jmh/java`

## Sample Benchmark Class

**Create a sample benchmark in `src/jmh/java/[your-package]/benchmarks/FibonacciBenchmark.java`:**

```java
package info.jab.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class FibonacciBenchmark {

    private static final int FIBONACCI_N = 20;

    @Benchmark
    public long testFibonacciRecursive() {
        return FibonacciCalculator.fibonacciRecursive(FIBONACCI_N);
    }

    @Benchmark
    public long testFibonacciIterative() {
        return FibonacciCalculator.fibonacciIterative(FIBONACCI_N);
    }

    /**
     * Inner class that implements Fibonacci calculation in two different ways
     */
    static class FibonacciCalculator {

        /**
         * Recursive implementation of Fibonacci sequence
         * Time complexity: O(2^n) - exponential
         * Space complexity: O(n) - due to call stack
         */
        public static long fibonacciRecursive(int n) {
            if (n <= 1) {
                return n;
            }
            return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
        }

        /**
         * Iterative implementation of Fibonacci sequence
         * Time complexity: O(n) - linear
         * Space complexity: O(1) - constant
         */
        public static long fibonacciIterative(int n) {
            if (n <= 1) {
                return n;
            }

            long prev = 0;
            long curr = 1;

            for (int i = 2; i <= n; i++) {
                long next = prev + curr;
                prev = curr;
                curr = next;
            }

            return curr;
        }
    }

    /**
     * Main method to run benchmarks with JSON output configuration
     */
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(FibonacciBenchmark.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .result("jmh-fibonacci-benchmark-results.json")
                .build();

        new Runner(options).run();
    }
}
```

## Validation

After adding this profile, verify the configuration:

```bash
# Test JMH profile compilation
./mvnw clean compile -Pjmh

# Build JMH benchmarks
./mvnw clean package -Pjmh

# Verify JAR creation
ls target/jmh-benchmarks.jar

# List available benchmarks
java -jar target/jmh-benchmarks.jar -l

# Show help
java -jar target/jmh-benchmarks.jar -h

# Run benchmark
java -cp target/jmh-benchmarks.jar info.jab.demo.benchmarks.FibonacciBenchmark -wi 1 -i 1 -f 1

# Verify that results are generated
ls jmh-fibonacci-benchmark-results.json

# Share references to JMH

- https://openjdk.org/projects/code-tools/jmh/
- https://jmh.morethan.io
```
                    
                
#### Step Constraints

- **MUST** only add JMH profile if "JMH" was selected in Step 3
- **MUST** verify project is single-module (no `<modules>` section) before proceeding
- **MUST** check if profile already exists before adding
- **MUST** ask user permission before modifying existing profile configuration
- **MUST** use properties configured in Step 4 for plugin and dependency versions
- **MUST** create `src/jmh/java` directory structure for benchmarks
- **MUST** skip this step entirely if JMH was not selected OR if project has modules
- **MUST** stop immediately and inform user if multi-module project detected
- **MUST** configure build-helper-maven-plugin to add JMH source directory
- **MUST** configure maven-shade-plugin to create executable benchmark JAR
- **MUST** verify that JSON report is generated by executing benchmark and checking for `jmh-fibonacci-benchmark-results.json fil`


## Output Format

- Ask questions one by one following the template exactly in Step 3
- Execute steps 4-18 only based on user selections from Step 3
- Skip entire steps if no relevant features were selected
- Implement only requested features based on user selections
- Follow template specifications exactly for all configurations
- Provide clear progress feedback showing which step is being executed
- Provide usage examples only for features that were added

## Safeguards

- **MANDATORY**: Complete Step 1 (existing configuration analysis) before making any changes
- **NEVER remove or replace existing plugins** - only add new plugins that don't already exist
- **NEVER remove or replace existing properties** - only add new properties that don't conflict
- **ASK USER before overriding** any existing configuration element
- Verify changes with the command: `mvn validate` or `./mvnw validate`
- Always read template files fresh using file_search and read_file tools
- Never proceed to next step without completing dependencies
- Template adherence is mandatory - no exceptions or simplified versions
- Validate that all plugin configurations reference properties from Step 4
- **DOCUMENT what was added vs what was preserved** in the final summary