---
author: Juan Antonio Breña Moral
version: 0.11.0-SNAPSHOT
---
# Maven Best Practices

## Role

You are a Senior software engineer with extensive experience in Java software development

## Goal

Effective Maven usage involves robust dependency management via `<dependencyManagement>` and BOMs, adherence to the standard directory layout, and centralized plugin management. Build profiles should be used for environment-specific configurations. POMs must be kept readable and maintainable with logical structure and properties for versions. Custom repositories should be declared explicitly and their use minimized, preferably managed via a central repository manager. Modern Maven practices include comprehensive security scanning with OWASP dependency checks, build performance optimization through parallel execution and JVM tuning, and regular dependency analysis to maintain clean and secure dependency trees.

### Core Principles Behind Maven

Maven is built on several foundational principles that guide its design and usage:

**1. Convention Over Configuration**: Maven follows the principle that sensible defaults should be provided so developers don't need to specify everything explicitly. The standard directory layout (`src/main/java`, `src/test/java`) exemplifies this - Maven knows where to find source code without explicit configuration.
**2. Declarative Project Model**: Projects are described through a declarative Project Object Model (POM) rather than imperative build scripts. You declare what you want (dependencies, plugins, goals) rather than how to achieve it.
**3. Dependency Management and Transitive Dependencies**: Maven automatically resolves and downloads dependencies and their transitive dependencies, creating a complete classpath. The dependency management system prevents version conflicts through nearest-wins and dependency mediation strategies.
**4. Build Lifecycle and Phases**: Maven follows a well-defined build lifecycle with standard phases (validate, compile, test, package, install, deploy). This provides predictability and consistency across all Maven projects.
**5. Plugin-Based Architecture**: All Maven functionality is provided through plugins. Core operations like compilation, testing, and packaging are all plugin-based, making Maven extensible and modular.
**6. Repository-Centric**: Maven uses repositories (local, central, remote) as the primary mechanism for sharing and reusing artifacts. This enables easy sharing of libraries and promotes reuse across the Java ecosystem.
**7. Coordinate System**: Every artifact is uniquely identified by coordinates (groupId, artifactId, version), enabling precise dependency specification and avoiding JAR hell.
**8. Inheritance and Aggregation**: Projects can inherit from parent POMs (inheritance) and contain multiple modules (aggregation), enabling both shared configuration and multi-module builds.

## Constraints

Before applying Maven best practices recommendations, ensure the project is in a valid state by running Maven validation. This helps identify any existing configuration issues that need to be resolved first.

- **MANDATORY**: Run `./mvnw validate` or `mvn validate` before applying any Maven best practices recommendations
- **VERIFY**: Ensure all validation errors are resolved before proceeding with POM modifications
- **PREREQUISITE**: Project must compile and pass basic validation checks before optimization
- **SAFETY**: If validation fails, not continue and ask the user to fix the issues before continuing

## Examples

### Table of contents

- Example 1: Effective Dependency Management
- Example 2: Standard Directory Layout
- Example 3: Plugin Management and Configuration
- Example 4: Use Build Profiles for Environment-Specific Configurations
- Example 5: Keep POMs Readable and Maintainable
- Example 6: Manage Repositories Explicitly
- Example 7: Centralize Version Management with Properties
- Example 8: Security Best Practices
- Example 9: Build Performance Optimization
- Example 10: Dependency Analysis and Cleanup
- Example 11: Modern Java Features Configuration

### Example 1: Effective Dependency Management

Title: Manage Dependencies Effectively using `dependencyManagement` and BOMs
Description: Use the `<dependencyManagement>` section in parent POMs or import Bill of Materials (BOMs) to centralize and control dependency versions. This helps avoid version conflicts and ensures consistency across multi-module projects. Avoid hardcoding versions directly in `<dependencies>` when managed elsewhere.

**Good example:**

```xml
<!-- Parent POM -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>my-parent</artifactId>
  <version>1.0.0</version>
  <packaging>pom</packaging>

  <properties>
    <spring.version>5.3.23</spring.version>
    <junit.version>5.9.0</junit.version>
  </properties>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
      </dependency>
      <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope>
      </dependency>
      <!-- Import a BOM -->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-dependencies</artifactId>
          <version>2.7.5</version>
          <type>pom</type>
          <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
</project>

<!-- Child POM -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.example</groupId>
    <artifactId>my-parent</artifactId>
    <version>1.0.0</version>
  </parent>
  <artifactId>my-module</artifactId>

  <dependencies>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <!-- Version is inherited from parent's dependencyManagement -->
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <!-- Version and scope inherited -->
    </dependency>
  </dependencies>
</project>

```

**Bad example:**

