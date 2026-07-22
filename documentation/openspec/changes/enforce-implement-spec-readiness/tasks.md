## 1. Characterize Existing Safeguards

- [x] 1.1 Strengthen `plinth-commands-generator/src/test/java/info/jab/pml/CommandIndexesTest.java` characterization assertions for delegation, skill discovery, dirty-workspace blocking, feature-branch/worktree routing, and separate `main` approval without adding the new readiness behavior.
- [x] 1.2 Strengthen `plinth-agents-generator/src/test/java/info/jab/pml/AgentIndexesTest.java` characterization assertions for existing `robot-tech-lead` artifact authority, coder delegation, skill discovery, and no-direct-implementation safeguards.
- [x] 1.3 Run focused command and agent generator tests and record the green behavior-preservation baseline before changing either XML source.

## 2. Drive Readiness Behavior with Failing Tests

- [x] 2.1 Add command contract assertions and scenarios in `CommandIndexesTest.java` and `plinth-commands-generator/src/test/resources/gherkin/commands/implement-spec.feature` for complete selected-scope traceability and stop-before-side-effects behavior when evidence is absent, ambiguous, placeholder, completed-only, partial, or divergent.
- [x] 2.2 Add command contract assertions and scenarios for invocation-over-`design.md` location precedence, the canonical `## Implementation Location` strategies, missing or invalid strategy questions, and preserved `main` approval.
- [x] 2.3 Add matching tech-lead assertions and scenarios in `AgentIndexesTest.java` and `plinth-agents-generator/src/test/resources/gherkin/agents/robot-tech-lead.feature` for selected-scope verification, artifact-specific remediation, and location resolution.
- [x] 2.4 Run the focused tests before XML edits and confirm they fail for the expected missing readiness and location policy markers.

## 3. Implement the Smallest Contract Change

- [x] 3.1 Update `plinth-commands-generator/src/main/resources/commands/implement-spec.xml` to verify bidirectional traceability for the selected scope before skill discovery, Git-location changes, or delegation and to report every unsupported scenario or task.
- [x] 3.2 Update `plinth-commands-generator/src/main/resources/commands/implement-spec.xml` to resolve location from invocation constraints, then a valid `design.md` `## Implementation Location` section, then an explicit contributor question while retaining dirty-workspace and default-branch protections.
- [x] 3.3 Update `plinth-agents-generator/src/main/resources/agents/robot-tech-lead.xml` with the aligned readiness ownership, remediation, selected-scope, and location-precedence contract.
- [x] 3.4 Refactor duplicated wording only when command and agent intent remains explicit and the new focused tests stay green; do not add a shared generator abstraction in this change.
- [x] 3.5 Validate both edited XML sources with `xmllint --noout` and their declared schemas.

## 4. Verify Generated Behavior and Compatibility

- [x] 4.1 Run `./mvnw clean verify -pl plinth-commands-generator` and review generated `implement-spec.md` under the module target output for gate order, selected-scope traceability, location precedence, and preserved safeguards.
- [x] 4.2 Run `./mvnw clean verify -pl plinth-agents-generator` and review generated `robot-tech-lead.md` under the module target output for aligned policy markers.
- [x] 4.3 Cross-check generated command and agent Markdown for equivalent readiness semantics without requiring identical document structure.
- [ ] 4.4 Execute the existing focused `/implement-spec` and `robot-tech-lead` acceptance prompts listed in their module prompt inventories and record any environment-dependent skips.
- [x] 4.5 Run `openspec validate --all` from `documentation/` and resolve every validation error before implementation handoff.
- [x] 4.6 Review CI-facing diffs to confirm no generated `.cursor/`, public `skills/`, website `docs/`, XSLT, or unrelated inventory assets were edited directly, and record migration guidance for callers affected by the stricter gate or location question.
- [x] 4.7 Regenerate the command and agent bundles through `plinth-skills-generator`, install all embedded assets into `.cursor/commands/` and `.cursor/agents/`, and verify both destinations match the generated installer assets.
