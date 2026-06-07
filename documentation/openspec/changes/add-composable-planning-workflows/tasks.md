## 1. Approval and Tests

- [ ] 1.1 Confirm approval for new XML files and `034-architecture-design-exploration` as the identifier.
- [ ] 1.2 Add focused skill inventory and generation tests for skill `034` and refactored skills `041` and `042`.

## 2. Design Discovery

- [ ] 2.1 Add `skills-generator/src/main/resources/skill-indexes/034-skill.xml`.
- [ ] 2.2 Add `skills-generator/src/main/resources/skill-references/034-architecture-design-exploration.xml`.
- [ ] 2.3 Add skill `034` to `skills-generator/src/main/resources/skills.xml`.

## 3. Composable Planning

- [ ] 3.1 Refactor the `041` skill index/reference XML to accept issue, design, ADR, OpenSpec, or combined inputs.
- [ ] 3.2 Add source recording and derivation-direction behavior to `041`.
- [ ] 3.3 Preserve plan-only execution without requiring OpenSpec.

## 4. Composable OpenSpec

- [ ] 4.1 Refactor the `042` skill index/reference XML to accept issue, plan, design, ADR, existing OpenSpec, or combined inputs.
- [ ] 4.2 Add one-change versus multiple-change assessment and user-approved change maps.
- [ ] 4.3 Add concern-specific authority, no-silent-sync, and explicit conflict handling.

## 5. Change Verification

- [ ] 5.1 Validate all edited XML files with `xmllint`.
- [ ] 5.2 Run skill-focused generator tests.
- [ ] 5.3 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 5.4 Run local skill generation without the `release` profile and inspect skills `034`, `041`, and `042`.
