# Tasks: Phase 3 - Remove `.cursor/rules` from the Product

## Stop Generation
- [ ] **Stop generating** any Markdown into a rules output directory used for Cursor rules.
- [ ] Remove or disable any build steps that create `.cursor/rules` content
- [ ] Verify build process no longer attempts rules generation

## Remove Existing Content
- [ ] **Remove** committed content under `.cursor/rules/` (or entire folder) per repo policy; update **`.gitignore`** if generated artifacts were tracked.
- [ ] Clean up any rules-related artifacts from the repository
- [ ] Update .gitignore to prevent accidental re-creation of rules directory

## Update Contributor Documentation
- [ ] **Replace contributor guidance** — `AGENTS.md` / `CLAUDE.md` / `CONTRIBUTING.md` must no longer say "edit XML → deploy to `.cursor/rules`"; they should say **single generator → `skills/`** only.
- [ ] Update any developer documentation that references the rules pipeline
- [ ] Ensure all contribution workflows reference skills-only approach

## Update Main Documentation
- [ ] **Update** `SYSTEM-PROMPTS-JAVA.md` (or **replace** with a **Skills-first catalog** / pointer to `skills.sh` + local `skills/`) so the main doc does not promise rules that no longer exist.
- [ ] Update project README if it references rules generation
- [ ] Create migration guide for users transitioning from rules to skills

## Validation
- [ ] Verify no references to `.cursor/rules` remain in documentation
- [ ] Confirm build process works without rules generation
- [ ] Test that skills generation continues to work correctly