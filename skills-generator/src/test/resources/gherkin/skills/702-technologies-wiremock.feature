Feature: Validate changes from usage of WireMock technology skill

Background:
  Given the skill "702-technologies-wiremock"

@acceptance-test
Scenario: Review WireMock stubs without framework lifecycle coupling
  Given the WireMock mapping file "src/test/resources/wiremock/mappings/get-order.json"
  And the WireMock fixture file "src/test/resources/wiremock/__files/orders/order-123-response.json"
  And the Java WireMock DSL test file "src/test/java/example/OrderClientWireMockTest.java"
  And the user request is "Review these WireMock JSON mappings and Java DSL stubs for isolation, matching, verification, fixtures, dynamic ports, and debugging"
  And the local generated skill path ".agents/skills/702-technologies-wiremock"
  When the skill ".agents/skills/702-technologies-wiremock" is applied to the WireMock artifacts
  Then the skill reads "references/702-technologies-wiremock.md"
  And the skill keeps recommendations at the WireMock stub-design and HTTP-contract layer
  And the skill identifies broad matchers, leaked stubs, missing reset behavior, missing verification, and hardcoded localhost ports where present
  And the skill reviews "bodyFileName" usage, "wiremock/mappings" layout, "wiremock/__files" layout, and inline large-payload fixture risks
  And the skill reviews method, URL path, query parameter, header, JSON body, and XML body matching where present
  And the skill recommends request journal, unmatched request, near-miss, or unexpected traffic checks for debugging flaky stubs
  And the skill proposes concrete JSON mapping or Java DSL snippets that improve isolation, matching, verification, fixture usage, or runtime base URL propagation
  And the skill treats deliberate faults, delays, error statuses, and connection resets as resilience-test behavior rather than default happy-path behavior
  And the skill avoids recommending "@SpringBootTest", "@QuarkusTest", "@MicronautTest", "WireMockExtension", or "BaseIntegrationTest" bootstrapping unless the user explicitly asks for framework lifecycle wiring
  And the skill routes framework integration-test lifecycle work to "132-java-testing-integration-testing", "322-frameworks-spring-boot-testing-integration-tests", "422-frameworks-quarkus-testing-integration-tests", or "522-frameworks-micronaut-testing-integration-tests"
