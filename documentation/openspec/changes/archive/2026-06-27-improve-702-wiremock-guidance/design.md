## Context

`702-technologies-wiremock` is a framework-agnostic technology skill for portable WireMock behavior and stub quality. It complements integration-test skills by focusing on HTTP stubs rather than framework bootstrapping.

Current example coverage is thin:

| Reference | Top-level examples | Gap |
|---|---:|---|
| `702-technologies-wiremock.xml` | 2 | Covers isolation concept and one JSON mapping, but not Java DSL, fixtures, dynamic ports, fault simulation, request journal debugging, or broad matcher risk in depth |

## Decisions

### Preserve One Reference

Keep `702-technologies-wiremock` as one reference. WireMock guidance is one coherent stub-review workflow, and splitting by mapping style or fault mode would be too granular for this skill.

### Add Stub Quality Depth

The reference should grow from two examples to at least seven examples:

| Example | Purpose |
|---|---|
| Per-test stubs and reset | Preserve existing isolation example and make it concrete |
| Precise matching and verification | Preserve existing JSON mapping example |
| Programmatic Java DSL stub | Show WireMock Java DSL matching and response patterns |
| `bodyFileName` fixtures | Show mapping and `__files` layout for reusable payloads |
| Dynamic port/base URL propagation | Avoid hardcoded ports and show runtime injection conceptually |
| Request journal and unmatched request debugging | Show how to diagnose near misses and unexpected traffic |
| Faults and delay scenarios | Show deliberate resilience cases without making happy paths flaky |
| Query/header/body matching | Show practical specificity beyond path-only matching |

### Stay Framework-Agnostic

The improved guidance must stay at the WireMock behavior and HTTP-stub layer:

- Do not prescribe `@SpringBootTest`, `@QuarkusTest`, or `@MicronautTest` setup.
- Do not replace framework integration-test base-class guidance from `132`, `322`, `422`, or `522`.
- Handoff to integration-test skills when the user needs application bootstrapping or framework lifecycle wiring.

### Acceptance Coverage

Add `skills-generator/src/test/resources/gherkin/skills/702-technologies-wiremock.feature`.

The acceptance scenarios should verify that the skill:

- reads `references/702-technologies-wiremock.md`
- reviews JSON mappings or Java DSL stubs
- identifies broad matchers, leaked stubs, missing verification, hardcoded ports, and fixture-layout issues
- proposes concrete JSON or Java DSL snippets
- keeps framework bootstrapping out of scope and routes that work to integration-test skills

Add `702-technologies-wiremock` to the acceptance prompt inventory.

## Two-Step Sequencing

### Step 1: Make the Change Easy

Behavior-preserving preparation should add OpenSpec requirements and acceptance expectations before broad XML changes where practical. Existing examples should be preserved while new coverage targets are made explicit.

Validation after Step 1 should include `openspec validate --all` and Markdown validation for planning artifacts.

### Step 2: Make the Behavior Change

Update the `702` XML reference and any skill-index wording, add Gherkin acceptance coverage and prompt inventory entry, regenerate local skills, and inspect generated Markdown.

Validation after Step 2 should include XML well-formedness checks, local skill generation, generated output inspection, listed acceptance prompt execution, and skills-generator module verification.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated `.agents/skills/702-technologies-wiremock/SKILL.md`.
- Inspect generated `.agents/skills/702-technologies-wiremock/references/702-technologies-wiremock.md`.
- Execute the listed `702-technologies-wiremock` acceptance prompt after adding it to the prompt inventory, or record a skipped prompt with reason.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this planning change. Exact WireMock snippets may be adjusted during implementation for readability and generated Markdown quality.
