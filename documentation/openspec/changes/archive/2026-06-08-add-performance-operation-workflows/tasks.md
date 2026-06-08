## 1. Command Assets

- [x] 1.1 Add `/profile` under canonical command sources with accepted inputs, owner, workflow, outputs, and safeguards.
- [x] 1.2 Add `/benchmark` under canonical command sources with accepted inputs, owner, tool-selection rules, outputs, and safeguards.
- [x] 1.3 Update command installer, command inventory source, and focused command generator tests.

## 2. Performance Engineer Agent

- [x] 2.1 Add `robot-java-performance.md` under canonical agent sources.
- [x] 2.2 Define profiling baseline, evidence collection, finding prioritization, approval, delegation, verification, and outcome reporting responsibilities.
- [x] 2.3 Define benchmark tool-selection, workload, environment, threshold, artifact, and limitation reporting responsibilities.
- [x] 2.4 Update agent installer, agent inventory source, and focused agent generator tests.

## 3. Skill Alignment

- [x] 3.1 Review skills `151`, `152`, and `161`-`164` for composable command and agent inputs/outputs.
- [x] 3.2 Confirm how existing Maven/JMH guidance is exposed to `/benchmark`.
- [x] 3.3 Refactor XML skill sources only where required, preserving existing skills as the authoritative behavior source.

## 4. Documentation and Integration

- [x] 4.1 Update README lifecycle tables and command/agent references.
- [x] 4.2 Update matching English, Spanish, and Chinese getting-started or lifecycle guidance where localized counterparts exist.
- [x] 4.3 Document reproducible workload, baseline authority, tool-selection boundaries, and equivalent-comparison safeguards.
- [x] 4.4 Confirm generated outputs are produced only through approved Maven profiles and not edited directly.

## 5. Validation

- [x] 5.1 Validate edited XML sources with `xmllint --noout`.
- [x] 5.2 Run focused generator tests or `./mvnw clean verify -pl skills-generator` for command, agent, and skill source changes.
- [x] 5.3 Run local skill generation with `./mvnw clean install -pl skills-generator` and validate generated local skills.
- [x] 5.4 Confirm release output was not refreshed; release-profile validation remains required only when updating public `skills/`.
- [x] 5.5 Run Markdown validation for documentation changes and inspect changed local links.
- [x] 5.6 Run `openspec validate --all`.
- [ ] 5.7 Archive `add-performance-operation-workflows` after implementation is complete and validation passes.
