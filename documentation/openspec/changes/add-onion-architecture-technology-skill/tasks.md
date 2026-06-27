# Tasks

## 1. Implementation Checklist

- [ ] 1.1 Review issue #940, the ArchUnit architectures user guide link from the issue, and existing `701-706` technology skill source patterns.
- [ ] 1.2 Confirm `707-technologies-onion-architecture` is not already registered in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.3 Create `skills-generator/src/main/resources/skill-indexes/707-skill.xml` with metadata, title, goal, constraints, triggers, and workflow steps.
- [ ] 1.4 Add `skills-generator/src/main/resources/skill-references/707-technologies-onion-architecture.xml` with Onion architecture review guidance, ArchUnit-aware verification examples, and output expectations.
- [ ] 1.5 Register skill id `707` with `skillId="707-technologies-onion-architecture"` and the new reference in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.6 Keep the skill framework-agnostic and route Spring Boot, Quarkus, and Micronaut runtime wiring to the matching framework skills.
- [ ] 1.7 Keep ArchUnit guidance optional and route dependency-addition decisions to `111-java-maven-dependencies` when needed.
- [ ] 1.8 Add `skills-generator/src/test/resources/gherkin/skills/707-technologies-onion-architecture.feature` with acceptance coverage for Onion architecture review and framework-boundary routing.
- [ ] 1.9 Add `707-technologies-onion-architecture` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` using the existing `execute @...feature` prompt format.
- [ ] 1.10 Validate changed XML files with `xmllint --noout`.
- [ ] 1.11 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.12 Inspect generated local `.agents/skills/707-technologies-onion-architecture/SKILL.md`.
- [ ] 1.13 Execute the listed `707-technologies-onion-architecture` acceptance prompt when the prompt runner is available; otherwise record the skip reason.
- [ ] 1.14 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.15 Run `openspec validate --all`.
