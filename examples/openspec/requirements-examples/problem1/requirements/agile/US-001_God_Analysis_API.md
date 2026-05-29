# User Story: US-001 God Analysis API

**As a** API consumer / data analyst
**I want to** consume God APIs (Greek, Roman & Nordic), filter gods whose names start with 'n', convert each filtered god name into its decimal representation, and return the sum of those values
**So that** I can perform cross-pantheon analysis and aggregate mythology data for research, reporting, or educational applications

## Acceptance Criteria

See: [US-001_god_analysis_api.feature](US-001_god_analysis_api.feature)

## Notes

- **Decimal Conversion Rule:** For each god name, each character is converted to its Unicode integer value and those integers are concatenated as strings (e.g., "Zeus" → Z(90)e(101)u(117)s(115) → "90101117115"). The final result is the numeric sum of all per-name string representations.
- **Case sensitivity:** Filtering for gods starting with 'n' is case-sensitive (only lowercase 'n').
- **HTTP timeouts:** Outbound calls use Spring `RestClient` with connect/read timeouts set once in configuration (defaults in `application.yml`; optional environment variable overrides). There is **no** automatic retry of failed or timed-out requests—scope stays limited to sensible client timeouts and partial aggregation when a source does not return in time.
- **Configuration:** Single default configuration with environment variable overrides for operational flexibility.
- **Data sources:**
  - Greek API: https://my-json-server.typicode.com/jabrena/latency-problems/greek
  - Roman API: https://my-json-server.typicode.com/jabrena/latency-problems/roman
  - Nordic API: https://my-json-server.typicode.com/jabrena/latency-problems/nordic