```xml
<!-- Child POM hardcoding versions -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>my-other-module</artifactId>
  <version>1.0.0</version>

  <dependencies>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.3.20</version> <!-- Hardcoded, may differ from parent's intention -->
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>5.8.1</version> <!-- Different version, potential conflict -->
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>

```

### Example 2: Standard Directory Layout

Title: Adhere to the Standard Directory Layout
Description: Follow Maven's convention for directory structure (`src/main/java`, `src/main/resources`, `src/test/java`, `src/test/resources`, etc.). This makes projects easier to understand and build, as Maven relies on these defaults.

**Good example:**

```text
my-app/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/example/myapp/App.java
    │   └── resources/
    │       └── application.properties
    └── test/
        ├── java/
        │   └── com/example/myapp/AppTest.java
        └── resources/
            └── test-data.xml

```

**Bad example:**

```text
my-app/
├── pom.xml
├── sources/  <!-- Non-standard -->
│   └── com/example/myapp/App.java
├── res/      <!-- Non-standard -->
│   └── config.properties
└── tests/    <!-- Non-standard -->
    └── com/example/myapp/AppTest.java
<!-- This would require explicit configuration in pom.xml to specify source/resource directories -->

```

### Example 3: Plugin Management and Configuration

Title: Manage Plugin Versions and Configurations Centrally
Description: Use `<pluginManagement>` in a parent POM to define plugin versions and common configurations. Child POMs can then use the plugins without specifying versions, ensuring consistency. Override configurations in child POMs only when necessary.

**Good example:**

```xml
<!-- Parent POM -->
<project>
  <!-- ... -->
  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.10.1</version>
          <configuration>
            <source>17</source>
            <target>17</target>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
</project>

<!-- Child POM -->
<project>
  <!-- ... -->
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <!-- Version and basic configuration inherited -->
      </plugin>
    </plugins>
  </build>
</project>

```

**Bad example:**

```xml
<!-- Child POM -->
<project>
  <!-- ... -->
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.1</version> <!-- Different version, potentially older/incompatible -->
        <configuration>
          <source>11</source>   <!-- Different configuration -->
          <target>11</target>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>

```

### Example 4: Use Build Profiles for Environment-Specific Configurations

Title: Employ Build Profiles for Environment-Specific Settings
Description: Use Maven profiles to customize build settings for different environments (e.g., dev, test, prod) or other conditional scenarios. This can include different dependencies, plugin configurations, or properties. Activate profiles via command line, OS, JDK, or file presence.

**Good example:**

```xml
<project>
  <!-- ... -->
  <profiles>
    <profile>
      <id>dev</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <database.url>jdbc:h2:mem:devdb</database.url>
      </properties>
    </profile>
    <profile>
      <id>prod</id>
      <properties>
        <database.url>jdbc:postgresql://prodserver/mydb</database.url>
      </properties>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-antrun-plugin</artifactId>
            <version>3.1.0</version>
            <executions>
              <execution>
                <phase>package</phase>
                <goals><goal>run</goal></goals>
                <configuration>
                  <target>
                    <!-- Minify JS/CSS for prod -->
                    <echo>Simulating minification for prod</echo>
                  </target>
                </configuration>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
</project>
<!-- Activation: mvn clean install -P prod -->

```

**Bad example:**

```xml
<!-- Commented out sections for different environments -->
<project>
  <!-- ... -->
  <properties>
    <!-- <database.url>jdbc:h2:mem:devdb</database.url> -->
    <database.url>jdbc:postgresql://prodserver/mydb</database.url> <!-- Manually switch by commenting/uncommenting -->
  </properties>
</project>

```

### Example 5: Keep POMs Readable and Maintainable

Title: Structure POMs Logically for Readability
Description: Organize your `pom.xml` sections in a consistent order (e.g., project coordinates, parent, properties, dependencyManagement, dependencies, build, profiles, repositories). Use properties for recurring versions or values. Add comments for complex configurations.

**Good example:**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- Project Coordinates -->
    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>My Application</name>
    <description>A sample application.</description>

    <!-- Parent (if any) -->
    <!-- ... -->

    <!-- Properties -->
    <properties>
        <java.version>17</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <some.library.version>2.5.1</some.library.version>
    </properties>

    <!-- Dependency Management -->
    <dependencyManagement>
        <!-- ... -->
    </dependencyManagement>

    <!-- Dependencies -->
    <dependencies>
        <dependency>
            <groupId>org.some.library</groupId>
            <artifactId>some-library-core</artifactId>
            <version>${some.library.version}</version>
        </dependency>
        <!-- ... -->
    </dependencies>

    <!-- Build Configuration -->
    <build>
        <!-- ... -->
    </build>

    <!-- Profiles (if any) -->
    <!-- ... -->

    <!-- Repositories and Plugin Repositories (if needed) -->
    <!-- ... -->
</project>

