# Problem 1

## User Story Statement

- **As an** API consumer / data analyst
- **I want to** consume God APIs (Greek, Roman & Nordic), filter gods whose names start with 'n', convert each filtered god name into a decimal representation, and return the sum of those values
- **So that** I can perform cross-pantheon analysis and aggregate mythology data for research, reporting, or educational applications.

**Notes:**

- Decimal conversion: Each character in a god name is converted to its numeric code (e.g., ASCII/Unicode), and those values are summed per name. The final result is the sum of all such per-name sums.
- Case sensitivity: Filtering for gods starting with `n` is case-sensitive (only lowercase `n`), consistent with the Gherkin below.

## Gherkin file

```gherkin
Feature: God Analysis API
# REST API: GET /api/v1/gods/stats/sum
# Notes:
# - Decimal Conversion Rule: Name then each char to its Unicode int value, then concatenate these ints as strings.
# (e.g., "Zeus" -> Z(90)e(101)u(117)s(115) -> "90101117115").
# - Outbound HTTP uses Spring RestClient with connect/read timeouts from application configuration (defaults in application.yml).
# - If a source request hits the configured timeout, aggregation continues with the other sources (partial result).
# - Filtering for gods starting with 'n' is case-sensitive (only lowercase 'n').
# - Greek API: https://my-json-server.typicode.com/jabrena/latency-problems/greek
# - Roman API: https://my-json-server.typicode.com/jabrena/latency-problems/roman
# - Nordic API: https://my-json-server.typicode.com/jabrena/latency-problems/nordic

Background:
    Given the God Analysis API is available at "/api/v1"
    And the system is configured with HTTP connect and read timeouts for outbound RestClient calls (default 5 seconds in application configuration)

Scenario: Happy path - Get sum with explicit sources
    When the client sends a GET request to "/gods/stats/sum" with query parameters "filter" = "n" and "sources" = "greek,roman,nordic"
    Then the response status code should be 200
    And the response body should contain a JSON object with a "sum" field
    And the value of "sum" should be "78179288397447443426"
```
