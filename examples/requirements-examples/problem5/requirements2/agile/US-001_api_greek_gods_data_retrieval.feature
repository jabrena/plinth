Feature: API Greek Gods Data Retrieval
  As an API consumer/developer
  I want to retrieve a complete list of Greek god names through a REST API endpoint
  So that I can integrate mythology content into my educational application with fast, reliable access to curated deity information

  Background:
    Given the Greek gods data synchronization service is running
    And the PostgreSQL database contains synchronized Greek god data
    And the REST API service is available and running

  @smoke @happy-path
  Scenario: Successfully retrieve complete list of Greek god names
    Given the database contains all 20 Greek god records
    When I send a GET request to "/api/v1/gods/greek"
    Then I should receive a 200 OK response
    And the response should be a JSON array
    And the response should contain exactly 20 Greek god names
    And the response should include the following god names:
      """
      [
        "Zeus",
        "Hera", 
        "Poseidon",
        "Demeter",
        "Ares",
        "Athena",
        "Apollo",
        "Artemis",
        "Hephaestus",
        "Aphrodite",
        "Hermes",
        "Dionysus",
        "Hades",
        "Hypnos",
        "Nike",
        "Janus",
        "Nemesis",
        "Iris",
        "Hecate",
        "Tyche"
      ]
      """
    And each god name should be a string value
    And there should be no duplicate entries in the response

  @performance
  Scenario: API response time meets performance requirements
    Given the database contains Greek god data
    When I send a GET request to "/api/v1/gods/greek"
    Then I should receive a response within 1 second
    And the response status should be 200 OK

  @performance @load-testing
  Scenario: API handles concurrent requests successfully
    Given the database contains Greek god data
    When I send 100 concurrent GET requests to "/api/v1/gods/greek"
    Then all requests should receive a 200 OK response
    And each response should contain the complete list of 20 Greek god names
    And all responses should be received within 2 seconds total

  @error-handling
  Scenario: Handle database connection failure gracefully
    Given the database connection is unavailable
    When I send a GET request to "/api/v1/gods/greek"
    Then I should receive a 500 Internal Server Error response
    And the response Content-Type should include "application/problem+json"
    And the response body should be a JSON object
    And the Problem Detail should include string property "type" with a non-empty value
    And the Problem Detail should include string property "title" with a non-empty value
    And the Problem Detail should include integer property "status" with value 500

  @error-handling
  Scenario: Handle empty database gracefully
    Given the database is available but contains no Greek god data
    When I send a GET request to "/api/v1/gods/greek"
    Then I should receive a 200 OK response
    And the response should be a JSON array
    And the response should be an empty array "[]"

  @data-quality
  Scenario: Verify data consistency with synchronized database
    Given the external data source contains updated Greek god information
    And the background synchronization service has completed successfully
    When I send a GET request to "/api/v1/gods/greek"
    Then I should receive a 200 OK response
    And the response should contain the most recently synchronized data
    And all god names should match the external data source format

  @api-specification
  Scenario: Validate response format matches OpenAPI specification
    Given the API is documented with OpenAPI 3.0.3 specification
    When I send a GET request to "/api/v1/gods/greek"
    Then I should receive a 200 OK response
    And the response Content-Type should be "application/json"
    And the response format should match the OpenAPI specification
    And the response should be a JSON array of strings

  @availability
  Scenario: API maintains high availability
    Given the API service has been running for 24 hours
    When I check the API availability metrics
    Then the uptime should be at least 99.9%
    And the error rate should be less than 0.1% 