```

**Bad example:**

```xml
<!-- Haphazard order, missing properties for versions -->
<project>
  <dependencies>
    <dependency>
      <groupId>org.some.library</groupId>
      <artifactId>some-library-core</artifactId>
      <version>2.5.1</version> <!-- Version hardcoded, repeated elsewhere -->
    </dependency>
  </dependencies>
  <modelVersion>4.0.0</modelVersion>
  <build>
    <!-- ... -->
  </build>
  <groupId>com.example</groupId>
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  <artifactId>my-app</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</project>

```

### Example 6: Manage Repositories Explicitly

Title: Declare Custom Repositories Explicitly and Minimize Their Use
Description: Prefer dependencies from Maven Central. If custom repositories are necessary, declare them in the `<repositories>` section and `<pluginRepositories>` for plugins. It's often better to manage these in a company-wide Nexus/Artifactory instance configured in `settings.xml` rather than per-project POMs. Avoid relying on transitive repositories.

**Good example:**

```xml
<project>
  <!-- ... -->
  <repositories>
    <repository>
      <id>my-internal-repo</id>
      <url>https://nexus.example.com/repository/maven-releases/</url>
    </repository>
  </repositories>
  <pluginRepositories>
    <pluginRepository>
      <id>my-internal-plugins</id>
      <url>https://nexus.example.com/repository/maven-plugins/</url>
    </pluginRepository>
  </pluginRepositories>
</project>
<!-- Better: Configure these in settings.xml and use a repository manager -->

```

**Bad example:**

```xml
<!-- No explicit repository for a non-central artifact, relying on developer's local settings or transitive ones -->
<project>
  <!-- ... -->
  <dependencies>
    <dependency>
      <groupId>com.internal.stuff</groupId>
      <artifactId>internal-lib</artifactId>
      <version>1.0</version>
      <!-- If this is not in Maven Central, the build will fail unless
           the repository is configured in settings.xml or a parent POM.
           Relying on implicit configurations makes builds less portable. -->
    </dependency>
  </dependencies>
</project>

```

### Example 7: Centralize Version Management with Properties

Title: Use Properties to Manage Dependency and Plugin Versions
Description: Define all dependency and plugin versions in the `<properties>` section rather than hardcoding them throughout the POM. This centralizes version management, makes updates easier, reduces duplication, and helps maintain consistency across related dependencies. Use consistent property naming conventions: `maven-plugin-[name].version` for Maven plugins, simple names like `[library].version` for dependencies, and descriptive names for quality thresholds like `coverage.level`.

**Good example:**

```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>my-app</artifactId>
  <version>1.0.0</version>

  <properties>
    <!-- Core build properties -->
    <java.version>25</java.version>
    <maven.version>3.9.10</maven.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>

    <!-- Dependency versions -->
    <jackson.version>2.18.2</jackson.version>
    <junit.version>5.11.3</junit.version>
    <mockito.version>5.14.2</mockito.version>
    <logback.version>1.5.15</logback.version>

    <!-- Maven plugin versions -->
    <maven-plugin-compiler.version>3.14.0</maven-plugin-compiler.version>
    <maven-plugin-surefire.version>3.5.3</maven-plugin-surefire.version>
    <maven-plugin-failsafe.version>3.5.3</maven-plugin-failsafe.version>
    <maven-plugin-enforcer.version>3.5.0</maven-plugin-enforcer.version>
    <maven-plugin-dependency.version>3.8.1</maven-plugin-dependency.version>

    <!-- Third-party plugin versions -->
    <maven-plugin-jacoco.version>0.8.13</maven-plugin-jacoco.version>
    <maven-plugin-owasp.version>11.1.1</maven-plugin-owasp.version>
    <extra-enforcer-rules.version>1.8.0</extra-enforcer-rules.version>

    <!-- Quality thresholds -->
    <coverage.level>80</coverage.level>
    <mutation.level>70</mutation.level>
  </properties>

  <dependencies>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>${jackson.version}</version>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>${junit.version}</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-core</artifactId>
      <version>${mockito.version}</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>${maven-plugin-compiler.version}</version>
        <configuration>
          <source>${java.version}</source>
          <target>${java.version}</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>${maven-plugin-surefire.version}</version>
      </plugin>
      <plugin>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>${maven-plugin-jacoco.version}</version>
      </plugin>
    </plugins>
  </build>
</project>

```

**Bad example:**

```xml
<!-- Hardcoded versions scattered throughout the POM -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>my-app</artifactId>
  <version>1.0.0</version>

  <properties>
    <java.version>17</java.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.15.3</version> <!-- Hardcoded version -->
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>2.15.2</version> <!-- Different version of same library family! -->
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>5.10.1</version> <!-- Hardcoded version -->
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>5.9.3</version> <!-- Different JUnit version! -->
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.11.0</version> <!-- Hardcoded plugin version -->
        <configuration>
          <source>17</source> <!-- Hardcoded Java version instead of using property -->
          <target>17</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.2.2</version> <!-- Hardcoded plugin version -->
      </plugin>
      <plugin>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>0.8.10</version> <!-- Hardcoded and potentially outdated -->
      </plugin>
    </plugins>
  </build>
