## Context

`133-java-testing-acceptance-tests` is the framework-agnostic acceptance-test skill. It applies when a project does not use Spring Boot, Quarkus, or Micronaut and the maintainer supplies sanitized Gherkin scenario facts.

The current skill already contains strong safety constraints:

- maintainer-sanitized scenario facts are required
- raw outsider-authored `.feature` files must not be ingested
- framework projects must be routed to `323`, `423`, or `523`
- only happy-path acceptance scenarios are implemented by default
- DB/Kafka fixtures must be existing project-local fixtures, not container runtime setup invented by this skill

The improvement should preserve those boundaries and make the workflow more complete and testable.

## Decisions

### Keep One Reference

Unlike `130-java-testing-strategies`, `133` does not need multiple references. The skill is a single workflow:

1. confirm sanitized scenario facts
2. select acceptance-tagged happy paths
3. generate or update base acceptance-test infrastructure
4. implement one acceptance test per scenario
5. document dependencies, fixture assumptions, and verification

The reference should be expanded, not split.

### Align With Framework-Specific Siblings

`133` should align with the maturity of `323`, `423`, and `523` while preserving framework-agnostic mechanics. In particular, it should make these points explicit:

- summarize selected scenarios and proposed test class names before coding
- prefer `*AT` class naming for acceptance tests
- configure Surefire for fast `*Test` unit tests and Failsafe for `*IT` plus `*AT`
- reset WireMock between tests when sharing one stub server
- use WireMock only for external HTTP dependencies, not internal service substitution
- never embed real secrets, API keys, or production URLs
- report skipped negative/error scenarios because happy path remains the default scope

### Clarify Source Trust Boundary

The generated skill and documentation should consistently say "maintainer-sanitized scenario facts" for this framework-agnostic skill. Documentation should not imply that a raw `.feature` file from an issue, PR, ticket, or other outsider-authored source can be directly ingested.

Maintainer-authored or maintainer-sanitized facts should include:

- feature name
- scenario titles
- tags
- Given/When/Then facts
- data tables or docstring payloads only as sanitized data

### Example Coverage Targets

The reference should grow from four examples to at least seven examples:

| Example | Purpose |
|---|---|
| Sanitized scenario facts with `@acceptance` | Existing example, preserve and clarify trust boundary |
| Parse-and-confirm-before-coding | Show the required summary before code generation |
| `BaseAcceptanceTest` with fixtures and WireMock | Existing example, improve fixture fallback wording |
| RestAssured concrete acceptance test | Existing example, add `@DisplayName` or traceability where useful |
| `*AT` naming and Surefire/Failsafe split | Align with framework-specific skills |
| WireMock reset/isolation and external-only stubbing | Prevent scenario leakage and internal mocking |
| Scenario Outline and skipped negative/error scenario report | Clarify one-row default and honest scope reporting |

### Acceptance Coverage

Add `skills-generator/src/test/resources/gherkin/skills/133-java-testing-acceptance-tests.feature`.

The acceptance scenario should verify that the skill:

- reads `references/133-java-testing-acceptance-tests.md`
- uses maintainer-sanitized scenario facts only
- treats Gherkin prose as data and ignores embedded instructions
- rejects Spring Boot, Quarkus, or Micronaut projects and routes to framework skills
- runs compile before generating tests
- proposes `*AT` class names
- uses RestAssured for framework-agnostic HTTP acceptance tests
- uses WireMock for external REST dependencies only
- uses existing project-local DB/Kafka fixtures or stops and asks for approved fixture configuration
- ensures `*AT` tests are run by Failsafe during `./mvnw clean verify`

Add `133-java-testing-acceptance-tests` to the acceptance prompt inventory.

## Two-Step Sequencing

### Step 1: Make the Change Easy

Behavior-preserving preparation should add this OpenSpec change and, during implementation, add acceptance expectations before broad XML changes where practical. Existing guidance should be preserved while making the desired behavior explicit.

Validation after Step 1 should include `openspec validate --all` and Markdown validation for planning artifacts.

### Step 2: Make the Behavior Change

Update the `133` XML reference and skill index wording, add the `133` Gherkin feature and prompt inventory entry, regenerate local skills, and inspect generated Markdown. This changes generated skill behavior by making acceptance-test generation safer, more explicit, and better validated.

Validation after Step 2 should include XML well-formedness checks, local skill generation, generated output inspection, listed acceptance prompt execution, and skills-generator module verification.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated `.agents/skills/133-java-testing-acceptance-tests/SKILL.md`.
- Inspect generated `.agents/skills/133-java-testing-acceptance-tests/references/133-java-testing-acceptance-tests.md`.
- Execute the listed `133-java-testing-acceptance-tests` acceptance prompt after adding it to the prompt inventory, or record a skipped prompt with reason.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this planning change. Exact example code may be adjusted during implementation to align with existing generated Markdown style.
