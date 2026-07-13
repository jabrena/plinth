## Context

Skill `123-java-design-patterns` already has a useful structure: one entry skill routes agents to focused references for classic Java, REST, Kafka/event-driven, database/persistence, and cross-cutting integration patterns. The main gap is not routing; it is coverage depth inside those references.

Current example coverage:

| Reference | Top-level examples | Coverage risk |
|---|---:|---|
| `123-java-design-patterns.xml` | 3 | Underrepresents creational, structural, and behavioral families |
| `123-rest-api-patterns.xml` | 3 | Covers basics but misses API evolution and concurrency patterns |
| `123-kafka-event-driven-patterns.xml` | 2 | Too thin for reliability, failure, ordering, and schema guidance |
| `123-database-persistence-patterns.xml` | 2 | Too thin for transactions, aggregates, migrations, and query behavior |
| `123-cross-cutting-integration-patterns.xml` | 2 | Too thin for observability, migration, and isolation patterns |

## Decisions

### Preserve the Existing Skill Boundary

Keep `123-java-design-patterns` as the umbrella skill for Java design and integration patterns. The existing reference split is appropriate and should be enriched rather than replaced.

### Add Decision Aids Before More Examples

Each reference should include a compact pattern selection matrix or equivalent guide. The matrix should map:

- design pressure
- candidate pattern
- use when
- avoid when
- validation signal

This makes the skill useful for reviews, where the right answer may be "no new pattern."

### Raise Example Coverage by Reference

The implementation should add examples to meet these minimum targets:

| Reference | Minimum examples after change | Recommended focus |
|---|---:|---|
| `123-java-design-patterns.xml` | 6 | Factory/static factory, adapter, state or command, plus current strategy/builder/decorator |
| `123-rest-api-patterns.xml` | 5 | Pagination/filtering, ETag or `If-Match`, DTO/versioning, plus current resource/idempotency/problem-details |
| `123-kafka-event-driven-patterns.xml` | 5 | Partition key selection, idempotent consumer/inbox, retry/DLT, schema evolution, plus current event-carried state/outbox |
| `123-database-persistence-patterns.xml` | 5 | Transaction boundary, aggregate boundary, migration/parallel change, N+1 avoidance, plus current repository/locking |
| `123-cross-cutting-integration-patterns.xml` | 5 | Correlation or trace propagation, bulkhead, strangler fig, fallback policy, plus current ACL/resilience policy |

Examples should prefer concise good/bad contrasts, request/response shapes, event shapes, SQL, or Java snippets depending on the pattern area.

### Preserve Problem-Led Pattern Guidance

New examples must continue the existing style:

- identify the design pressure before naming the pattern
- name simpler alternatives where a pattern is not justified
- include benefit, cost, and when-not-to-use guidance
- avoid pattern shopping and over-abstraction
- use modern Java features where Java code is shown

### Acceptance Coverage

The existing acceptance test focuses on REST review behavior. Add coverage for at least:

- Kafka/event-driven reliability review that requires reading `references/123-kafka-event-driven-patterns.md`
- database/persistence review that requires reading `references/123-database-persistence-patterns.md`

The acceptance prompt inventory must be kept in sync if the `123` Gherkin file changes.

## Two-Step Sequencing

### Step 1: Make the Change Easy

Behavior-preserving preparation should clarify the intended coverage targets and update tests or acceptance expectations before broad content additions. This may include documenting the example target table in the OpenSpec change and preparing focused Gherkin scenarios.

Validation after Step 1 should include OpenSpec validation and inspection that the new acceptance expectations still describe existing or intended skill behavior without editing generated output directly.

### Step 2: Make the Behavior Change

Add the new XML reference examples and decision aids, regenerate local skills, and verify generated Markdown. This changes the skill guidance behavior by giving agents more concrete examples and stronger selection support.

Validation after Step 2 should include XML well-formedness checks, local skill generation, generated output inspection, applicable acceptance prompt execution, and the plinth-skills-generator module verification.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl plinth-skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated `.agents/skills/123-java-design-patterns/SKILL.md` and generated `references/123-*.md`.
- Check `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` and run only the listed `123-java-design-patterns` acceptance prompt when the `123` Gherkin file or generated skill output changes.
- Run `./mvnw clean verify -pl plinth-skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this planning change. The exact examples may be adjusted during implementation if local source context reveals a simpler or more consistent example set.