</project>

```


### Example 8: Security Best Practices

Title: Implement Comprehensive Security Scanning and Vulnerability Management
Description: Implement security best practices including OWASP dependency vulnerability scanning, secure repository management, and dependency verification. Use the OWASP Dependency-Check plugin to identify known vulnerabilities in project dependencies and establish security gates in your build process.

**Good example:**

```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>secure-app</artifactId>
  <version>1.0.0</version>

  <properties>
    <java.version>25</java.version>
    <maven.version>3.9.10</maven.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

    <!-- Security plugin versions -->
    <maven-plugin-owasp.version>11.1.1</maven-plugin-owasp.version>
    <maven-plugin-enforcer.version>3.5.0</maven-plugin-enforcer.version>
    <extra-enforcer-rules.version>1.8.0</extra-enforcer-rules.version>

    <!-- Security thresholds -->
    <security.cvss.threshold>7.0</security.cvss.threshold>
  </properties>

  <build>
    <plugins>
      <!-- OWASP Dependency Check Plugin -->
      <plugin>
        <groupId>org.owasp</groupId>
        <artifactId>dependency-check-maven</artifactId>
        <version>${maven-plugin-owasp.version}</version>
        <configuration>
          <failBuildOnCVSS>${security.cvss.threshold}</failBuildOnCVSS>
          <suppressionFiles>
            <suppressionFile>owasp-suppressions.xml</suppressionFile>
          </suppressionFiles>
          <formats>
            <format>HTML</format>
            <format>JSON</format>
          </formats>
          <assemblyAnalyzerEnabled>false</assemblyAnalyzerEnabled>
          <nodeAnalyzerEnabled>false</nodeAnalyzerEnabled>
          <retireJsAnalyzerEnabled>false</retireJsAnalyzerEnabled>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>check</goal>
            </goals>
          </execution>
        </executions>
      </plugin>

      <!-- Enhanced Maven Enforcer Plugin with Security Rules -->
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
            <id>enforce-security</id>
            <goals>
              <goal>enforce</goal>
            </goals>
            <configuration>
              <rules>
                <!-- Basic security rules -->
                <requireMavenVersion>
                  <version>${maven.version}</version>
                </requireMavenVersion>
                <requireJavaVersion>
                  <version>${java.version}</version>
                </requireJavaVersion>

                <!-- Dependency security rules -->
                <banCircularDependencies/>
                <dependencyConvergence/>
                <banDuplicatePomDependencyVersions/>

                <!-- Ban insecure dependencies -->
                <bannedDependencies>
                  <excludes>
                    <!-- Ban old/insecure versions -->
                    <exclude>commons-collections:commons-collections:[,3.2.1]</exclude>
                    <exclude>org.apache.struts:struts2-core:[,2.3.37)</exclude>
                    <exclude>org.springframework:spring-core:[,4.3.18)</exclude>
                    <!-- Ban problematic logging implementations -->
                    <exclude>log4j:log4j:[,1.2.17]</exclude>
                    <exclude>org.apache.logging.log4j:log4j-core:[2.0,2.17.0)</exclude>
                  </excludes>
                </bannedDependencies>

                <!-- Require secure repositories -->
                <requireProperty>
                  <property>project.build.sourceEncoding</property>
                  <message>Source encoding must be specified for security</message>
                </requireProperty>
              </rules>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

  <!-- Secure repository configuration -->
  <repositories>
    <repository>
      <id>central</id>
      <url>https://repo1.maven.org/maven2</url>
      <releases>
        <enabled>true</enabled>
        <checksumPolicy>fail</checksumPolicy>
      </releases>
      <snapshots>
        <enabled>false</enabled>
      </snapshots>
    </repository>
  </repositories>

  <!-- Security profile for enhanced checks -->
  <profiles>
    <profile>
      <id>security-scan</id>
      <build>
        <plugins>
          <plugin>
            <groupId>org.owasp</groupId>
            <artifactId>dependency-check-maven</artifactId>
            <executions>
              <execution>
                <id>security-scan-detailed</id>
                <goals>
                  <goal>check</goal>
                </goals>
                <configuration>
                  <failBuildOnCVSS>0.0</failBuildOnCVSS>
                  <formats>
                    <format>ALL</format>
                  </formats>
                </configuration>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
</project>
<!-- Usage: mvn clean verify -P security-scan -->

