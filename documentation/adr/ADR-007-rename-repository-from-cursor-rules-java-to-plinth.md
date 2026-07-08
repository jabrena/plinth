---
status: "accepted"
date: 2026-07-08
decision-makers: Juan Antonio Breña Moral
consulted: Repository maintainers
informed: Repository contributors and downstream skill consumers
---

# Rename Repository from `cursor-rules-java` to `plinth`

## Context and Problem Statement

The repository started as `cursor-rules-java`, a name that accurately described the original
scope: Cursor rules for Java enterprise development. Since then, the project has expanded into
a broader foundation for generated agent skills, commands, workflows, OpenSpec planning,
architecture guidance, documentation, and release pipelines.

The old repository name now overemphasizes one legacy delivery format and one IDE product at
the same time that the project is moving toward Agent Skills and multi-agent adoption. The
repository needs a name that represents the project's role as a reusable foundation for AI-assisted
Java development while preserving continuity for existing users and downstream references.

In civil architecture, a plinth is the base that supports the visible structure above it. That
metaphor fits the project better than the original name: the visible output may be a skill,
agent, command, workflow, or generated article, but the value depends on the supporting base
of requirements, design constraints, compatibility strategy, tests, security checks, architecture
boundaries, operational evidence, and human review.

## Decision Drivers

* Align the project name with the broader scope beyond Cursor rules.
* Avoid presenting deprecated `.cursor/rules/` output as the primary project identity.
* Keep the GitHub repository identity concise and memorable for skills, agents, commands, and documentation.
* Express the project's role as a stable engineering base rather than a single generated output.
* Preserve continuity for existing consumers while the ecosystem updates external references.
* Support previous decisions that moved the project toward Agent Skills and explicit release publishing.

## Considered Options

* Option 1: Keep the repository name `cursor-rules-java`.
* Option 2: Rename the repository to `plinth` while documenting compatibility expectations.
* Option 3: Rename all project identifiers, package names, artifact IDs, and external references immediately.

## Decision Outcome

Chosen option: "Option 2: Rename the repository to `plinth` while documenting compatibility expectations",
because it gives the project a durable identity that fits its current direction without forcing a risky
all-at-once migration across build metadata, documentation, package names, registries, and external portals.

The name `plinth` represents the project as a foundation for agent-ready Java engineering guidance. The
repository URL becomes `https://github.com/jabrena/plinth`, while historical names and technical identifiers
can remain where changing them would create avoidable compatibility or release risk.

This decision intentionally separates project identity from immediate technical renaming. New
project-level communication should prefer `plinth`, while compatibility-sensitive identifiers
can be reviewed later with their own migration evidence.

### Consequences

* Good, because the public project name no longer anchors the project to Cursor-specific rule files.
* Good, because the new name can cover skills, agents, commands, workflows, OpenSpec artifacts, and future delivery formats.
* Good, because the name communicates that AI-assisted delivery needs a foundation of planning, tests, validation, security gates, and review.
* Good, because documenting the rename gives contributors a single place to understand why mixed historical references may exist.
* Bad, because some external registries, articles, package identifiers, and historical documentation may continue to mention `cursor-rules-java`.
* Bad, because maintainers need to distinguish between intentional historical compatibility references and stale references that should be updated.
* Neutral, because GitHub redirects can preserve access for existing repository links, but downstream tools may still display cached or legacy names.

### Confirmation

The decision is confirmed when:

* The repository canonical URL is `https://github.com/jabrena/plinth`.
* Contributor-facing documentation can reference this ADR when explaining the rename.
* New project-level documentation prefers `plinth` for the repository identity.
* Remaining `cursor-rules-java` references are treated as compatibility, historical, artifact, or third-party registry references unless changed by a separate migration.

## Pros and Cons of the Options

### Option 1: Keep `cursor-rules-java`

* Good, because it avoids rename churn and preserves the original search term.
* Good, because existing downstream references remain semantically consistent.
* Bad, because it keeps the project identity tied to Cursor rules after that output became legacy.
* Bad, because it undersells the broader skills, agents, commands, and workflow scope.

### Option 2: Rename to `plinth` and document compatibility expectations

* Good, because it gives the project a broader and more durable identity.
* Good, because it avoids coupling the name to a single IDE or generated output format.
* Good, because it reflects the project's foundation role for skills, agents, commands, workflows, validation, and release evidence.
* Good, because compatibility-sensitive identifiers can be evaluated separately instead of renamed in one disruptive change.
* Bad, because the repository can temporarily contain both names in different contexts.

### Option 3: Rename every related identifier immediately

* Good, because it would create a fully consistent naming surface.
* Bad, because it risks breaking package coordinates, generated metadata, documentation links, external registries, and user automation.
* Bad, because it mixes a repository identity decision with a larger migration that should be planned and validated separately.

## More Information

* Issue: https://github.com/jabrena/plinth/issues/986
* Prior decisions: [ADR-004](./ADR-004-skill-generation.md), [ADR-005](./ADR-005-drop-cursor-rules-support-in-favor-of-agent-skills.md), [ADR-006](./ADR-006-separate-local-skill-generation-from-release-publishing.md)
