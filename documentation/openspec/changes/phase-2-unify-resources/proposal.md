# Phase 2: Unify Resource Layout (XML, XSLT, PML)

## Problem Statement

After merging the Java code in Phase 1, the resource files (XML sources, XSLT stylesheets, PML schemas, etc.) are still scattered across the old module structure. This creates confusion and makes the build process harder to understand.

## Proposed Solution

Consolidate all resource files under a single, clear directory structure within the unified module, updating all references to use the new paths.

## Success Criteria

- All XML, XSLT, and PML resources located in a single, logical directory structure
- All classpath and resource path references updated
- PML schema and XSLT behavior unchanged (no functional changes)
- Old `system-prompts-generator/` directory completely removed
- Build and generation processes work correctly with new paths

## Implementation Details

This phase focuses on file organization and path updates without changing the actual processing logic or schema definitions.