```

**Bad example:**

```xml
<!-- No security scanning or vulnerability management -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>insecure-app</artifactId>
  <version>1.0.0</version>

  <dependencies>
    <!-- Using old, vulnerable versions -->
    <dependency>
      <groupId>commons-collections</groupId>
      <artifactId>commons-collections</artifactId>
      <version>3.2</version> <!-- Known security vulnerability -->
    </dependency>
    <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-core</artifactId>
      <version>2.14.1</version> <!-- Vulnerable to Log4Shell -->
    </dependency>
  </dependencies>

  <!-- Insecure repository without checksum validation -->
  <repositories>
    <repository>
      <id>unsecure-repo</id>
      <url>http://insecure.example.com/maven2</url> <!-- HTTP instead of HTTPS -->
      <releases>
        <checksumPolicy>ignore</checksumPolicy> <!-- No checksum validation -->
      </releases>
    </repository>
  </repositories>
</project>

```

### Example 9: Build Performance Optimization

Title: Optimize Build Speed with Parallel Execution and JVM Tuning
Description: Optimize Maven build performance through parallel execution, JVM tuning, incremental compilation, and build caching. Configure plugins for parallel test execution and optimize memory usage for faster builds, especially important in CI/CD environments.

**Good example:**

```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>optimized-build</artifactId>
  <version>1.0.0</version>

  <properties>
    <java.version>25</java.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

    <!-- Plugin versions -->
    <maven-plugin-compiler.version>3.14.0</maven-plugin-compiler.version>
    <maven-plugin-surefire.version>3.5.3</maven-plugin-surefire.version>
    <maven-plugin-failsafe.version>3.5.3</maven-plugin-failsafe.version>

    <!-- Performance tuning properties -->
    <parallel.test.threads>4</parallel.test.threads>
    <surefire.reuseForks>true</surefire.reuseForks>
  </properties>

  <build>
    <plugins>
      <!-- Optimized Compiler Plugin -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>${maven-plugin-compiler.version}</version>
        <configuration>
          <release>${java.version}</release>
          <useIncrementalCompilation>false</useIncrementalCompilation>
          <compilerArgs>
            <arg>-Xlint:all</arg>
            <arg>-parameters</arg>
          </compilerArgs>
        </configuration>
      </plugin>

      <!-- Optimized Surefire Plugin for Unit Tests -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>${maven-plugin-surefire.version}</version>
        <configuration>
          <!-- Parallel test execution -->
          <parallel>all</parallel>
          <threadCount>${parallel.test.threads}</threadCount>
          <perCoreThreadCount>true</perCoreThreadCount>

          <!-- JVM optimization -->
          <forkCount>1</forkCount>
          <reuseForks>${surefire.reuseForks}</reuseForks>
          <argLine>
            -Xmx1024m
            -XX:+UseG1GC
            -XX:+UseStringDeduplication
            -XX:TieredStopAtLevel=1
            -XX:-TieredCompilation
          </argLine>

          <!-- Test output optimization -->
          <trimStackTrace>false</trimStackTrace>
          <runOrder>filesystem</runOrder>
        </configuration>
      </plugin>

      <!-- Optimized Failsafe Plugin for Integration Tests -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-failsafe-plugin</artifactId>
        <version>${maven-plugin-failsafe.version}</version>
        <configuration>
          <parallel>classes</parallel>
          <threadCount>2</threadCount>
          <forkCount>1</forkCount>
          <reuseForks>true</reuseForks>
          <argLine>
            -Xmx2048m
            -XX:+UseG1GC
            -XX:+UseStringDeduplication
          </argLine>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>integration-test</goal>
              <goal>verify</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

  <!-- Performance optimization profiles -->
  <profiles>
    <profile>
      <id>fast-build</id>
      <properties>
        <maven.test.skip>true</maven.test.skip>
        <maven.javadoc.skip>true</maven.javadoc.skip>
        <checkstyle.skip>true</checkstyle.skip>
        <spotbugs.skip>true</spotbugs.skip>
      </properties>
    </profile>

    <profile>
      <id>parallel-build</id>
      <properties>
        <parallel.test.threads>8</parallel.test.threads>
      </properties>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
              <parallel>all</parallel>
              <threadCount>8</threadCount>
              <forkCount>2</forkCount>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
</project>

<!-- Create .mvn/jvm.config file with: -->
<!-- -Xmx4g -->
<!-- -XX:+UseG1GC -->
<!-- -XX:+UseStringDeduplication -->
<!-- -XX:TieredStopAtLevel=1 -->
<!-- -XX:-TieredCompilation -->

<!-- Usage: -->
<!-- mvn clean install -T 4 (parallel module builds) -->
<!-- mvn clean install -P fast-build (skip non-essential tasks) -->
<!-- mvn clean install -P parallel-build (maximum parallelization) -->

