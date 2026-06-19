Feature: Validate changes from usage of skill

Background:
  Given the skill "110-java-maven-best-practices"

@acceptance-test
Scenario: Validate Maven skill changes against expected definition
  Given the example project "examples/maven/maven-demo"
  And the example project has a baseline "pom.xml"
  And the folder "examples/maven/maven-demo" has no git changes
  When the skill "110-java-maven-best-practices" is applied to "examples/maven/maven-demo"
  Then the "pom.xml" moves these dependency management versions to properties:
    | artifactId         | previousInlineVersion | property               | propertyValue |
    | junit-bom          | 5.11.0                | junit.version          | 5.11.0        |
    | testcontainers-bom | 1.19.8                | testcontainers.version | 1.19.8        |
  And the "pom.xml" declares these Maven plugins with centralized version properties:
    | groupId                  | artifactId             | property                      | propertyValue |
    | org.apache.maven.plugins | maven-compiler-plugin  | maven-plugin-compiler.version | 3.14.0        |
    | org.apache.maven.plugins | maven-surefire-plugin  | maven-plugin-surefire.version | 3.5.3         |
  And "./mvnw clean verify" succeeds in "examples/maven/maven-demo"
  And any git changes produced during skill execution and verification are reset

@acceptance-test
Scenario: Stop Maven skill when example project validation fails
  Given the example project "examples/maven/maven-demo-ko"
  And the example project has a baseline "pom.xml"
  And the folder "examples/maven/maven-demo-ko" has no git changes
  When the skill "110-java-maven-best-practices" is applied to "examples/maven/maven-demo-ko"
  Then "./mvnw validate" fails in "examples/maven/maven-demo-ko"
  And the skill reports the validation failure before applying recommendations
  And the "pom.xml" remains unchanged
  And the folder "examples/maven/maven-demo-ko" has no git changes

@acceptance-test
Scenario: Discover every child POM before Maven multi-module recommendations
  Given the example project "examples/maven/maven-multi-module-demo"
  And the folder "examples/maven/maven-multi-module-demo" has no git changes
  When the skill "110-java-maven-best-practices" is applied to "examples/maven/maven-multi-module-demo"
  Then "./mvnw -f examples/maven/maven-multi-module-demo/pom.xml validate" succeeds from the repository root
  And the skill reports these discovered Maven modules before recommendations:
    | module       | pomPath                                                        |
    | root         | examples/maven/maven-multi-module-demo/pom.xml                 |
    | demo-api     | examples/maven/maven-multi-module-demo/demo-api/pom.xml        |
    | demo-service | examples/maven/maven-multi-module-demo/demo-service/pom.xml    |
  And the skill reports the child issue "demo-api declares jackson-databind with a hardcoded version managed by the parent"
  And the skill reports the child issue "demo-service declares redundant child pluginManagement for maven-compiler-plugin"
  And recommendations are produced only after reading every discovered child "pom.xml"
  And "demo-api/pom.xml" no longer declares a version for "com.fasterxml.jackson.core:jackson-databind"
  And "demo-service/pom.xml" no longer declares child "pluginManagement"
  And "./mvnw -f examples/maven/maven-multi-module-demo/pom.xml clean verify" succeeds from the repository root
  And any git changes produced during skill execution and verification are reset
