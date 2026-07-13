# Tasks

## 1. Implementation Checklist

### Step 1 — Behavior-preserving extraction

- [ ] 1.1 Review issue [#1036](https://github.com/jabrena/plinth/issues/1036) and this change's refined design before editing code.
- [ ] 1.2 Create `agents-generator` module (`artifactId` `agents-generator`, `groupId` `info.jab.pml`) and register it in the root `pom.xml`.
- [ ] 1.3 Introduce `agents.xml` and `AgentIndexes.java`; move the nine agent markdown assets to `agents-generator/src/main/resources/agents/` and move `java-agents-inventory-template.md` into `agents-generator`.
- [ ] 1.4 Add a one-way Maven dependency from `agents-generator` to `commands-generator` to reuse `InventoryXmlLoader`.
- [ ] 1.5 Move `EmbeddedAgentBundleTests` from `SkillsGeneratorTest` into `agents-generator` as `AgentIndexesTest`; relocate `skills-generator/src/test/resources/gherkin/agents/`; update asset classpath assertions from `skill-references/assets/agents/` to `agents/`. Split installer XML checks into `skills-generator` (see 1.9).
- [ ] 1.6 Add a one-way Maven dependency from `skills-generator` to `agents-generator`.
- [ ] 1.7 Implement `bridge-agent-assets` and `bridge-agent-inventory-template` executions in `skills-generator` (sibling-module copy into `target/generated-resources/`) and expose staged files at `skill-references/assets/agents/` and `skill-references/assets/java-agents-inventory-template.md` on the main classpath. Reuse existing `copy-bridged-command-assets-to-test` for test classpath staging.
- [ ] 1.8 Remove committed agent markdown copies from `skills-generator` once the bridge is working.
- [ ] 1.9 Add `AgentBridgeTest` and `AgentInstallerParityTest` in `skills-generator`. `AgentInstallerParityTest` MUST parse `005-agents-installation.xml` XInclude hrefs (not `skills.xml` `<resource-list>`) and compare against `AgentIndexes.agentFiles()`.
- [ ] 1.10 Add `AgentSkillPropagationTest` asserting embed-first output: `005` reference embeds staged agent bodies (for example `name: robot-architect`); `002` reference embeds inventory template rows inline. Do not copy `CommandSkillPropagationTest` link-only assertions.
- [ ] 1.11 Update `005-agents-installation.xml` repository copy-path guidance to `agents-generator/src/main/resources/agents/` while preserving embedded reference fallback for skill-only installs.
- [ ] 1.12 Update `002-agents-inventory.feature`, `005-agents-installation.feature`, and `acceptance-tests-prompts-agents.md` for relocated paths and generated-skill assertions.
- [ ] 1.13 Update `AGENTS.md` and `DEVELOPER.md` with `agents-generator` commands and `./mvnw clean install -pl skills-generator -am`.
- [ ] 1.14 Run `./mvnw clean verify -pl agents-generator`, `./mvnw clean install -pl skills-generator -am`, and `./mvnw clean verify`.

### Step 2 — Inventory template hardening

- [ ] 2.1 Generate or harden `java-agents-inventory-template.md` from `agents.xml` and bridge it into `skill-references/assets/` during `generate-resources`.
- [ ] 2.2 Extend parity tests so `002` inventory output cannot drift from the manifest without failing the build.
- [ ] 2.3 Re-run `./mvnw clean verify -pl skills-generator -am` and confirm generated `002` still lists every manifest agent.

### Closeout

- [ ] 3.1 Remove the `//TODO Move to agents-generator ASAP` comment after relocation.
- [ ] 3.2 Run `openspec validate --all`.
