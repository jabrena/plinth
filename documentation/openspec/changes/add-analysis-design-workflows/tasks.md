## 1. Parallel Source Changes

- [ ] 1.1 Create one isolated branch/worktree per source child with `/create-worktree` when running the changes concurrently.
- [ ] 1.2 Complete and validate `add-analysis-design-commands`.
- [ ] 1.3 Complete and validate `add-analysis-design-agents`.
- [ ] 1.4 Confirm new-XML approval, then complete and validate `add-composable-planning-workflows`.

## 2. Integration Gate

- [ ] 2.1 Integrate the three source changes and resolve shared test-file conflicts without changing ownership boundaries.
- [ ] 2.2 Confirm command names, agent names, skill identifiers, installer counts, and inventory counts agree.
- [ ] 2.3 Resolve the `robot-coordinator` compatibility-alias decision.
- [ ] 2.4 Review the integrated source artifacts against issue #806 before documentation begins.

## 3. Documentation and Final Validation

- [ ] 3.1 Complete `document-analysis-design-lifecycle`.
- [ ] 3.2 Confirm all localized documentation and migration guidance reflect the integrated behavior.
- [ ] 3.3 Confirm final Maven, local generation, Markdown, skill-check, stale-reference, and OpenSpec validation pass.

## 4. Parent Completion

- [ ] 4.1 Verify all four child changes are complete and ready for archive.
- [ ] 4.2 Verify issue #806 acceptance criteria are covered by the child specifications.
- [ ] 4.3 Archive child changes in integration order, then archive this coordination change.
