# Tasks: Phase 5 - CI and Validation

## GitHub Actions Updates
- [ ] **GitHub Actions** — Adjust workflows that build `-pl system-prompts-generator` or assume two install phases.
- [ ] Update build matrices to reference the unified module
- [ ] Ensure deployment workflows work with new structure

## Skills Validation
- [ ] **Skill validation** — `npx skill-check skills` remains the main quality gate; add/adjust if paths change.
- [ ] Verify all skills pass validation after migration
- [ ] Update any custom validation rules if needed

## Enforcement Checks
- [ ] **Optional:** add a CI check that **fails** if `.cursor/rules` reappears (if the project wants to enforce the policy).
- [ ] Consider adding validation that ensures skills-only output
- [ ] Add checks for documentation consistency

## Build Process Validation
- [ ] Verify all CI builds pass with new module structure
- [ ] Test deployment processes work correctly
- [ ] Ensure artifact publishing works as expected

## Quality Gates
- [ ] Update any quality metrics that referenced rules generation
- [ ] Ensure code coverage and other metrics work with merged module
- [ ] Validate that all tests pass in CI environment