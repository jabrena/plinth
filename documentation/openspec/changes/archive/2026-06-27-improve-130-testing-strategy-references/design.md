## Context

`130-java-testing-strategies` is intended to answer strategy-level questions:

- RIGHT-BICEP: what behavior and risks should be tested?
- A-TRIP: what makes tests high quality, reliable, and maintainable?
- CORRECT: which boundary conditions are easy to miss?

The current reference file covers all three techniques together. It includes one example for RIGHT-BICEP, one for A-TRIP, and one for CORRECT. That is enough for a primer, but not enough for applied Java test review or improvement work.

## Decisions

### Preserve the `130` Skill Identifier

Keep `130-java-testing-strategies` as the public skill id. The change improves internal reference structure and selection behavior, not the skill's identity.

### Split References by Technique

Create three focused references:

| Reference | Primary question | Trigger signal |
|---|---|---|
| `130-java-testing-strategies-right-bicep` | What should we test? | Missing behavior coverage, weak oracle, untested errors, missing cross-checks |
| `130-java-testing-strategies-a-trip` | Are these tests good tests? | Flakiness, manual setup, order dependency, shared state, unclear assertions |
| `130-java-testing-strategies-correct` | Which boundaries are missing? | Boundary-condition review, invalid input handling, cardinality/time/order concerns |

The generated `SKILL.md` should instruct agents to read only the matching reference for narrow requests and to combine references only for broad test-strategy reviews.

### Technique Coverage Targets

Each reference should explain all cases in that technique, not only list them.

#### RIGHT-BICEP

The RIGHT-BICEP reference should cover:

- Right results and oracle clarity
- Boundary conditions as a bridge to CORRECT
- Inverse relationships such as serialize/deserialize, encode/decode, add/remove, or write/read
- Cross-checks using independent calculation, alternate implementation, fixture, or property
- Error conditions and exception contracts
- Performance guardrails as bounded smoke checks, not fragile microbenchmarks

#### A-TRIP

The A-TRIP reference should cover:

- Automatic tests that run in normal build or focused test commands
- Thorough tests that cover meaningful behavior without duplicating implementation
- Repeatable tests with fixed `Clock`, fixed random seed, isolated temp files, and deterministic data
- Independent tests without shared mutable state or order assumptions
- Professional tests with descriptive names, clear assertions, maintainable fixtures, and no noise

#### CORRECT

The CORRECT reference should cover:

- Conformance to format, schema, type, or validation rules
- Ordering for sorted, stable, or explicitly unordered results
- Range, including lower bound, upper bound, just-inside, and just-outside values
- Reference/external dependency conditions, including missing records, unavailable services, permissions, or filesystem paths
- Existence, including null, blank, empty, missing optional, and absent repository values
- Cardinality, including zero, one, many, exact count, maximum count, and duplicates
- Time, using `Clock` or equivalent controllable time source

### Relationship to `131-java-testing-unit-testing`

`130` should decide what to test and how to reason about test quality. `131` should continue to own detailed JUnit 5, AssertJ, Mockito, parameterized test, and unit-test implementation mechanics. The `130` references may show concise Java examples, but they should not become a duplicate unit-testing framework catalog.

### Acceptance Coverage

Add `plinth-skills-generator/src/test/resources/gherkin/skills/130-java-testing-strategies.feature`.

The acceptance scenarios should verify:

- RIGHT-BICEP requests read the RIGHT-BICEP reference and produce a gap matrix.
- A-TRIP requests read the A-TRIP reference and identify flakiness or quality violations.
- CORRECT requests read the CORRECT reference and identify boundary-condition gaps.
- Broad test-strategy requests may combine all three references.
- Code changes remain gated by compile and verification instructions.

Add a matching `130-java-testing-strategies` entry to `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.

## Two-Step Sequencing

### Step 1: Make the Change Easy

Behavior-preserving preparation should add the OpenSpec requirements, define the desired reference split, and add acceptance expectations before changing XML behavior. If implementation starts from the existing monolithic reference, preserve current content while moving it into focused files.

Validation after Step 1 should include `openspec validate --all` and Markdown validation for the planning artifacts.

### Step 2: Make the Behavior Change

Add the split XML references, update `130-skill.xml` routing, register references in `skills.xml`, add acceptance tests, regenerate local skills, and inspect generated Markdown. This changes generated skill behavior by making reference selection more precise and examples deeper.

Validation after Step 2 should include XML well-formedness checks, local skill generation, generated output inspection, applicable acceptance prompt execution, and plinth-skills-generator module verification.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl plinth-skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated `.agents/skills/130-java-testing-strategies/SKILL.md`.
- Inspect generated `.agents/skills/130-java-testing-strategies/references/130-*.md`.
- Execute the listed `130-java-testing-strategies` acceptance prompt after adding it to the prompt inventory, or record a skipped prompt with reason.
- Run `./mvnw clean verify -pl plinth-skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this planning change. Exact Java examples may be adjusted during implementation to align with existing style and generated Markdown readability.
