## Context

The current analysis/design lifecycle has three coordinating agents:

- `robot-business-analyst` for issue quality, traceability, and readiness.
- `robot-architect` for design exploration, ADRs, and diagrams.
- `robot-tech-lead` for plan/OpenSpec creation and implementation delivery coordination.

Issue #1019 asks for a clearer lifecycle boundary: `robot-architect` takes the pre-implementation work that `robot-tech-lead` will no longer take, and `robot-tech-lead` focuses on implementation-phase delivery.

This change does not transfer any `robot-business-analyst` responsibility. Issue creation, requirements quality, traceability, read-only alignment review, and delivery-readiness review remain owned by `robot-business-analyst`.

## Goals / Non-Goals

**Goals:**

- Make `robot-architect` the owner of architecture-led planning and OpenSpec creation.
- Make `robot-tech-lead` the owner of delivery coordination from an approved execution artifact.
- Route `/create-spec` through `@robot-architect`.
- Preserve framework-aware coder delegation by `robot-tech-lead`.
- Preserve all `robot-business-analyst` responsibilities unchanged.
- Keep both coordinating agents out of direct application-code implementation.

**Non-Goals:**

- Change `robot-business-analyst` missions, prompts, issue-quality workflow, or read-only alignment review.
- Change coder-agent responsibilities.
- Remove `robot-tech-lead`.
- Change OpenSpec file formats or generator architecture.
- Edit generated release output directly.

## Decisions

### Move plan and OpenSpec creation to architect

`robot-architect` should own the bridge from approved design direction to implementation-ready artifacts. This includes source traceability, alternatives, trade-offs, unresolved questions, relevant planning/design/testing/migration skills, and explicit handoff details for `robot-tech-lead`. The bridge starts after business-analysis inputs are available; it does not replace `robot-business-analyst` issue creation, requirements-quality, traceability, or alignment-review responsibilities.

### Keep tech lead delivery-only

`robot-tech-lead` should treat an approved plan or OpenSpec `tasks.md` as the execution artifact. Its role remains framework identification, implementation-agent selection, delegation by groups, task tracking, validation evidence synthesis, blocker reporting, and OpenSpec task status updates.

### Move `/create-spec` command ownership

`/create-spec` creates or updates OpenSpec changes, which is now pre-implementation work. Its owning agent and acceptance coverage should move from `@robot-tech-lead` to `@robot-architect`. This is a compatibility-relevant routing change and needs documentation/release guidance.

### Use two-step sequencing

The implementation should first update acceptance tests and documentation expectations without changing generated behavior, then update source prompt assets and regenerate local output. Each step needs validation so reviewers can see whether failures come from changed expectations or changed behavior.

## Acceptance Test Updates From Issue #1019

The first implementation slice MUST update the current acceptance-test contracts to match issue #1019 before changing agent or command prompt source assets. The planned target Gherkin files live under this OpenSpec change so `/implement-spec` can copy their intent into the repository acceptance-test sources:

- `acceptance-tests/robot-architect.feature` -> `plinth-skills-generator/src/test/resources/gherkin/agents/robot-architect.feature`
- `acceptance-tests/robot-tech-lead.feature` -> `plinth-skills-generator/src/test/resources/gherkin/agents/robot-tech-lead.feature`
- `acceptance-tests/create-spec.feature` -> `plinth-skills-generator/src/test/resources/gherkin/commands/create-spec.feature`

These files are planned target state for `/implement-spec`; they are not applied by `/create-spec`.

## Risks / Trade-offs

- [Risk] Existing users may continue asking `@robot-tech-lead` to create specs. -> Update `/create-spec`, guides, inventories, examples, and release notes with migration wording.
- [Risk] Architect becomes too broad. -> Keep its new responsibility pre-implementation only and prohibit direct implementation or delivery delegation.
- [Risk] Generated outputs drift from source assets. -> Edit canonical source assets and run the generator workflow instead of editing generated files directly.
- [Risk] Command acceptance tests and agent acceptance tests diverge. -> Update `create-spec.feature`, `robot-architect.feature`, and `robot-tech-lead.feature` together.

## Compatibility Review

- **POTENTIALLY BREAKING**: `/create-spec` owning agent changes from `@robot-tech-lead` to `@robot-architect`. Mitigation: update command source, command acceptance test, command inventories, README/guides, localized docs, and release notes.
- **POTENTIALLY BREAKING**: Direct prompts to `@robot-tech-lead` for plan/OpenSpec creation should route differently. Mitigation: tech lead prompt must explain routing to architect and require approved execution artifacts before delivery.
- **NON-BREAKING**: Coder-agent routing remains unchanged once delivery begins.

## Validation Strategy

- Validate OpenSpec with `openspec validate --all`.
- Validate changed Gherkin acceptance-test syntax through the `plinth-skills-generator` test suite.
- Validate edited XML assets with `xmllint --noout` when XML changes are made.
- Regenerate local agent/command output with `./mvnw clean install -pl plinth-skills-generator`.
- Run focused command/agent acceptance prompts for changed prompt outputs listed in the repository prompt inventories.
- Run Markdown validation for documentation-only changes when applicable.
