Feature: Validate changes from usage of Maven search skill

Background:
  Given the skill "114-java-maven-search"

@acceptance-test
Scenario: Add Versions Maven Plugin and report Maven demo updates
  Given the example project "examples/maven/maven-demo"
  And the example project has a baseline "pom.xml"
  And the folder "examples/maven/maven-demo" has no git changes
  And the Maven Wrapper is present in "examples/maven/maven-demo"
  And the "pom.xml" does not declare "org.codehaus.mojo:versions-maven-plugin"
  And the user request is "Check available property, dependency, and plugin updates for examples/maven/maven-demo"
  When the skill "114-java-maven-search" is applied to "examples/maven/maven-demo"
  Then the skill classifies the request as "project update reports"
  And the skill verifies this Maven Central coordinate before editing "pom.xml":
    | groupId          | artifactId            | version | packaging    |
    | org.codehaus.mojo | versions-maven-plugin | 2.21.0  | maven-plugin |
  And the skill records these verifiable Maven Central URLs:
    | url                                                                                                              |
    | https://repo.maven.apache.org/maven2/org/codehaus/mojo/versions-maven-plugin/maven-metadata.xml                  |
    | https://repo.maven.apache.org/maven2/org/codehaus/mojo/versions-maven-plugin/2.21.0/versions-maven-plugin-2.21.0.pom |
  And the "pom.xml" declares the Versions Maven Plugin:
    | groupId          | artifactId            | version |
    | org.codehaus.mojo | versions-maven-plugin | 2.21.0  |
  And the "pom.xml" preserves these existing Maven plugins:
    | groupId                  | artifactId              |
    | com.diffplug.spotless    | spotless-maven-plugin   |
    | org.apache.maven.plugins | maven-failsafe-plugin   |
  And "./mvnw validate" succeeds in "examples/maven/maven-demo"
  And "./mvnw versions:display-property-updates" succeeds in "examples/maven/maven-demo"
  And "./mvnw versions:display-dependency-updates" succeeds in "examples/maven/maven-demo"
  And "./mvnw versions:display-plugin-updates" succeeds in "examples/maven/maven-demo"
  And the skill reports update results grouped by:
    | reportType        |
    | property updates  |
    | dependency updates |
    | plugin updates    |
  And the skill formats any fixed coordinates as "groupId:artifactId:version"
  And any git changes produced during skill execution and verification are reset
