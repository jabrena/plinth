---
status: "accepted"
date: 2026-07-23
decision-makers: Juan Antonio Breña Moral
consulted: Repository maintainers
informed: Repository contributors and downstream skill/agent/command consumers
---

# One XML Schema per Generated Artifact

## Context and Problem Statement

The repository generates several distinct output artifacts from XML sources: Agent Skills
(`SKILL.md`, from `plinth-skills-generator/src/main/resources/skill-indexes/`), skill
references (Markdown, from `plinth-skills-generator/src/main/resources/skill-references/`),
commands (from `plinth-commands-generator`), and agents (from `plinth-agents-generator`).

Each artifact type had its own XML shape and validation history. `agents.xsd` and
`commands.xsd` were each authored as new, specialized schemas colocated with their module's
`src/main/resources/` root, referenced by their XML sources via a relative
`xsi:noNamespaceSchemaLocation="../<schema>.xsd"`. Skill-index XML, by contrast, referenced a
remote, shared schema (`https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd`) with no local copy
and no automated schema-validation test, while skill-reference XML also referenced that same
remote schema and is validated against it by `RemoteSchemaValidationTest`.

This split meant skill-index generation depended on network access to a remote schema at
validation time, and there was no consistent, explicit policy that each generated-artifact type
owns its own schema. [Issue #991](https://github.com/jabrena/plinth/issues/991) requested a
local, artifact-scoped schema for skill-index XML (`skills.xsd`), which raised the broader
question this ADR resolves: should every generated-artifact type have its own schema, and should
schemas remain scoped strictly to the artifact type they validate?

More fundamentally, a single monolithic schema shared across artifact types cannot faithfully
represent them, because an agent, a command, and a skill index are different abstractions with
different lifecycles. Each is authored, versioned, and evolved on its own schedule: a skill index
can grow a new optional element (for example, a richer `constraints` shape) without any
corresponding change to what a command or an agent needs to express, and vice versa. A shared
`pml.xsd` would force every artifact type through one structural contract, so a change driven by
one abstraction's lifecycle (adding, tightening, or deprecating a field) either bleeds into
artifact types that never asked for it, or gets held back until every artifact type is ready to
accept it. Modeling each generated artifact with its own schema lets each abstraction's structure
evolve at its own pace, independent of the others.

Skill-reference XML is the deliberate exception to this per-artifact split, and for the opposite
reason: it is not a distinct abstraction that has outgrown the shared PML schema, it is that
schema's original abstraction. Before Agent Skills existed, this project generated system prompts
(the `.cursor/rules/` "system prompt" pipeline described in [ADR-004](./ADR-004-skill-generation.md),
which frames Agent Skills as "the natural evolution of system prompts"). Skill-reference XML —
with its `role`, `goal`, and template-driven structure — is that same system-prompt shape carried
forward, not a new one. `pml.xsd` was, and still is, the right schema for it. Additionally, `pml.xsd`
is currently published from this project's history, but the project's stated direction is to
finish migrating it fully into the dedicated `pml` repository in coming releases, so that repository
— not `plinth` — becomes its sole authoritative home. Forking a local, plinth-owned copy for
skill-reference XML now (the way `skills.xsd` does for skill-index XML) would pull that ownership
in the opposite direction from where it is headed.

## Decision Drivers

* Skill-index generation (`SkillsGenerator`) must not depend on runtime network access to a
  remote schema to be reliable and reproducible.
* Each generated artifact (skill-index, skill-reference, command, agent) is a distinct abstraction
  with its own lifecycle — authored, versioned, and structurally evolved on its own schedule; a
  shared schema across artifact types would couple unrelated changes and force every abstraction
  through one structural contract regardless of which one actually needs to change.
* `agents.xsd` and `commands.xsd` already established a working, colocated-schema convention that
  should be extended consistently rather than reinvented per artifact.
* Skill-reference XML intentionally remains on the shared PML schema because it is the direct
  continuation of this project's former system-prompt abstraction (see [ADR-004](./ADR-004-skill-generation.md)) —
  the abstraction `pml.xsd` was built to model — not a distinct artifact type that has outgrown it;
  forking a local copy for it would work against the schema's planned migration to being fully
  owned by the external `pml` repository. This ADR must record that boundary, not blur it.

## Considered Options

* Option 1: One XML Schema per generated artifact, each colocated with its owning module's
  `src/main/resources/` root and referenced by its XML sources via a relative
  `../<schema>.xsd` path.
* Option 2: One shared schema across all XML sources that produce generated Markdown/skill output
  (skill-index, skill-reference, command, agent combined).
* Option 3: Keep remote-schema references for artifact types that do not yet have a local,
  specialized schema (status quo for skill-index XML before this change), and only formalize a
  policy once every artifact type has migrated.

## Decision Outcome

Chosen option: "Option 1: One XML Schema per generated artifact", because a skill index, a
command, and an agent are different abstractions with different lifecycles, and one monolithic
`pml.xsd` cannot represent that without coupling their independent evolution together. This option
also matches the already-shipped, working precedent (`agents.xsd`, `commands.xsd`) and closes the
one remaining gap (skill-index XML depending on a remote schema) without merging unrelated
artifact types under a single schema file. Under this policy:

* Skill-index XML (`plinth-skills-generator/src/main/resources/skill-indexes/*.xml`) validates
  against `skills.xsd`, a complete, unchanged local copy of PML 0.8.0 `pml.xsd`, colocated at
  `plinth-skills-generator/src/main/resources/skills.xsd`. This is the scope implemented by this
  ADR's originating change (`documentation/openspec/changes/add-skills-xsd-schema/`).
* Skill-reference XML (`plinth-skills-generator/src/main/resources/skill-references/*.xml`)
  continues to validate against the remote PML 0.8.0 `pml.xsd`
  (`https://jabrena.github.io/pml/schemas/0.8.0/pml.xsd`), unaffected by this ADR. It is not
  migrated to a local schema by this decision, because it is the same abstraction `pml.xsd` was
  originally built to model (this project's former system prompts, per ADR-004) rather than a
  distinct one, and the schema's ownership is headed toward the external `pml` repository, not
  toward being forked locally.
* Command XML (`plinth-commands-generator/src/main/resources/commands/*.xml`) validates against
  `commands.xsd`, already colocated at `plinth-commands-generator/src/main/resources/commands.xsd`.
* Agent XML (`plinth-agents-generator/src/main/resources/agents/*.xml`) validates against
  `agents.xsd`, already colocated at `plinth-agents-generator/src/main/resources/agents.xsd`.

**Colocated-schema, relative-path convention:** every schema is colocated at its owning module's
`src/main/resources/` root; every artifact XML source directory sits exactly one level below that
root and references its schema via `xsi:noNamespaceSchemaLocation="../<schema>.xsd"`
(`../agents.xsd`, `../commands.xsd`, `../skills.xsd`). This attribute is IDE/developer-tooling
convenience and self-documentation only — no runtime validation layer (generator code, Maven
tests, or CI) resolves the schema through this attribute; each layer loads its schema by a fixed
classpath resource name instead. Any future generated-artifact schema should follow this same
convention: colocate the `.xsd` at the module's resources root, and reference it with a relative
`../<schema>.xsd` path from the artifact's XML source directory.

### Consequences

* Good, because skill-index generation and its Maven test coverage no longer depend on network
  access to a remote schema, matching the reliability already enjoyed by commands/agents.
* Good, because the policy is explicit and consistent: one schema, one artifact type, one owning
  module, extending a proven convention rather than introducing a new one.
* Good, because skill-reference XML's continued use of the remote schema is an explicit, recorded
  boundary rather than an accidental gap.
* Neutral, because this ADR does not require migrating skill-reference XML off the remote schema;
  that remains a distinct, unscheduled decision if ever pursued.
* Bad, because four schema files (soon `skills.xsd`, `commands.xsd`, `agents.xsd`, plus the
  remote-only skill-reference case) must each be kept in sync with their artifact's evolving XML
  shape independently, with no shared validation code path.

### Confirmation

The implementation is confirmed when:

* `plinth-skills-generator/src/main/resources/skills.xsd` exists as a byte-for-byte copy of PML
  0.8.0 `pml.xsd`, and all 125 `skill-indexes/*.xml` files reference it via
  `xsi:noNamespaceSchemaLocation="../skills.xsd"`.
* `SkillIndexSchemaValidationTest` validates the complete `skill-indexes/` inventory plus a
  representative invalid fixture against `skills.xsd`, with no network access required
  (`XMLConstants.ACCESS_EXTERNAL_DTD` / `ACCESS_EXTERNAL_SCHEMA` set to `""`).
* `RemoteSchemaValidationTest` continues to validate `skill-references/*.xml` against the remote
  PML 0.8.0 schema, unmodified in scope.
* `CommandSchemaTest` and the analogous agent schema test continue to validate `commands.xsd` and
  `agents.xsd` against their respective inventories, unaffected by this ADR.

## Pros and Cons of the Options

### Option 1: One XML Schema per generated artifact (colocated, relative-path convention)

* Good, because it extends an already-proven, working convention instead of inventing a new one.
* Good, because each artifact type's schema evolves independently without cross-artifact coupling.
* Good, because it closes the skill-index remote-schema dependency gap.
* Bad, because it means four independent schema files to maintain instead of one shared file.

### Option 2: One shared schema across all XML sources

* Good, because a single schema file is simpler to reference.
* Bad, because it treats abstractions with different lifecycles — skill index, command, agent —
  as if they shared one structural contract; a change one abstraction's lifecycle demands (a new
  field, a tightened constraint, a deprecation) either leaks into artifact types that never asked
  for it, or gets blocked until every artifact type is ready to accept it.
* Bad, because it couples unrelated artifact types (a command XML change could force a schema
  review for skill-index XML, and vice versa).
* Bad, because it contradicts the already-shipped `agents.xsd`/`commands.xsd` precedent, which
  would need to be unwound.

### Option 3: Keep remote-schema references for not-yet-migrated artifact types

* Good, because it requires no immediate migration work.
* Bad, because it leaves skill-index generation dependent on remote network access indefinitely.
* Bad, because it delays, rather than resolves, the policy question this ADR is meant to settle.

## More Information

* Issue: https://github.com/jabrena/plinth/issues/991
* Originating change: `documentation/openspec/changes/add-skills-xsd-schema/`
  (`proposal.md`, `design.md`, `specs/pml-skills-schema/spec.md`,
  `specs/skills-generator-module/spec.md`)
* Prior decisions: [ADR-001](./ADR-001-generate-cursor-rules-from-xml-files.md) (XSD → Markdown
  generation precedent, to be updated separately as a non-gating follow-up to reflect the current
  architecture), [ADR-004](./ADR-004-skill-generation.md), [ADR-006](./ADR-006-separate-local-skill-generation-from-release-publishing.md)