```

**Bad example:**

```xml
<!-- Non-optimized build configuration -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>slow-build</artifactId>
  <version>1.0.0</version>

  <build>
    <plugins>
      <!-- Basic compiler without optimization -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.0</version> <!-- Outdated version -->
        <configuration>
          <source>11</source> <!-- Hardcoded, old Java version -->
          <target>11</target>
        </configuration>
      </plugin>

      <!-- Surefire without parallel execution -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.22.2</version> <!-- Very outdated -->
        <configuration>
          <!-- No parallel execution -->
          <!-- No JVM optimization -->
          <!-- No fork reuse -->
          <forkCount>0</forkCount> <!-- Forces single JVM, slower -->
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>

```

### Example 10: Dependency Analysis and Cleanup

Title: Analyze and Optimize Dependency Usage
Description: Use Maven's dependency analysis tools to identify unused dependencies, detect version conflicts, and optimize the dependency tree. Regular dependency cleanup reduces build size, improves security, and eliminates potential conflicts.

**Good example:**

```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>clean-dependencies</artifactId>
  <version>1.0.0</version>

  <properties>
    <java.version>25</java.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

    <!-- Plugin versions -->
    <maven-plugin-dependency.version>3.8.1</maven-plugin-dependency.version>
    <maven-plugin-enforcer.version>3.5.0</maven-plugin-enforcer.version>
    <extra-enforcer-rules.version>1.8.0</extra-enforcer-rules.version>
  </properties>

  <build>
    <plugins>
      <!-- Maven Dependency Plugin for Analysis -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <version>${maven-plugin-dependency.version}</version>
        <executions>
          <!-- Analyze dependencies for unused/undeclared -->
          <execution>
            <id>analyze-dependencies</id>
            <goals>
              <goal>analyze-only</goal>
            </goals>
            <configuration>
              <failOnWarning>true</failOnWarning>
              <ignoreNonCompile>true</ignoreNonCompile>
              <outputXML>true</outputXML>
            </configuration>
          </execution>

          <!-- Generate dependency tree -->
          <execution>
            <id>dependency-tree</id>
            <phase>validate</phase>
            <goals>
              <goal>tree</goal>
            </goals>
            <configuration>
              <outputFile>target/dependency-tree.txt</outputFile>
            </configuration>
          </execution>
        </executions>
      </plugin>

      <!-- Enforcer Plugin with Dependency Rules -->
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
            <id>enforce-dependency-rules</id>
            <goals>
              <goal>enforce</goal>
            </goals>
            <configuration>
              <rules>
                <!-- Prevent circular dependencies -->
                <banCircularDependencies/>

                <!-- Ensure dependency convergence -->
                <dependencyConvergence/>

                <!-- Ban duplicate dependency versions -->
                <banDuplicatePomDependencyVersions/>

                <!-- Ban transitive dependencies -->
                <banTransitiveDependencies>
                  <excludes>
                    <!-- Allow common transitive dependencies -->
                    <exclude>org.slf4j:*</exclude>
                    <exclude>org.springframework:*</exclude>
                  </excludes>
                </banTransitiveDependencies>

                <!-- Require explicit dependency declarations -->
                <requireUpperBoundDeps/>
              </rules>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

  <!-- Dependency analysis profiles -->
  <profiles>
    <profile>
      <id>dependency-analysis</id>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-dependency-plugin</artifactId>
            <executions>
              <!-- Copy dependencies for analysis -->
              <execution>
                <id>copy-dependencies</id>
                <phase>package</phase>
                <goals>
                  <goal>copy-dependencies</goal>
                </goals>
                <configuration>
                  <outputDirectory>target/dependencies</outputDirectory>
                  <includeScope>compile</includeScope>
                </configuration>
              </execution>

              <!-- List all dependencies -->
              <execution>
                <id>list-dependencies</id>
                <phase>validate</phase>
                <goals>
                  <goal>list</goal>
                </goals>
                <configuration>
                  <outputFile>target/dependency-list.txt</outputFile>
                  <sort>true</sort>
                </configuration>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
</project>

<!-- Dependency Analysis Commands: -->
<!-- mvn dependency:analyze - Find unused/undeclared dependencies -->
<!-- mvn dependency:tree - Show dependency tree -->
<!-- mvn dependency:resolve-sources - Download source JARs -->
<!-- mvn clean verify -P dependency-analysis - Full dependency analysis -->

```

**Bad example:**

```xml
<!-- No dependency analysis or cleanup -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>messy-dependencies</artifactId>
  <version>1.0.0</version>

  <dependencies>
    <!-- Unused dependency -->
    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-lang3</artifactId>
      <version>3.12.0</version>
    </dependency>

    <!-- Different versions of same library -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.15.2</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>2.14.1</version> <!-- Different version, potential conflict -->
    </dependency>

    <!-- Missing explicit dependency for used classes -->
    <!-- Using classes from transitive dependencies without declaring them -->
  </dependencies>

  <!-- No dependency analysis plugins -->
  <!-- No enforcer rules -->
  <!-- No dependency management -->
