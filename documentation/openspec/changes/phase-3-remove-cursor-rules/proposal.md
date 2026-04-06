# Phase 3: Remove `.cursor/rules` from the Product

## Problem Statement

The project currently generates content to `.cursor/rules/` which creates duplication with the Skills pipeline. To complete the migration to skills-only architecture, we need to completely remove the rules generation and update all related documentation.

## Proposed Solution

Stop generating any content to `.cursor/rules/`, remove the existing rules directory, and update all contributor documentation to reflect the skills-only approach.

## Success Criteria

- No Markdown content generated to `.cursor/rules/`
- `.cursor/rules/` directory removed from repository
- All contributor guidance updated to reference skills-only workflow
- Documentation no longer promises rules that don't exist
- Clear migration path documented for users

## Implementation Details

This is the core breaking change that removes the rules pipeline entirely and updates all documentation to reflect the new skills-only reality.