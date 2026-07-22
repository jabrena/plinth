## 1. Define Focused Contract Tests

- [ ] 1.1 Update `plinth-commands-generator/src/test/java/info/jab/pml/CommandIndexesTest.java` to assert generated `/implement-spec` content includes the OpenSpec acceptance-evidence stop and missing-location question while preserving the dirty-workspace and `main` approval safeguards.
- [ ] 1.2 Extend `plinth-commands-generator/src/test/resources/gherkin/commands/implement-spec.feature` with readiness scenarios for complete evidence, missing evidence, explicit location, missing location, and the protected `main` selection.
- [ ] 1.3 Update `plinth-agents-generator/src/test/java/info/jab/pml/AgentIndexesTest.java` to assert `robot-tech-lead` owns pre-delegation acceptance-evidence and location verification.
- [ ] 1.4 Extend `plinth-agents-generator/src/test/resources/gherkin/agents/robot-tech-lead.feature` with matching stop, ask, and approved-location scenarios.

## 2. Implement Readiness Contracts

- [ ] 2.1 Update `plinth-commands-generator/src/main/resources/commands/implement-spec.xml` so OpenSpec execution verifies concrete scenarios plus actionable incomplete tasks before skill discovery, Git-location changes, or delegation.
- [ ] 2.2 Update `plinth-commands-generator/src/main/resources/commands/implement-spec.xml` to resolve location from invocation constraints, then OpenSpec content, then an explicit contributor question, while retaining dirty-workspace and default-branch protections.
- [ ] 2.3 Update `plinth-agents-generator/src/main/resources/agents/robot-tech-lead.xml` with the same readiness ownership, evidence-remediation, and location-resolution contract.
- [ ] 2.4 Validate both edited XML sources with `xmllint --noout` and their declared schemas.

## 3. Verify Generated Behavior

- [ ] 3.1 Run `./mvnw clean verify -pl plinth-commands-generator` and review generated `implement-spec.md` under the module target output for the required gate ordering and safeguards.
- [ ] 3.2 Run `./mvnw clean verify -pl plinth-agents-generator` and review generated `robot-tech-lead.md` under the module target output for aligned readiness behavior.
- [ ] 3.3 Execute the existing focused `/implement-spec` and `robot-tech-lead` acceptance prompts listed in their module prompt inventories and record any environment-dependent skips.
- [ ] 3.4 Run `openspec validate --all` from `documentation/` and resolve every validation error before implementation handoff.
- [ ] 3.5 Review CI-facing diffs to confirm no generated `.cursor/`, public `skills/`, website `docs/`, XSLT, or unrelated inventory assets were edited directly.
