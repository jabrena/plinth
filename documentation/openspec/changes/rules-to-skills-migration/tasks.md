# Tasks: Remove Rules Support in Favor of Skills

## Overview
This is the master change tracking the overall migration from Rules to Skills-only architecture. Individual phases are tracked as separate OpenSpec changes.

## Phase Dependencies
- [ ] Complete Phase 0: Decision and Compatibility (`phase-0-decision-compatibility`)
- [ ] Complete Phase 1: Merge Generators (`phase-1-merge-generators`)
- [ ] Complete Phase 2: Unify Resources (`phase-2-unify-resources`)
- [ ] Complete Phase 3: Remove Cursor Rules (`phase-3-remove-cursor-rules`)
- [ ] Complete Phase 4: Site, Docs, Examples (`phase-4-site-docs-examples`)
- [ ] Complete Phase 5: CI and Validation (`phase-5-ci-validation`)
- [ ] Complete Phase 6: Issue Alignment (`phase-6-issue-alignment`)

## Final Validation
- [ ] Verify single generator pipeline produces only Skills
- [ ] Confirm no `.cursor/rules/` content exists
- [ ] Validate all documentation reflects skills-only approach
- [ ] Test migration path for existing users
- [ ] Ensure cross-tool compatibility maintained

## Release Preparation
- [ ] Update version to v0.14.0 (breaking change)
- [ ] Create comprehensive CHANGELOG entry
- [ ] Document migration guide for users
- [ ] Update README and main documentation
- [ ] Validate all examples work with new approach