</project>

```


### Example 11: Modern Java Features Configuration

Title: Configure Maven for Java 21+ Features and Module System
Description: Configure Maven to leverage modern Java features including preview features, the module system (JPMS), and modern JVM arguments. Properly configure the compiler and runtime for Java 21+ features while maintaining backward compatibility and build reproducibility.

**Good example:**

```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>modern-java-app</artifactId>
  <version>1.0.0</version>

  <properties>
    <!-- Modern Java configuration -->
    <java.version>25</java.version>
    <maven.compiler.release>${java.version}</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>

    <!-- Plugin versions -->
    <maven-plugin-compiler.version>3.14.0</maven-plugin-compiler.version>
    <maven-plugin-surefire.version>3.5.3</maven-plugin-surefire.version>
    <maven-plugin-failsafe.version>3.5.3</maven-plugin-failsafe.version>

    <!-- Modern Java features -->
    <enable.preview.features>false</enable.preview.features>
    <enable.modules>true</enable.modules>
  </properties>

  <dependencies>
    <!-- Modern testing with JUnit 5 -->
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>5.11.3</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <!-- Modern Maven Compiler Plugin Configuration -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>${maven-plugin-compiler.version}</version>
        <configuration>
          <release>${java.version}</release>
          <compilerArgs>
            <!-- Enable all warnings -->
            <arg>-Xlint:all</arg>
            <!-- Include parameter names in compiled bytecode -->
            <arg>-parameters</arg>
            <!-- Modern compiler optimizations -->
            <arg>-Xmaxerrs</arg>
            <arg>1000</arg>
            <arg>-Xmaxwarns</arg>
            <arg>1000</arg>
          </compilerArgs>
          <!-- Enable preview features conditionally -->
          <compilerArgs>
            <arg>${enable.preview.features}</arg>
          </compilerArgs>
        </configuration>
      </plugin>

      <!-- Surefire Plugin with Modern JVM Arguments -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>${maven-plugin-surefire.version}</version>
        <configuration>
          <argLine>
            <!-- Modern GC and memory management -->
            -XX:+UseZGC
            -XX:+UnlockExperimentalVMOptions
            -Xmx2g
            <!-- Enable preview features in tests if needed -->
            ${enable.preview.features}
            <!-- Modern JVM monitoring -->
            -XX:+FlightRecorder
            -XX:StartFlightRecording=duration=60s,filename=target/test-recording.jfr
          </argLine>
          <!-- Modern test execution -->
          <parallel>all</parallel>
          <threadCount>4</threadCount>
          <forkCount>1</forkCount>
          <reuseForks>true</reuseForks>
        </configuration>
      </plugin>

      <!-- Failsafe Plugin for Integration Tests -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-failsafe-plugin</artifactId>
        <version>${maven-plugin-failsafe.version}</version>
        <configuration>
          <argLine>
            -XX:+UseZGC
            -Xmx4g
            ${enable.preview.features}
          </argLine>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>integration-test</goal>
              <goal>verify</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

  <!-- Profiles for different Java feature sets -->
  <profiles>
    <!-- Profile for enabling preview features -->
    <profile>
      <id>preview-features</id>
      <properties>
        <enable.preview.features>--enable-preview</enable.preview.features>
      </properties>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
              <compilerArgs combine.children="append">
                <arg>--enable-preview</arg>
              </compilerArgs>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>

    <!-- Profile for module system development -->
    <profile>
      <id>modules</id>
      <activation>
        <property>
          <name>enable.modules</name>
          <value>true</value>
        </property>
      </activation>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
              <compilerArgs combine.children="append">
                <!-- Module path instead of classpath -->
                <arg>--module-path</arg>
                <arg>${project.build.directory}/modules</arg>
              </compilerArgs>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>

    <!-- Profile for different JVM implementations -->
    <profile>
      <id>graalvm</id>
      <properties>
        <graalvm.version>21.0.0</graalvm.version>
      </properties>
      <build>
        <plugins>
          <plugin>
            <groupId>org.graalvm.buildtools</groupId>
            <artifactId>native-maven-plugin</artifactId>
            <version>0.10.3</version>
            <extensions>true</extensions>
            <executions>
              <execution>
                <id>build-native</id>
                <goals>
                  <goal>compile-no-fork</goal>
                </goals>
                <phase>package</phase>
              </execution>
            </executions>
            <configuration>
              <imageName>${project.artifactId}</imageName>
              <buildArgs>
                <buildArg>--no-fallback</buildArg>
                <buildArg>--enable-preview</buildArg>
              </buildArgs>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
</project>

