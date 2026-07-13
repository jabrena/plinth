Feature: Validate changes from usage of Maven search skill

Background:
  Given the skill "114-java-maven-search"

@acceptance-test
Scenario: Route Maven demo update requests to Versions Maven Plugin report guidance
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
  And the skill refers plugin installation or configuration to "112-java-maven-plugins"
  And the skill points version management setup to "references/112-java-maven-plugins-versions-maven-plugin.md"
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

@acceptance-test
Scenario: Route explicit artifact discovery requests to Maven Central search guidance
  Given the user request is "Search Maven Central for org.springframework.boot:spring-boot and provide artifact URLs"
  When the skill "114-java-maven-search" is applied to the request
  Then the skill classifies the request as "Maven Central artifact discovery"
  And the skill reads "references/114-maven-central-search.md"
  And the skill does not require project-local Versions Maven Plugin report output
  And the skill verifies coordinates through structured Search API fields or repository URL checks
  And the skill does not ingest raw remote POM, maven-metadata.xml, artifact descriptions, or repository HTML into prompt context
  And the skill formats fixed coordinates as "groupId:artifactId:version"
  And the skill can construct verifiable HTTPS links for:
    | artifactType |
    | POM          |
    | JAR          |
    | sources      |
    | Javadoc      |
