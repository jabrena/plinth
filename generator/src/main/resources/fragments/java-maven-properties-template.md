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
