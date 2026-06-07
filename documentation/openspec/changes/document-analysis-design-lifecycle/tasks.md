## 1. Dependency Gate

- [ ] 1.1 Confirm `add-analysis-design-commands` is integrated and validated.
- [ ] 1.2 Confirm `add-analysis-design-agents` is integrated and validated.
- [ ] 1.3 Confirm `add-composable-planning-workflows` is integrated and validated.
- [ ] 1.4 Resolve whether `robot-coordinator` has a temporary compatibility alias.

## 2. README and Inventories

- [ ] 2.1 Update `README.md`, `README_ES.md`, and `README_ZH.md`.
- [ ] 2.2 Update `documentation/guides/INVENTORY-COMMANDS-JAVA.md` from the canonical command inventory template.
- [ ] 2.3 Update `documentation/guides/INVENTORY-AGENTS-JAVA.md` from the canonical agent inventory template.

## 3. Localized Guidance

- [ ] 3.1 Update English, Spanish, and Chinese agent guides for architect, tech lead, business analyst, coder delegation, and migration.
- [ ] 3.2 Update English, Spanish, and Chinese workflow guides for feature-branch, worktree isolation, design exploration, ADR/diagram, plan-only, spec-first, plan-to-spec, alignment, and delivery paths.
- [ ] 3.3 Document artifact authority, derivation direction, conflict handling, and OpenSpec change decomposition.
- [ ] 3.4 Inspect changed local links and remove stale coordinator references except explicit migration notes.

## 4. Integrated Verification

- [ ] 4.1 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 4.2 Run `./mvnw clean install -pl skills-generator` without the `release` profile and inspect `.agents/skills`.
- [ ] 4.3 Run `jbang .github/scripts/MarkdownValidator.java --verbose .`.
- [ ] 4.4 Run `npx skill-check@latest .agents/skills --no-security-scan --format github` when available.
- [ ] 4.5 Run stale-name and local-link checks across README and documentation.
- [ ] 4.6 Run `openspec validate --all` from `documentation/`.
