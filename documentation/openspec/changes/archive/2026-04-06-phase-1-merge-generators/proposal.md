# Phase 1: Merge the Generators (Maven + Java)

## Problem Statement

Currently, the project has two separate Maven modules:
- `system-prompts-generator` - generates rules to `.cursor/rules/`
- `plinth-skills-generator` - generates skills to `skills/` and depends on the first module

This creates unnecessary complexity and inter-module dependencies that need to be consolidated.

## Proposed Solution

Merge the two generator modules into a single unified module that handles all content generation, eliminating the inter-module dependency and simplifying the build process.

## Success Criteria

- Single Maven module containing all generator functionality
- No inter-module dependencies between generators
- All Java sources and tests consolidated
- Build process (`./mvnw clean verify`) passes successfully
- No `.cursor/rules` generation (preparation for removal)

## Implementation Details

This phase focuses on the Maven and Java code consolidation without changing the underlying XML/XSLT processing logic.