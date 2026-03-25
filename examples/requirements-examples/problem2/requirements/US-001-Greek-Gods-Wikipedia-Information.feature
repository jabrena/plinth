Feature: Greek Gods Wikipedia Information
  As a user
  I want to find out which Greek god has the most literature on Wikipedia
  So that I can learn more about them

  Background:
    Given the following REST APIs:
      | API Name      | URL                                                              |
      | Greek Gods    | https://my-json-server.typicode.com/jabrena/latency-problems/greek |
      | Wikipedia     | https://en.wikipedia.org/wiki/{greekGod}                         |
    And the amount of literature for a god on Wikipedia is defined as the character length of its page content

  @integration-test
  Scenario: Successfully retrieve the list of Greek gods from the external Greek Gods API
    When the system calls the Greek Gods API (HTTP GET to the configured Greek Gods URL)
    Then the response body is a non-empty JSON array of Greek god names

  @acceptance-test
  Scenario: Identify the Greek god with the most literature on Wikipedia
    Given I have the list of Greek gods
    When I retrieve the Wikipedia page for each god
    And I calculate the character length of each god's Wikipedia page content, assuming a length of 0 if a page cannot be retrieved
    Then I should be presented with a list of god name(s) that have the most literature, along with their character count

  @acceptance-test
  Scenario: Identify the winner with known stub data
    Given the Greek Gods API returns the list: "Ares", "Zeus"
    And the Wikipedia page for "Ares" contains exactly 100 characters
    And the Wikipedia page for "Zeus" contains exactly 200 characters
    When I request GET /api/v1/gods/wikipedia/most-literature
    Then the response status is 200
    And the response body contains gods: ["Zeus"] with characterCount: 200

  @acceptance-test
  Scenario: Handle an unavailable Wikipedia page with zero character count
    Given the Greek Gods API returns the list: "Ares", "Zeus"
    And the Wikipedia page for "Ares" contains exactly 150 characters
    And the Wikipedia page for "Zeus" cannot be retrieved
    When I request GET /api/v1/gods/wikipedia/most-literature
    Then the response status is 200
    And the response body contains gods: ["Ares"] with characterCount: 150

  @acceptance-test
  Scenario: Handle a tie between multiple gods
    Given the Greek Gods API returns the list: "Ares", "Zeus", "Athena"
    And the Wikipedia page for "Ares" contains exactly 300 characters
    And the Wikipedia page for "Zeus" contains exactly 300 characters
    And the Wikipedia page for "Athena" contains exactly 200 characters
    When I request GET /api/v1/gods/wikipedia/most-literature
    Then the response status is 200
    And the response body contains gods: ["Ares", "Zeus"] with characterCount: 300

  @acceptance-test
  Scenario: Return HTTP 503 when the Greek Gods API is unavailable
    Given the Greek Gods API is unavailable
    When I request GET /api/v1/gods/wikipedia/most-literature
    Then the response status is 503
    And the response body is empty
