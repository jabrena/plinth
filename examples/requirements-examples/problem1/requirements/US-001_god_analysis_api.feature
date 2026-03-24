Feature: God Analysis API
  # REST API: GET /api/v1/gods/stats/sum
  # Notes:
  # - Decimal Conversion Rule: For each name, convert each char to its Unicode int value,
  #   then concatenate those ints as strings (e.g., "Zeus" -> Z(90)e(101)u(117)s(115) -> "90101117115").
  # - Filtering for gods starting with 'n' is case-sensitive (only lowercase 'n').
  # - If a source API call hits the configured RestClient timeout, the calculation proceeds with results from the remaining sources (single attempt per source; no retries).
  # - Greek API:  https://my-json-server.typicode.com/jabrena/latency-problems/greek
  # - Roman API:  https://my-json-server.typicode.com/jabrena/latency-problems/roman
  # - Nordic API: https://my-json-server.typicode.com/jabrena/latency-problems/nordic

  Background:
    Given the God Analysis API is available at "/api/v1"
    And the system is configured with HTTP connect and read timeouts for outbound RestClient calls (default 5 seconds in application configuration)

  @acceptance-test
  Scenario: Happy path - Get sum with all three sources filtered by lowercase 'n'
    When the client sends a GET request to "/gods/stats/sum" with query parameters "filter" = "n" and "sources" = "greek,roman,nordic"
    Then the response status code should be 200
    And the response body should contain a JSON object with a "sum" field
    And the value of "sum" should be "78179288397447443426"

  @integration-test
  Scenario: Partial result when multiple source APIs time out
    Given the Nordic API is configured to respond after the timeout threshold
    And the Roman API is configured to respond after the timeout threshold
    When the client sends a GET request to "/gods/stats/sum" with query parameters "filter" = "n" and "sources" = "greek,roman,nordic"
    Then the response status code should be 200
    And the response body should contain a JSON object with a "sum" field
    And the value of "sum" should be "78101109179220212216"

  @error-handling
  Scenario: Error when filter parameter is missing
    When the client sends a GET request to "/gods/stats/sum" with query parameters "sources" = "greek,roman,nordic"
    Then the response status code should be 400
    And the response body should contain an error message

  @error-handling
  Scenario: Error when sources parameter is missing
    When the client sends a GET request to "/gods/stats/sum" with query parameters "filter" = "n"
    Then the response status code should be 400
    And the response body should contain an error message

  @error-handling
  Scenario: Error when filter parameter is empty
    When the client sends a GET request to "/gods/stats/sum" with query parameters "filter" = "" and "sources" = "greek,roman,nordic"
    Then the response status code should be 400
    And the response body should contain an error message

  @error-handling
  Scenario: Error when filter parameter has multiple characters
    When the client sends a GET request to "/gods/stats/sum" with query parameters "filter" = "abc" and "sources" = "greek,roman,nordic"
    Then the response status code should be 400
    And the response body should contain an error message

  @error-handling
  Scenario: Error when sources parameter contains invalid source names
    When the client sends a GET request to "/gods/stats/sum" with query parameters "filter" = "n" and "sources" = "invalid,unknown"
    Then the response status code should be 400
    And the response body should contain an error message

  @error-handling
  Scenario: Error when sources parameter is empty
    When the client sends a GET request to "/gods/stats/sum" with query parameters "filter" = "n" and "sources" = ""
    Then the response status code should be 400
    And the response body should contain an error message

