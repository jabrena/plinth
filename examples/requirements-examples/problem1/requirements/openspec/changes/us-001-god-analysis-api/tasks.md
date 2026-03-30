# Tasks: US-001 God Analysis API

Derived from [US-001-plan-analysis.plan.md](../../../agile/US-001-plan-analysis.plan.md). Execute in order; after each **GREEN** implementation step, complete the associated **Verify** before proceeding.

## Execution rules (from plan)

1. Complete one task at a time; update **Status** in the source plan when tracking there.
2. **GREEN** tasks require a **Verify** milestone before the next implementation slice.
3. **Milestone** rows: complete refactor pair (logging → optimize) before each milestone Verify.

## Task list


| #   | Task                                                                           | Phase    | TDD  | Milestone | Group | Status |
| --- | ------------------------------------------------------------------------------ | -------- | ---- | --------- | ----- | ------ |
| 1   | Create Maven project structure with dependencies                               | Setup    |      |           | A1    | ☐      |
| 2   | Write acceptance test for happy path sum calculation                           | RED      | Test |           | A1    | ☐      |
| 3   | Create REST controller stub to pass acceptance test                            | GREEN    | Impl |           | A1    | ☐      |
| 4   | Add structured logging for request/response                                    | Refactor |      |           | A1    | ☐      |
| 5   | Optimize controller validation and error handling                              | Refactor |      |           | A1    | ☐      |
| 6   | Verify acceptance test passes with controller                                  | Verify   |      | milestone | A1    | ☐      |
| 7   | Write service layer unit test for aggregation logic                            | RED      | Test |           | A2    | ☐      |
| 8   | Implement service with Unicode algorithm                                       | GREEN    | Impl |           | A2    | ☐      |
| 9   | Add service-level logging                                                      | Refactor |      |           | A2    | ☐      |
| 10  | Optimize service configuration and error handling                              | Refactor |      |           | A2    | ☐      |
| 11  | Write integration test for filter=N zero result                                | RED      | Test |           | A2    | ☐      |
| 12  | Implement filter logic validation                                              | GREEN    | Impl |           | A2    | ☐      |
| 13  | Verify service unit tests pass                                                 | Verify   |      | milestone | A2    | ☐      |
| 14  | Write HTTP client tests for timeout-bound fetching (single attempt per source) | RED      | Test |           | A3    | ☐      |
| 15  | Implement HTTP client with configured RestClient timeouts (no retries)         | GREEN    | Impl |           | A3    | ☐      |
| 16  | Add client-level logging for HTTP outcomes                                     | Refactor |      |           | A3    | ☐      |
| 17  | Optimize client configuration (timeouts, per-source isolation)                 | Refactor |      |           | A3    | ☐      |
| 18  | Write integration test for Nordic/Roman timeout scenario (per feature file)    | RED      | Test |           | A3    | ☐      |
| 19  | Implement partial result handling in service                                   | GREEN    | Impl |           | A3    | ☐      |
| 20  | Verify HTTP client tests pass (timeout phase)                                  | Verify   |      | milestone | A3    | ☐      |
| 21  | Verify all integration tests pass                                              | Verify   |      | milestone | A4    | ☐      |


## File checklist (from plan)


| Order | File                                                                                        |
| ----- | ------------------------------------------------------------------------------------------- |
| 1     | `god-analysis-api/pom.xml`                                                                  |
| 2     | `god-analysis-api/src/main/resources/application.yml`                                       |
| 3     | `god-analysis-api/src/test/java/info/jab/ms/controller/GodAnalysisApiAT.java`               |
| 4     | `god-analysis-api/src/main/java/info/jab/ms/controller/GodStatsController.java`             |
| 5     | `god-analysis-api/src/main/java/info/jab/ms/dto/GodStatsResponse.java`                      |
| 6     | `god-analysis-api/src/test/java/info/jab/ms/service/GodAnalysisServiceTest.java`            |
| 7     | `god-analysis-api/src/main/java/info/jab/ms/service/GodAnalysisService.java`                |
| 8     | `god-analysis-api/src/main/java/info/jab/ms/algorithm/UnicodeAggregator.java`               |
| 9     | `god-analysis-api/src/test/java/info/jab/ms/client/GodDataClientTest.java`                  |
| 10    | `god-analysis-api/src/main/java/info/jab/ms/client/GodDataClient.java`                      |
| 11    | `god-analysis-api/src/main/java/info/jab/ms/config/HttpClientConfig.java`                   |
| 12    | `god-analysis-api/src/test/java/info/jab/ms/controller/GodAnalysisApiIT.java`               |
| 13    | `god-analysis-api/src/test/resources/wiremock/greek-gods.json`                              |
| 14    | `god-analysis-api/src/test/resources/wiremock/roman-gods.json`                              |
| 15    | `god-analysis-api/src/test/resources/wiremock/nordic-gods.json`                             |
| 16    | `god-analysis-api/src/test/java/info/jab/ms/support/WireMockExtension.java` (or equivalent) |
| 17    | `god-analysis-api/README.md`                                                                |


## Deliverables

- Module builds with `./mvnw clean verify`
- README documents run, env vars, WireMock/Testcontainers notes
- Acceptance scenarios aligned with [US-001_god_analysis_api.feature](../../../agile/US-001_god_analysis_api.feature)

