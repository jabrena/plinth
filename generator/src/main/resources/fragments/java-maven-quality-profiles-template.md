# Java Maven Quality Analysis Profiles Template

This template provides Maven quality analysis profile configurations that are conditionally added based on user selections.

**Usage**: Referenced from Step 6 of the modular Maven plugins rule.

**Purpose**: Configure quality analysis profiles only when specific quality features are selected.

**Dependencies**: Only execute if user selected quality analysis features in Step 2.

## Conditional Quality Profiles

### 1. JaCoCo Code Coverage Profile

**When to add**: Only if user selected "Code coverage reporting (JaCoCo)" in Step 2.

**Purpose**: Analyze and enforce code coverage thresholds with detailed reporting.

Add this profile to the `<profiles>` section in pom.xml:

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

### 2. PiTest Mutation Testing Profile

**When to add**: Only if user selected "Mutation testing (PiTest)" in Step 2.

**Purpose**: Analyze test quality by introducing mutations and verifying test detection.

Add this profile to the `<profiles>` section in pom.xml:

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

### 3. Security Vulnerability Scanning Profile

**When to add**: Only if user selected "Security vulnerability scanning (OWASP)" in Step 2.

**Purpose**: Scan dependencies for known security vulnerabilities.

Add this profile to the `<profiles>` section in pom.xml:

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

### 4. Static Code Analysis Profile

**When to add**: Only if user selected "Static code analysis (SpotBugs, PMD)" in Step 2.

**Purpose**: Perform static analysis to detect potential bugs and code quality issues.

Add this profile to the `<profiles>` section in pom.xml:

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

### 5. SonarQube/SonarCloud Profile

**When to add**: Only if user selected "Sonar" in Step 2 and provided Sonar configuration.

**Purpose**: Integrate with SonarQube/SonarCloud for comprehensive code quality analysis.

Add this profile to the `<profiles>` section in pom.xml:

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

### Important Replacements

1. **Package Names**: Replace `REPLACE_WITH_ACTUAL_PACKAGE` with the project's actual base package (e.g., `com.example.myapp`)
2. **Sonar Configuration**: Replace Sonar placeholders with actual values from Step 2:
   - `REPLACE_WITH_SONAR_HOST_URL`
   - `REPLACE_WITH_SONAR_ORGANIZATION`
   - `REPLACE_WITH_SONAR_PROJECT_KEY`
   - `REPLACE_WITH_SONAR_PROJECT_NAME`

### Profile Management

1. **Add profiles selectively**: Only include profiles for features selected in Step 2
2. **Verify property references**: Ensure coverage.level and plugin version properties are defined in Step 3
3. **Test isolation**: Profiles are inactive by default and must be explicitly activated

## Usage Examples

### Code Coverage with JaCoCo

```bash
# Run tests with coverage
./mvnw clean verify -Pjacoco

# View coverage reports
open target/site/jacoco/index.html
```

### Mutation Testing with PiTest

```bash
# Run mutation testing
./mvnw clean verify -Ppitest

# View mutation test reports
open target/pit-reports/index.html
```

### Security Vulnerability Scanning

```bash
# Run security scan
./mvnw clean verify -Psecurity

# View security reports
open target/dependency-check/dependency-check-report.html
```

### Static Code Analysis

```bash
# Run static analysis
./mvnw clean verify -Pfind-bugs

# Generate reports
./mvnw site -Pfind-bugs

# View reports
open target/site/spotbugs.html
open target/site/pmd.html
```

### SonarQube Analysis

```bash
# Run Sonar analysis (requires token)
./mvnw clean verify sonar:sonar -Psonar -Dsonar.login=YOUR_TOKEN

# For SonarCloud with GitHub Actions
./mvnw clean verify sonar:sonar -Psonar -Dsonar.login=$SONAR_TOKEN
```

### Combined Quality Analysis

```bash
# Run all quality checks
./mvnw clean verify -Pjacoco,pitest,security,find-bugs

# Run quality checks and upload to Sonar
./mvnw clean verify sonar:sonar -Pjacoco,sonar -Dsonar.login=$SONAR_TOKEN
```

## Validation

After adding quality profiles, verify the configuration:

```bash
# Validate plugin configuration
./mvnw validate

# Test individual profiles
./mvnw clean verify -Pjacoco
./mvnw clean verify -Psecurity

# List all available profiles
./mvnw help:all-profiles
```

## Expected Outcomes

### With JaCoCo
- Coverage reports in `target/site/jacoco/`
- Build fails if coverage below threshold
- Integration with IDE coverage tools

### With PiTest
- Mutation test reports in `target/pit-reports/`
- Test quality metrics and recommendations
- Identification of weak test assertions

### With Security Scanning
- Vulnerability reports in `target/dependency-check/`
- CVE database integration
- Build fails on high-severity vulnerabilities

### With Static Analysis
- SpotBugs reports for potential bugs
- PMD reports for code quality issues
- Integration with Maven site generation

### With Sonar
- Centralized quality metrics dashboard
- Historical quality trends
- Integration with CI/CD pipelines
