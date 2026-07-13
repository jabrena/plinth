## Context

Issue #940 asks for `707-technologies-onion-architecture`, a framework-agnostic Java skill for Onion architecture support. The issue body frames the user story around Java enterprise developers and architects who want to review and improve application boundaries with ArchUnit-aware guidance.

The existing `700` technology skills establish the implementation model: each skill has an index XML source, one or more reference XML sources when deeper guidance is needed, registration in `skills.xml`, local generation into `.agents/skills`, and acceptance coverage when the generated behavior needs verification.

## Decisions

### Skill Identifier

Use `707-technologies-onion-architecture`.

This follows the existing `700` technology skill sequence after `706-technologies-containers-docker` and keeps architecture-style technology guidance separate from the `030-034` architecture planning and diagramming skills.

### Skill Shape

The skill uses the existing XML source pattern:

- `plinth-skills-generator/src/main/resources/skill-indexes/707-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `plinth-skills-generator/src/main/resources/skill-references/707-technologies-onion-architecture.xml` provides detailed guidance, examples, and output expectations.
- `plinth-skills-generator/src/main/resources/skills.xml` registers skill id `707` and its reference.

Dedicated report templates are not required for the first slice. The initial deliverable is practical review guidance and acceptance-testable generated skill behavior.

### Onion Architecture Scope

The skill focuses on framework-agnostic Onion architecture review for Java projects:

- Domain model and domain service independence from infrastructure and framework APIs.
- Application/use-case services orchestrating domain behavior through ports or interfaces.
- Adapters and infrastructure implementations depending inward on application/domain contracts.
- Package and module dependency direction that prevents outer layers from leaking into inner layers.
- Boundary findings supported by code evidence and concrete remediation steps.

The skill must avoid prescribing Spring Boot, Quarkus, or Micronaut wiring unless the user explicitly asks for a framework-specific implementation. Framework runtime concerns should be routed to the matching framework skills.

### ArchUnit Verification

ArchUnit guidance is included as an optional verification approach because the issue explicitly links the ArchUnit architectures guide. The skill should describe how Onion architecture boundaries can be encoded as architecture tests and may show concise ArchUnit examples. It must not require adding ArchUnit to every project by default.

When dependency addition is needed, route Maven dependency evaluation to `111-java-maven-dependencies`, especially the existing ArchUnit dependency guidance.

### Two-Step Implementation Sequence

Step 1 makes the change easy without changing generated behavior broadly:

- Review existing `700` technology skill source patterns.
- Create the new XML source shape and registration plan.
- Add or prepare focused acceptance coverage expectations.
- Validate that source ownership and generated-output boundaries are clear.

Step 2 makes the behavior change:

- Add the `707` XML skill source and reference guidance.
- Register the skill and add acceptance coverage.
- Regenerate local skills and verify `.agents/skills/707-technologies-onion-architecture/SKILL.md`.

Each step must have verification: XML validation and focused source inspection after preparation, then generator and Maven verification after the behavior change.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl plinth-skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `.agents/skills/707-technologies-onion-architecture/SKILL.md`.
- Add `plinth-skills-generator/src/test/resources/gherkin/skills/707-technologies-onion-architecture.feature`.
- Add `707-technologies-onion-architecture` to `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- Execute only the listed `707` acceptance prompt when local generated output changes and the prompt runner is available; otherwise record the skip reason.
- Run `./mvnw clean verify -pl plinth-skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this first slice. Deeper report templates, richer framework-specific examples, or a separate architecture assessment output can be proposed in follow-up issues if maintainers want them.
