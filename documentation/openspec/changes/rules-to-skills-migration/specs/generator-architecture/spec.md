# Generator Architecture Specification

## REMOVED Requirements

### Requirement: Rules Generation Pipeline
The system shall no longer generate Cursor rules to `.cursor/rules/` directory.

#### Scenario: Rules directory removed
- **Given** the project has been migrated to skills-only architecture
- **When** the build process runs
- **Then** no content is generated to `.cursor/rules/`
- **And** the `.cursor/rules/` directory does not exist

### Requirement: Dual Generator Modules
The system shall no longer maintain separate `system-prompts-generator` and `skills-generator` modules.

#### Scenario: Single generator module
- **Given** the generators have been merged
- **When** examining the project structure
- **Then** only one generator module exists
- **And** no inter-module dependencies exist between generators

## ADDED Requirements

### Requirement: Unified Skills Generation
The system SHALL generate all content as Skills in the `skills/` directory.

#### Scenario: Skills-only output
- **Given** the unified generator is configured
- **When** the build process runs
- **Then** all generated content appears in `skills/` directory
- **And** content follows the SKILL.md format
- **And** content is compatible with Cursor, Claude, GitHub Copilot, and OpenAI Codex

### Requirement: Single Source of Truth
The system SHALL maintain a single source of truth for all generated content without duplication.

#### Scenario: No content duplication
- **Given** the migration is complete
- **When** examining generated content
- **Then** no duplicate content exists between different output formats
- **And** all content has a single authoritative source

## MODIFIED Requirements

### Requirement: Build Process
The system SHALL use a single unified build process for all content generation.

#### Scenario: Unified build command
- **Given** the generators have been merged
- **When** running `./mvnw clean install -pl skills-generator`
- **Then** all skills are generated successfully
- **And** no rules generation occurs
- **And** the build completes without errors

### Requirement: Documentation and Examples
The system SHALL provide documentation and examples that reflect the skills-only architecture.

#### Scenario: Updated documentation
- **Given** the migration is complete
- **When** reviewing project documentation
- **Then** all references point to skills-only approach
- **And** no references to rules generation remain
- **And** migration guidance is provided for existing users