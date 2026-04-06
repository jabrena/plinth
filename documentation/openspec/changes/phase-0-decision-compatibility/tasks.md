# Tasks: Phase 0 - Decision and Compatibility

## Breaking Change Policy
- [ ] **Confirm breaking change policy** — Removing `.cursor/rules` is a **major consumer break** for anyone who only copied rules. Plan **version bump** (e.g. v0.14.0), **CHANGELOG** entry, and a **migration snippet** ("use Skills under `skills/` or registry; rules removed").

## Dependency Inventory
- [ ] **Inventory dependents** — Search repo for: `system-prompts-generator`, `.cursor/rules`, `cursor-rules-java-generator`, `SYSTEM-PROMPTS-JAVA`, site templates, CI, examples (`examples/cursor-agent-cli-demo`, etc.) and list required edits.

## Naming Decisions
- [ ] **Rename vs keep artifact IDs** — Decide whether the merged module keeps `cursor-rules-java-skills-generator` or is renamed (e.g. `java-agents-content-generator`) and whether `groupId`/`artifactId` stay for Maven consumers.

## Documentation Planning
- [ ] Create migration guide template for users
- [ ] Plan CHANGELOG structure for breaking changes
- [ ] Identify all documentation that needs updates