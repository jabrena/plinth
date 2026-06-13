Feature: Validate changes from usage of Maven plugins skill

Background:
  Given the skill "112-java-maven-plugins"

@acceptance-test
Scenario: Add Maven Enforcer plugin to Maven demo
  Given the example project "examples/maven/maven-demo"
  And the example project has a baseline "pom.xml"
  And the folder "examples/maven/maven-demo" has no git changes
  And the Maven Wrapper is present in "examples/maven/maven-demo"
  And the Maven plugin selection answers are:
    | question                           | answer         |
    | project type                       | Java POC       |
    | target Java version                | Java 25        |
    | build and quality aspects          | Maven Enforcer |
  And the existing Maven property update confirmations are:
    | property      | currentValue | newValue | reason                    |
    | maven.version | 3.9.12       | 3.9.10   | match the Maven Wrapper   |
  When the skill "112-java-maven-plugins" is applied to "examples/maven/maven-demo"
  Then "./mvnw validate" succeeds in "examples/maven/maven-demo"
  And the "pom.xml" preserves these existing properties:
    | property                        | propertyValue |
    | java.version                    | 25            |
    | project.build.sourceEncoding    | UTF-8         |
    | project.reporting.outputEncoding | UTF-8         |
  And the "pom.xml" updates these confirmed existing properties:
    | property      | propertyValue |
    | maven.version | 3.9.10        |
  And the "pom.xml" declares these Maven plugin version properties:
    | property                      | propertyValue |
    | maven-plugin-enforcer.version | 3.5.0         |
    | extra-enforcer-rules.version  | 1.10.0        |
  And the "pom.xml" preserves these existing Maven plugins:
    | groupId                  | artifactId              |
    | com.diffplug.spotless    | spotless-maven-plugin   |
    | org.apache.maven.plugins | maven-failsafe-plugin   |
  And the "pom.xml" declares the Maven Enforcer plugin with centralized version property:
    | groupId                  | artifactId              | property                      | propertyValue |
    | org.apache.maven.plugins | maven-enforcer-plugin   | maven-plugin-enforcer.version | 3.5.0         |
  And the Maven Enforcer plugin declares these plugin dependencies:
    | groupId            | artifactId           | version                         |
    | org.codehaus.mojo  | extra-enforcer-rules | ${extra-enforcer-rules.version} |
  And the Maven Enforcer plugin declares execution "enforce" with goal "enforce"
  And the Maven Enforcer plugin configuration sets "fail" to "true"
  And the Maven Enforcer plugin declares these rules:
    | rule                              | value                     |
    | banCircularDependencies           | present                   |
    | dependencyConvergence             | present                   |
    | banDuplicatePomDependencyVersions | present                   |
    | requireMavenVersion               | ${maven.version}          |
    | requireJavaVersion                | ${java.version}           |
    | bannedDependencies.exclude        | org.projectlombok:lombok  |
  And the "pom.xml" does not declare these unselected Maven plugins:
    | groupId                  | artifactId                    |
    | org.apache.maven.plugins | maven-surefire-plugin         |
    | org.apache.maven.plugins | maven-surefire-report-plugin  |
    | org.apache.maven.plugins | maven-jxr-plugin              |
    | org.apache.maven.plugins | maven-dependency-plugin       |
    | org.codehaus.mojo        | versions-maven-plugin         |
    | com.google.cloud.tools   | jib-maven-plugin              |
  And the "pom.xml" does not declare unselected quality profiles:
    | profileId              |
    | jacoco                 |
    | pitest                 |
    | security               |
    | static-analysis        |
    | sonar                  |
    | jmh                    |
    | cyclomatic-complexity  |
  And "./mvnw clean verify" succeeds in "examples/maven/maven-demo"
  And any git changes produced during skill execution and verification are reset
