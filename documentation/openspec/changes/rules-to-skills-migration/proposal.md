# Remove Rules Support in Favor of Skills

## Problem Statement

The cursor-rules-java project currently maintains two parallel content generation pipelines:
1. **Rules** generated into `.cursor/rules/` from `system-prompts-generator/` (PML-backed sources)
2. **Skills** generated into `skills/` via `skills-generator/`

This creates significant maintenance overhead:
- **1:1 duplication** between rules and skills for many topics
- **Token bloat** and maintenance drift
- **Unclear boundaries** between principles vs procedures
- **Generator complexity** with unmanaged duplicate sources

## Proposed Solution

Migrate to a **skills-only architecture** by:

1. **Consolidating generators** into a single pipeline
2. **Removing `.cursor/rules/` support** entirely
3. **Focusing on Skills** as the primary deliverable
4. **Establishing clear ownership model** for what belongs where

## Success Criteria

- Single generator pipeline producing only Skills
- No duplication between rules and skills
- Clear documentation of principles vs procedures
- Maintained cross-tool compatibility (Cursor, Claude, GitHub Copilot, OpenAI Codex)
- Smooth migration path for existing users

## Impact Assessment

### Benefits
- **Reduced maintenance cost** - single pipeline to maintain
- **Better cross-tool fit** - SKILL.md is widely supported
- **Clear positioning** - skills library for Java enterprise work
- **No duplication drift** - single source of truth

### Risks
- **Breaking change** for users relying on `.cursor/rules`
- **Loss of always-on enforcement** in Cursor (mitigated by AGENTS.md)
- **Migration complexity** for existing consumers

## Implementation Approach

This change will be implemented through 6 sequential phases:

0. **Decision and Compatibility** - Planning and breaking change policy
1. **Merge Generators** - Consolidate Maven modules and Java sources
2. **Unify Resources** - Merge XML, XSLT, and PML resources
3. **Remove Cursor Rules** - Stop generating and remove `.cursor/rules`
4. **Site, Docs, Examples** - Update documentation and examples
5. **CI and Validation** - Adjust workflows and validation
6. **Issue Alignment** - Close loop on generator alignment

Each phase is tracked as a separate OpenSpec change for detailed planning and execution.

## References

- GitHub Issue: [#477 Remove Rules support in favor of Skills](https://github.com/jabrena/cursor-rules-java/issues/477)
- Target milestone: v0.14.0
- Cross-tool compatibility analysis included in issue