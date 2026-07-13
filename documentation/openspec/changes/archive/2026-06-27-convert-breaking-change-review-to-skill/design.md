## Context

Issue #954 converts the breaking-change review workflow from a command into `@056-design-avoid-breaking-changes`. Issue #886 supplies the original value statement: maintainers need to review OpenSpec plans or specs for compatibility risks before implementation or release promotion.

The current repository still contains `/review-breaking-changes` references in command sources and README files. Keeping both the command and the new skill would create two user-facing entry points for the same workflow, increasing drift risk. The safer design is to make the skill the source of behavior and remove the command from the command bundle in the same change.

## Decisions

### Use a skill as the primary workflow contract

Create `056-design-avoid-breaking-changes` as the generated skill identifier. The skill owns the compatibility review workflow, examples, constraints, and handoff guidance.

The skill should review proposed changes across several breaking-change dimensions:

- command names, accepted inputs, output format, and installation behavior
- skill identifiers, triggers, metadata, generated references, and routing boundaries
- generated output ownership for `.agents/skills`, public `skills/`, `.cursor/commands`, `.cursor/rules`, and `docs/`
- XML sources, XInclude dependencies, schemas, and generator registrations
- README and localized README discoverability
- tests, acceptance prompt inventories, and CI expectations
- external API, schema, configuration, data, and migration contracts when a plan/spec includes them
- release notes, deprecation paths, and user migration guidance

### Retire the command instead of aliasing it

Remove `/review-breaking-changes` from command-owned sources rather than keeping it as a thin alias. The issue asks to remove the current command, and an alias would still be a command-level contract.

The removal must be complete enough that installation workflows, command inventories, README files, tests, and generated command prompts no longer advertise or validate `/review-breaking-changes`.

### Preserve issue traceability

The new skill should retain traceability to issue #886 in OpenSpec artifacts and implementation notes, but the final user workflow should say to use `@056-design-avoid-breaking-changes`.

### Keep implementation source-owned

Skill behavior should be authored in XML sources under `plinth-skills-generator/src/main/resources/`. Command retirement should edit generator sources and tests, not generated files directly. README files are authored docs and should be updated directly, with localized README files kept in sync.

## Two-Step Sequencing

### Step 1: Make the change easy

Add the new skill source, registration, and acceptance coverage while leaving the existing command in place temporarily during implementation. This allows the skill behavior and compatibility-review scenarios to be validated before command removal.

Validation after Step 1:

- Validate changed XML files with `xmllint --noout`.
- Run focused skill generation checks.
- Inspect generated `.agents/skills/056-design-avoid-breaking-changes/SKILL.md`.

### Step 2: Make the behavior change

Remove `/review-breaking-changes` from command sources, inventories, command tests, command acceptance prompt inventory, and README files. Regenerate local outputs and verify that no active source or generated local output advertises the retired command.

Validation after Step 2:

- Run `rg "review-breaking-changes"` and confirm only historical OpenSpec, changelog, or intentional traceability references remain.
- Run `./mvnw clean verify -pl plinth-skills-generator`.
- Run `openspec validate --all` from `documentation/`.

## Compatibility Review Output

The skill should produce a concise report with:

- source artifacts reviewed
- risk summary by compatibility surface
- severity-ranked findings
- migration or deprecation guidance when a breaking change is intentional
- validation checklist for generated output, tests, docs, and release notes
- explicit no-issue result when no breaking-change risks are found

## Risks / Trade-offs

- [Risk] Removing a command can break users who already adopted it. -> Update README and inventories to point to the skill, and include migration guidance in release notes when implemented.
- [Risk] Skill guidance becomes too broad. -> Keep the first version focused on reviewing supplied plans/specs and reporting risks, not automatically changing implementation artifacts.
- [Risk] Generated command output drifts from command sources. -> Validate through generator tests and local generation rather than editing generated output directly.

## Open Questions

None for this spec. Implementation should make conservative choices that follow existing skill XML, command retirement, README synchronization, and validation patterns.
