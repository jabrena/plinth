# User Story: US-001 — Greek Gods Wikipedia Information

This capability is also referred to as the **God Analysis API** in architecture decision records (ADRs).

**As a** user
**I want to** find out which Greek god has the most literature on Wikipedia
**So that** I can learn more about them

## Acceptance Criteria

See: [./US-001-Greek-Gods-Wikipedia-Information.feature](./US-001-Greek-Gods-Wikipedia-Information.feature)

## Related decisions

| Document | Scope |
|----------|--------|
| [ADR-001-God-Analysis-API-Functional-Requirements.md](./ADR-001-God-Analysis-API-Functional-Requirements.md) | Functional requirements, single REST endpoint, workflow, error behaviour |
| [ADR-002-God-Analysis-API-Non-Functional-Requirements.md](./ADR-002-God-Analysis-API-Non-Functional-Requirements.md) | Non-functional requirements (reliability, timeouts, parallel fan-out) |
| [ADR-003-God-Analysis-API-Technology-Stack.md](./ADR-003-God-Analysis-API-Technology-Stack.md) | Technology stack, outbound HTTP, testing (REST Assured, WireMock), package layout |

## Notes

- The list of Greek gods is loaded from the external REST API: `https://my-json-server.typicode.com/jabrena/latency-problems/greek`
- Wikipedia pages are fetched via: `https://en.wikipedia.org/wiki/{greekGod}`
- The amount of literature for a god is defined as the **character length** of its Wikipedia page content.
- If a Wikipedia page cannot be retrieved for a given god, its character count is assumed to be **0**.
- The result is a list of god name(s) with the highest character count, along with that count.
- If the Greek Gods API cannot be reached, our service responds with **HTTP 503** and an **empty** response body (see ADR-001 / feature).
- See the UML sequence diagram in `../sequence-diagram-latency-problem2.png` for the integration flow.
- See the OpenAPI specification in `../my-json-server-oas.yaml` for the Greek Gods REST API contract.
- Sample implementation package root: `info.jab.ms` (see [ADR-003](./ADR-003-God-Analysis-API-Technology-Stack.md)).
