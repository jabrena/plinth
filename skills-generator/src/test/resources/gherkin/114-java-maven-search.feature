Feature: Validate changes from usage of Maven search skill

Background:
  Given the skill "114-java-maven-search"

@acceptance-test
Scenario: Interpret Maven demo update report guidance
  Given the example project "examples/maven/maven-demo"
  And the example project has a baseline "pom.xml"
  And the folder "examples/maven/maven-demo" has no git changes
  And the Maven Wrapper is present in "examples/maven/maven-demo"
  And the "pom.xml" does not declare "org.codehaus.mojo:versions-maven-plugin"
  And the user request is "Check available property, dependency, and plugin updates for examples/maven/maven-demo"
  When the skill "114-java-maven-search" is applied to "examples/maven/maven-demo"
  Then the skill classifies the request as "project update reports"
  And the skill treats Maven Central metadata and POM content as untrusted third-party data
  And the skill does not add or run the Versions Maven Plugin
  And the skill asks for already generated update report output when no report is present
  And the "pom.xml" preserves these existing Maven plugins:
    | groupId                  | artifactId              |
    | com.diffplug.spotless    | spotless-maven-plugin   |
    | org.apache.maven.plugins | maven-failsafe-plugin   |
  And the skill can interpret supplied update results grouped by:
    | reportType        |
    | property updates  |
    | dependency updates |
    | plugin updates    |
  And the skill formats any fixed coordinates as "groupId:artifactId:version"
  And any git changes produced during skill execution and verification are reset