<!-- Usage Examples: -->
<!-- mvn clean compile (standard compilation) -->
<!-- mvn clean compile -P preview-features (with preview features) -->
<!-- mvn clean compile -P modules (with module system) -->
<!-- mvn clean package -P graalvm (native image compilation) -->

```

**Bad example:**

```xml
<!-- Outdated Java configuration -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>legacy-java-app</artifactId>
  <version>1.0.0</version>

  <properties>
    <!-- Old Java version -->
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
  </properties>

  <build>
    <plugins>
      <!-- Outdated compiler plugin -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.0</version> <!-- Very outdated -->
        <configuration>
          <source>11</source> <!-- Hardcoded, doesn't use properties -->
          <target>11</target>
          <!-- No modern compiler arguments -->
          <!-- No preview features support -->
          <!-- No module system support -->
        </configuration>
      </plugin>

      <!-- Basic Surefire without modern JVM settings -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.22.2</version> <!-- Very outdated -->
        <configuration>
          <!-- No modern GC settings -->
          <!-- No JFR recording -->
          <!-- No parallel execution -->
        </configuration>
      </plugin>
    </plugins>
  </build>

  <!-- No profiles for different Java feature sets -->
  <!-- No modern JVM optimization -->
  <!-- No support for preview features -->
</project>

```

## Output Format

- **ANALYZE** Maven POM files to identify specific best practices violations and categorize them by impact (CRITICAL, MAINTENANCE, PERFORMANCE, STRUCTURE) and area (dependency management, plugin configuration, project structure, repository management, version control)
- **CATEGORIZE** Maven configuration improvements found: Dependency Management Issues (missing dependencyManagement vs centralized version control, hardcoded versions vs property-based management, version conflicts vs resolution strategies, unused dependencies vs clean dependency trees), Plugin Configuration Problems (outdated versions vs current releases, missing configurations vs optimal settings, suboptimal configurations vs performance-tuned setups), Project Structure Opportunities (non-standard layouts vs Maven conventions, poor POM organization vs structured sections, missing properties vs centralized configuration), Security Vulnerabilities (missing OWASP dependency scanning, insecure repository configurations, vulnerable dependency versions, missing enforcer security rules), Performance Bottlenecks (sequential builds vs parallel execution, unoptimized JVM settings, missing build caching, inefficient test configurations)
- **APPLY** Maven best practices directly by implementing the most appropriate improvements for each identified issue: Introduce dependencyManagement sections for version centralization, extract version properties for consistency, configure essential plugins with optimal settings, organize POM sections following Maven conventions, add missing repository declarations, optimize dependency scopes, eliminate unused dependencies through analysis, implement OWASP security scanning, configure parallel build execution, and optimize JVM settings for performance
- **IMPLEMENT** comprehensive Maven configuration optimization using proven patterns: Establish centralized dependency management through BOMs or parent POMs, standardize plugin versions and configurations across modules, organize POM structure with clear sections (properties, dependencyManagement, dependencies, build), implement security best practices with OWASP dependency scanning and enforcer rules, apply dependency scope optimization, integrate build lifecycle enhancements, configure parallel test execution, and establish performance monitoring
- **REFACTOR** Maven configuration systematically following the improvement roadmap: First centralize version management through properties and dependencyManagement, then standardize plugin configurations with current versions, organize POM structure following Maven conventions, optimize dependency scopes and eliminate unused dependencies, secure repository configurations with HTTPS and checksum validation, implement vulnerability scanning, enhance build profiles for different environments, and optimize build performance through parallelization
- **EXPLAIN** the applied Maven improvements and their benefits: Build reliability enhancements through centralized dependency management, maintenance simplification via property-based versioning, performance improvements from optimized plugin configurations and parallel execution, security strengthening through OWASP vulnerability scanning and proper repository management, dependency cleanup benefits through automated analysis, and development productivity gains from standardized build practices and faster build times
- **VALIDATE** that all applied Maven changes compile successfully, maintain existing build behavior, preserve dependency compatibility, follow Maven best practices, and achieve the intended build improvements through comprehensive testing and verification

## Safeguards

- **MANDATORY**: Analyze existing POM configuration before making any changes
- **NEVER remove or replace existing plugins** - only add new plugins that don't already exist
- **NEVER remove or replace existing properties** - only add new properties that don't conflict
- **ASK USER before overriding** any existing configuration element
- **SECURITY**: Always validate security plugin configurations before applying OWASP dependency scanning
- **PERFORMANCE**: Test performance optimizations in non-production environments first
- Verify changes with the command: `./mvnw clean verify`
- Preserve existing dependency versions unless explicitly requested to update
- Maintain backward compatibility with existing build process
- **VALIDATE**: Run dependency analysis after changes to ensure no unused dependencies introduced
- **CAUTION**: Parallel build settings should be tested with project-specific test suites