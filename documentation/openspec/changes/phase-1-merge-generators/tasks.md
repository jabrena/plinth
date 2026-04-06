# Tasks: Phase 1 - Merge the Generators

## Java Source Consolidation
- [ ] **Move Java sources and tests** from `system-prompts-generator/src/` into `skills-generator/src/` (or new unified module path), resolve package names and any duplicate classes.

## Maven Configuration
- [ ] **Merge `pom.xml`** — Single `pom.xml` with dependencies of both; **remove** the `copy-cursor-rules` execution that writes to `../.cursor/rules`.
- [ ] **Update parent `pom.xml`** — Remove `<module>system-prompts-generator</module>`; fix `<dependencyManagement>` / plugin references that pointed at `cursor-rules-java-generator`.

## Dependency Resolution
- [ ] **Fix `skills-generator`'s dependency** — Today it depends on `cursor-rules-java-generator`; after merge, that dependency becomes **in-module** (remove inter-module dependency or replace with internal packages).

## Build Validation
- [ ] **Run** `./mvnw clean verify` (and `-pl` scoped runs while iterating) until green.
- [ ] Verify no `.cursor/rules` content is generated
- [ ] Confirm skills generation still works correctly

## Testing
- [ ] Ensure all existing tests pass in the merged module
- [ ] Verify test coverage is maintained
- [ ] Update any test configurations that assumed separate modules