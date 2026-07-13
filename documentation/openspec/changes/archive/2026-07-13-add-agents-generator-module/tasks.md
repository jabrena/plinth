# Tasks

## 1. Implementation Checklist

### Step 1 — Behavior-preserving extraction

- [x] 1.1 Review issue [#1036](https://github.com/jabrena/plinth/issues/1036) and this change's refined design before editing code.
- [x] 1.2 Create `plinth-agents-generator` module (`artifactId` `plinth-agents-generator`, `groupId` `info.jab.pml`) and register it in the root `pom.xml`.
- [x] 1.3 Introduce `agents.xml` and `AgentIndexes.java`; move the nine agent markdown assets to `plinth-agents-generator/src/main/resources/agents/` and move `java-agents-inventory-template.md` into `plinth-agents-generator`.
- [x] 1.4 Add a one-way Maven dependency from `plinth-agents-generator` to `plinth-commands-generator` to reuse `InventoryXmlLoader`.
- [x] 1.5 Move `EmbeddedAgentBundleTests` from `SkillsGeneratorTest` into `plinth-agents-generator` as `AgentIndexesTest`; relocate `plinth-skills-generator/src/test/resources/gherkin/agents/`; update asset classpath assertions from `skill-references/assets/agents/` to `agents/`. Split installer XML checks into `plinth-skills-generator` (see 1.9).
- [x] 1.6 Add a one-way Maven dependency from `plinth-skills-generator` to `plinth-agents-generator`.
- [x] 1.7 Implement `bridge-agent-assets` and `bridge-agent-inventory-template` executions in `plinth-skills-generator` (sibling-module copy into `target/generated-resources/`) and expose staged files at `skill-references/assets/agents/` and `skill-references/assets/java-agents-inventory-template.md` on the main classpath. Reuse existing `copy-bridged-command-assets-to-test` for test classpath staging.
- [x] 1.8 Remove committed agent markdown copies from `plinth-skills-generator` once the bridge is working.
- [x] 1.9 Add `AgentBridgeTest` and `AgentInstallerParityTest` in `plinth-skills-generator`. `AgentInstallerParityTest` MUST parse `005-agents-installation.xml` XInclude hrefs (not `skills.xml` `<resource-list>`) and compare against `AgentIndexes.agentFiles()`.
- [x] 1.10 Add `AgentSkillPropagationTest` asserting embed-first output: `005` reference embeds staged agent bodies (for example `name: robot-architect`); `002` reference embeds inventory template rows inline. Do not copy `CommandSkillPropagationTest` link-only assertions.
- [x] 1.11 Update `005-agents-installation.xml` repository copy-path guidance to `plinth-agents-generator/src/main/resources/agents/` while preserving embedded reference fallback for skill-only installs.
- [x] 1.12 Update `002-agents-inventory.feature`, `005-agents-installation.feature`, and `acceptance-tests-prompts-agents.md` for relocated paths and generated-skill assertions.
- [x] 1.13 Update `AGENTS.md` and `DEVELOPER.md` with `plinth-agents-generator` commands and `./mvnw clean install -pl plinth-skills-generator -am`.
- [x] 1.14 Run `./mvnw clean verify -pl plinth-agents-generator`, `./mvnw clean install -pl plinth-skills-generator -am`, and `./mvnw clean verify`.

### Step 2 — Inventory template hardening

- [x] 2.1 Generate or harden `java-agents-inventory-template.md` from `agents.xml` and bridge it into `skill-references/assets/` during `generate-resources`.
- [x] 2.2 Extend parity tests so `002` inventory output cannot drift from the manifest without failing the build.
- [x] 2.3 Re-run `./mvnw clean verify -pl plinth-skills-generator -am` and confirm generated `002` still lists every manifest agent.

### Closeout

- [x] 3.1 Remove the `//TODO Move to plinth-agents-generator ASAP` comment after relocation.
- [x] 3.2 Run `openspec validate --all`.
