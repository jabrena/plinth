## 1. Agent Tests

- [x] 1.1 Add focused assertions for `robot-architect.md`, `robot-tech-lead.md`, and the absence of non-migration `robot-coordinator` links.
- [x] 1.2 Add assertions that all four existing coder agents remain installed and referenced by the tech lead.

## 2. Agent Assets

- [x] 2.1 Promote `.cursor/agents/robot-architect.md` into `skills-generator/src/main/resources/skill-references/assets/agents/robot-architect.md`.
- [x] 2.2 Extend architect missions for design exploration, ADRs, and diagrams.
- [x] 2.3 Replace `robot-coordinator.md` with `robot-tech-lead.md`, preserving framework routing and delegation.
- [x] 2.4 Add independent plan/spec creation and selected-workflow delivery missions to the tech lead.
- [x] 2.5 Extend `robot-business-analyst.md` with issue creation and read-only alignment/readiness review.

## 3. Installer and Inventory

- [x] 3.1 Update `skills-generator/src/main/resources/skill-references/005-agents-installation.xml`.
- [x] 3.2 Update `skills-generator/src/main/resources/skill-references/assets/java-agents-inventory-template.md`.
- [x] 3.3 Validate `005-agents-installation.xml` with `xmllint`.

## 4. Change Verification

- [x] 4.1 Run agent-focused generator tests.
- [x] 4.2 Run `./mvnw clean verify -pl skills-generator`.
- [x] 4.3 Run local skill generation without the `release` profile and inspect agent links and filenames.
