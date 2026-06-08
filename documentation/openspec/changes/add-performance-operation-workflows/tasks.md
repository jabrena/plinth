## 1. Command Assets

- [ ] 1.1 Add `/profile` under canonical command sources with accepted inputs, owner, workflow, outputs, and safeguards.
- [ ] 1.2 Add `/benchmark` under canonical command sources with accepted inputs, owner, tool-selection rules, outputs, and safeguards.
- [ ] 1.3 Update command installer, command inventory source, and focused command generator tests.

## 2. Performance Engineer Agent

- [ ] 2.1 Add `robot-performance-engineer.md` under canonical agent sources.
- [ ] 2.2 Define profiling baseline, evidence collection, finding prioritization, approval, delegation, verification, and outcome reporting responsibilities.
- [ ] 2.3 Define benchmark tool-selection, workload, environment, threshold, artifact, and limitation reporting responsibilities.
- [ ] 2.4 Update agent installer, agent inventory source, and focused agent generator tests.

## 3. Skill Alignment

- [ ] 3.1 Review skills `151`, `152`, and `161`-`164` for composable command and agent inputs/outputs.
- [ ] 3.2 Confirm how existing Maven/JMH guidance is exposed to `/benchmark`.
- [ ] 3.3 Refactor XML skill sources only where required, preserving existing skills as the authoritative behavior source.

## 4. Documentation and Integration

- [ ] 4.1 Update README lifecycle tables and command/agent references.
- [ ] 4.2 Update matching English, Spanish, and Chinese getting-started or lifecycle guidance where localized counterparts exist.
- [ ] 4.3 Document reproducible workload, baseline authority, tool-selection boundaries, and equivalent-comparison safeguards.
- [ ] 4.4 Confirm generated outputs are produced only through approved Maven profiles and not edited directly.

## 5. Validation

- [ ] 5.1 Validate edited XML sources with `xmllint --noout`.
- [ ] 5.2 Run focused generator tests or `./mvnw clean verify -pl skills-generator` for command, agent, and skill source changes.
- [ ] 5.3 Run local skill generation with `./mvnw clean install -pl skills-generator` and validate generated local skills.
- [ ] 5.4 For release output, run `./mvnw clean install -pl skills-generator -P release`, `npx skill-check@latest skills --no-security-scan --format github`, and behavioral scanner validation when available.
- [ ] 5.5 Run Markdown validation for documentation changes and inspect changed local links.
- [ ] 5.6 Run `openspec validate --all`.
