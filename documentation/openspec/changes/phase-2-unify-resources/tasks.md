# Tasks: Phase 2 - Unify Resource Layout

## Resource Relocation
- [ ] **Relocate** `system-prompts-generator/src/main/resources/` (system-prompt XML, XSLT, assets, etc.) under the **single** module—pick one clear tree (e.g. `resources/system-prompts/` + `resources/skills/` or a flatter layout per project convention).

## Path Updates
- [ ] **Update all classpath/resource paths** in Java and tests that assumed the old module root.
- [ ] Update any build scripts or configuration that references old resource paths
- [ ] Verify all resource loading works with new structure

## Schema and Processing Preservation
- [ ] **Keep PML schema / XSLT behavior** unchanged unless a **separate** change request is opened (per issue #477 out-of-scope note); **moving** files is fine, **rewriting** XSLT is a distinct decision.
- [ ] Validate that all XML processing produces identical output

## Cleanup
- [ ] **Delete** the empty `system-prompts-generator/` directory once nothing references it.
- [ ] Remove any dangling references to the old module structure
- [ ] Update .gitignore if needed for new resource locations

## Validation
- [ ] Run full build to ensure all resources are found correctly
- [ ] Verify generated skills match previous output
- [ ] Test resource loading in different environments (IDE, command line)