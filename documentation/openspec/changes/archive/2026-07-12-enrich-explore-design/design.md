## Context

After issue #1019 and archived change `2026-07-12-move-planning-spec-ownership-to-architect`, `/create-spec` routes through `@robot-architect`. The command and agent contracts still apply design skills during initial OpenSpec creation, while `/explore-design` remains a narrow issue-only entry point backed by `034-architecture-design-exploration`.

Issue #1026 targets the 0.18.0 workflow mapping documented in the Plinth 0.17.0 release article: `/create-spec` owns Proposal and Specification with `042-planning-openspec` only; `/explore-design` owns Task planning and design refinement with skills `051`–`057`, `121`–`123`, and `130`.

## Goals / Non-Goals

**Goals:**

- Make `/create-spec` the first workflow step with `042-planning-openspec` as its sole associated skill.
- Make `/explore-design` the second workflow step that refines an issue or OpenSpec change using the full design skill set, including `057-design-feature-toggles`.
- Split `@robot-architect` missions so OpenSpec authoring and design refinement are distinct, ordered steps.
- Remove `034-architecture-design-exploration` from all source, generated, inventory, installer, and documentation surfaces.
- Update acceptance tests and lifecycle documentation before changing generated command/agent prompts.

**Non-Goals:**

- Change `@robot-tech-lead` implementation-delivery responsibilities.
- Change `@robot-business-analyst` issue or alignment responsibilities.
- Implement application code.
- Change OpenSpec file formats or generator architecture beyond skill/command/agent source updates.

## Decisions

### Create-spec-first workflow

`/create-spec` creates the initial OpenSpec proposal, design, specification, and task artifacts from approved inputs. It records source traceability, derivation direction, assumptions, unresolved decisions, validation expectations, and handoff details. It does not apply design skills `051`–`057`, `121`–`123`, or `130`.

### Explore-design-second workflow

`/explore-design` improves and refines the technical approach after initial specification. It accepts an issue or OpenSpec change, compares alternatives and trade-offs, recommends a design direction, identifies ADR candidates and unresolved questions, and may refine the existing OpenSpec change without replacing initial authoring owned by `/create-spec`.

### Agent mission decoupling

`@robot-architect` mission 4 (OpenSpec authoring) retains `@041-planning-plan-mode` and `@042-planning-openspec` plus traceability and handoff responsibilities. Design refinement moves to a separate step owned by `/explore-design` and lists skills `051`–`057`, `121`–`123`, and `130` in the prescribed order. Mission 1 no longer references `034`.

### Retire skill 034 physically

`034-architecture-design-exploration` overlaps the enriched design skill set and must be deleted from XML inventory, skill indexes, acceptance tests, prompt inventories, installer references, README/inventory docs, and generated `skills/` and `.agents/skills/` output after regeneration. Do not keep it as deprecated or orphaned.

### Two-step implementation sequencing

Follow the repository two-step method:

1. **Behavior-preserving preparation:** Update acceptance tests and documentation expectations to the target contracts without changing generated command/agent/skill prompts.
2. **Behavior-changing work:** Update canonical source assets, delete skill `034` sources, regenerate local output, and validate.

## Acceptance Test Updates From Issue #1026

The first implementation slice MUST align repository acceptance tests with issue #1026 before changing generated prompts. Planned target Gherkin files live under this OpenSpec change for `/implement-spec` to copy into repository sources:

- `acceptance-tests/create-spec.feature` → `plinth-skills-generator/src/test/resources/gherkin/commands/create-spec.feature`
- `acceptance-tests/explore-design.feature` → `plinth-skills-generator/src/test/resources/gherkin/commands/explore-design.feature`
- `acceptance-tests/robot-architect.feature` → `plinth-skills-generator/src/test/resources/gherkin/agents/robot-architect.feature`

These files are planned target state for `/implement-spec`; they are not applied by `/create-spec`.

## Risks / Trade-offs

- [Risk] Users may still expect design exploration during `/create-spec`. → Update guides, inventories, README localized counterparts, and release notes with the create-spec-first workflow.
- [Risk] Skill `034` removal breaks users referencing it directly. → Document migration to `/explore-design` and the enriched design skill set; verify no repository references remain.
- [Risk] Generated outputs drift from source assets. → Edit canonical assets under `plinth-skills-generator` and regenerate; do not edit generated files directly.
- [Risk] Acceptance tests and prompts diverge. → Update `create-spec.feature`, `explore-design.feature`, and `robot-architect.feature` together.

## Compatibility Review

- **POTENTIALLY BREAKING**: `/create-spec` no longer applies design skills `051`–`057`, `121`–`123`, or `130`. Mitigation: document `/explore-design` as the follow-up step; update command source, acceptance tests, and workflow guides.
- **POTENTIALLY BREAKING**: `/explore-design` usage changes from `<issue-or-artifact>` to `<issue|openspec-change>` and retires skill `034`. Mitigation: update command source, inventories, installer references, and acceptance tests.
- **POTENTIALLY BREAKING**: `@robot-architect` mission split changes when design skills apply. Mitigation: update agent source and `robot-architect.feature`.
- **NON-BREAKING**: `@robot-tech-lead` and `@robot-business-analyst` responsibilities remain unchanged.

## Validation Strategy

- Validate OpenSpec with `openspec validate --all`.
- Validate edited XML sources with `xmllint --noout` when XML files change.
- Regenerate local agent/command/skill output with `./mvnw clean install -pl plinth-skills-generator`.
- Run `./mvnw clean verify -pl plinth-skills-generator` or focused generator verification.
- Execute affected acceptance prompts listed in command and agent prompt inventories.
- Run Markdown validation for documentation-only changes when applicable.
- Confirm no repository file still routes to or documents `034-architecture-design-exploration`.
