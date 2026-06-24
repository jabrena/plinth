Feature: Validate changes from usage of Micronaut Kafka skill

Background:
  Given the skill "514-frameworks-micronaut-kafka"

@acceptance-test
Scenario: Add Micronaut Kafka integration testing with trusted Kafka container image configuration
  Given the example project "examples/frameworks/micronaut"
  And the user request is "Add a Micronaut Kafka integration test for a sum calculated event"
  And the project uses trusted CI configuration property "test.kafka.image" for Kafka Testcontainers images
  And the local generated skill path ".agents/skills/514-frameworks-micronaut-kafka"
  And the folder "examples/frameworks/micronaut" has no git changes
  When the skill ".agents/skills/514-frameworks-micronaut-kafka" is applied to the example project
  Then the skill reads "references/514-frameworks-micronaut-kafka.md"
  And "./mvnw compile" or "mvn compile" is run before applying Kafka changes
  And the skill adds or verifies "micronaut-kafka" dependency and annotation processor configuration using Micronaut dependency management
  And the skill models events as typed "@Serdeable" records
  And the skill uses "@KafkaClient" producers with explicit "@KafkaKey" values
  And the skill uses "@KafkaListener" consumers with versioned "groupId" and safe offset and error strategy
  And the skill wires Kafka integration tests with "@MicronautTest", "TestPropertyProvider", and "@TestInstance(Lifecycle.PER_CLASS)"
  And the skill resolves the Kafka Testcontainers image from trusted project or CI configuration, preferably pinned by digest
  And the skill does not hard-code public Docker Hub image tags such as "apache/kafka-native:3.8.0"
  And "./mvnw clean verify" or "mvn clean verify" is run after improvements
  And any git changes produced during skill execution and verification are reset
