## Context

`701-technologies-openapi` is a framework-agnostic technology skill for OpenAPI 3.x contract quality. It should help agents review and improve OpenAPI artifacts without selecting Spring Boot, Quarkus, Micronaut, or runtime exposure details.

Current example coverage is thin:

| Reference | Top-level examples | Gap |
|---|---:|---|
| `701-technologies-openapi.xml` | 2 | Covers metadata and one operation/error shape, but not schemas, parameters, security, validation, examples, or breaking-change awareness |

## Decisions

### Preserve One Reference

Keep `701-technologies-openapi` as one reference. OpenAPI guidance is one coherent contract-review workflow, and splitting by OpenAPI section would create needless routing complexity for a compact technology skill.

### Add Contract Review Depth

The reference should grow from two examples to at least eight examples:

| Example | Purpose |
|---|---|
| Strong API metadata | Preserve existing metadata/versioning example |
| Explicit operations and errors | Preserve existing operation/error example |
| Reusable schema design | Required fields, formats, enum values, nullability, examples |
| Parameter design | Path/query/header parameters, pagination, filtering, sorting |
| Problem Details error model | Shared validation, conflict, and not-found shapes |
| Security schemes | Bearer/OAuth2/API key, global vs operation-level security |
| Content examples | Realistic request/response examples and content types |
| Breaking-change and validation gates | Additive vs breaking changes, Spectral or equivalent linting, pre-codegen validation |

### Stay Contract-Centric

The improved guidance must stay at the OpenAPI artifact layer:

- Do not prescribe framework-specific controller annotations.
- Do not configure runtime OpenAPI exposure.
- Do not execute code generation as part of this skill.
- Handoff to framework skills when the user asks for controller binding, runtime publication, or server implementation.

### Acceptance Coverage

Add `plinth-skills-generator/src/test/resources/gherkin/skills/701-technologies-openapi.feature`.

The acceptance scenarios should verify that the skill:

- reads `references/701-technologies-openapi.md`
- reviews a provided OpenAPI contract or diff
- identifies issues in schemas, parameters, errors, security, examples, and breaking changes
- proposes concrete YAML or JSON snippets
- stays framework-agnostic and routes implementation concerns to framework skills
- recommends validation/linting or breaking-change checks without hardcoding a single external tool as mandatory

Add `701-technologies-openapi` to the acceptance prompt inventory.

## Two-Step Sequencing

### Step 1: Make the Change Easy

Behavior-preserving preparation should add OpenSpec requirements and acceptance expectations before broad XML changes where practical. Existing examples should be preserved while new coverage targets are made explicit.

Validation after Step 1 should include `openspec validate --all` and Markdown validation for planning artifacts.

### Step 2: Make the Behavior Change

Update the `701` XML reference and any skill-index wording, add Gherkin acceptance coverage and prompt inventory entry, regenerate local skills, and inspect generated Markdown.

Validation after Step 2 should include XML well-formedness checks, local skill generation, generated output inspection, listed acceptance prompt execution, and plinth-skills-generator module verification.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl plinth-skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated `.agents/skills/701-technologies-openapi/SKILL.md`.
- Inspect generated `.agents/skills/701-technologies-openapi/references/701-technologies-openapi.md`.
- Execute the listed `701-technologies-openapi` acceptance prompt after adding it to the prompt inventory, or record a skipped prompt with reason.
- Run `./mvnw clean verify -pl plinth-skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this planning change. Exact OpenAPI snippets may be adjusted during implementation for readability and generated Markdown quality.
