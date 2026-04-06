# Tasks: Phase 1 - Merge the Generators

## Java Source Consolidation
- [x] **Move Java sources and tests** from `system-prompts-generator/src/` into `skills-generator/src/` (or new unified module path), resolve package names and any duplicate classes.

## Maven Configuration
- [x] **Merge `pom.xml`** — Single `pom.xml` with dependencies of both; **remove** the `copy-cursor-rules` execution that writes to `../.cursor/rules`.
- [x] **Update parent `pom.xml`** — Remove `<module>system-prompts-generator</module>`; fix `<dependencyManagement>` / plugin references that pointed at `cursor-rules-java-generator`.

## Dependency Resolution
- [x] **Fix `skills-generator`'s dependency** — Today it depends on `cursor-rules-java-generator`; after merge, that dependency becomes **in-module** (remove inter-module dependency or replace with internal packages).

## Build Validation
- [x] **Run** `./mvnw clean verify` (and `-pl` scoped runs while iterating) until green.
- [x] Verify no `.cursor/rules` content is generated
- [x] Confirm skills generation still works correctly
- [x] **Regenerate skills idempotently** — Run `./mvnw clean install -pl skills-generator` so `skills/` is regenerated; then confirm **`git status` is clean** (no diffs under `skills/`). A successful merge produces deterministic output, so a second full generation should not change tracked files.

## Testing
- [x] Ensure all existing tests pass in the merged module
- [x] Verify test coverage is maintained
- [x] Update any test configurations that assumed separate modules

## Documentation
- [x] **Update `AGENTS.md` (Commands)** — Remove the `system-prompts-generator` entries (`./mvnw clean verify -pl system-prompts-generator` and `./mvnw clean install -pl system-prompts-generator` that deploy to `.cursor/rules/`); document only the unified generator workflow (build/test and install to `skills/` via the merged module). Align the **File structure** and **Boundaries** bullets if XML sources or module paths move.
