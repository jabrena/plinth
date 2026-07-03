Feature: Validate changes from usage of WireMock technology skill

Background:
  Given the skill "702-technologies-wiremock"
  And the repository has no git changes

@acceptance-test
Scenario: Generate WireMock stubs and a demo test for the God Analysis API
  Given the God Analysis API story file "examples/openspec/god-analysis-api/requirements/problem1/US-001_God_Analysis_API.md"
  And the God Analysis API acceptance file "examples/openspec/god-analysis-api/requirements/problem1/US-001_god_analysis_api.feature"
  And the demo folder "examples/openspec/god-analysis-api/demo" has no git changes
  And the documented source APIs are:
    | source | endpoint                                                                | stub path                         | fixture path                                      |
    | greek  | https://my-json-server.typicode.com/jabrena/latency-problems/greek      | /jabrena/latency-problems/greek   | wiremock/__files/gods/greek-response.json        |
    | roman  | https://my-json-server.typicode.com/jabrena/latency-problems/roman      | /jabrena/latency-problems/roman   | wiremock/__files/gods/roman-response.json        |
    | nordic | https://my-json-server.typicode.com/jabrena/latency-problems/nordic     | /jabrena/latency-problems/nordic  | wiremock/__files/gods/nordic-response.json       |
  And the user request is "Create a WireMock-backed demo test in examples/openspec/god-analysis-api/demo for US-001. Stub the Greek, Roman, and Nordic APIs, use the stubs from a Java test, verify filter=N returns sum 78179288397447443426, and reset the generated demo changes after verification."
  And the local generated skill path ".agents/skills/702-technologies-wiremock"
  When the skill ".agents/skills/702-technologies-wiremock" is applied to the God Analysis API demo request
  Then the skill reads "references/702-technologies-wiremock.md"
  And the skill verifies the repository has no uncommitted changes before creating demo artifacts
  And the skill creates or proposes WireMock JSON mappings under "examples/openspec/god-analysis-api/demo/src/test/resources/wiremock/mappings/gods"
  And each mapping uses method "GET", the documented URL path, an "Accept" header matcher, status "200", "Content-Type: application/json", and "bodyFileName"
  And the skill creates or proposes response fixtures under "examples/openspec/god-analysis-api/demo/src/test/resources/wiremock/__files/gods"
  And the fixture data contains names needed by the documented "filter=N" happy path, including "Nike", "Nemesis", "Neptun", and "Njord"
  And the skill creates or proposes a Java test under "examples/openspec/god-analysis-api/demo/src/test/java"
  And the Java test starts WireMock on a dynamic port, loads the "wiremock" classpath files, and propagates runtime base URLs for the Greek, Roman, and Nordic sources
  And the Java test exercises the God Analysis API calculation through production code or the application HTTP endpoint and asserts the sum "78179288397447443426"
  And the Java test verifies one outbound GET request was made to each Greek, Roman, and Nordic stub
  And the Java test checks unmatched WireMock requests and resets WireMock state between tests
  And the skill runs the relevant Maven compile or test command for "examples/openspec/god-analysis-api/demo" after creating the demo test
  And the skill keeps the work at the WireMock stub-design, fixture, dynamic-port, verification, and demo-test layer
  And the skill avoids recommending "@SpringBootTest", "@QuarkusTest", "@MicronautTest", or "BaseIntegrationTest" bootstrapping unless the user explicitly asks for framework lifecycle wiring
  And the skill routes framework integration-test lifecycle work to "132-java-testing-integration-testing", "322-frameworks-spring-boot-testing-integration-tests", "422-frameworks-quarkus-testing-integration-tests", or "522-frameworks-micronaut-testing-integration-tests"
  And any git changes produced during skill execution and verification are reset
