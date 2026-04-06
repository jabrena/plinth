# Phase 3: Rules Removal Specification

## REMOVED Requirements

### Requirement: Cursor Rules Directory
The system SHALL no longer maintain or generate content to the `.cursor/rules/` directory.

#### Scenario: Rules directory eliminated
- **Given** Phase 3 is complete
- **When** examining the project structure
- **Then** no `.cursor/rules/` directory exists
- **And** no build process generates rules content
- **And** .gitignore is updated to prevent accidental recreation

### Requirement: Rules-Based Documentation
The system SHALL no longer provide documentation that references rules generation.

#### Scenario: Documentation cleanup
- **Given** the rules pipeline is removed
- **When** reviewing contributor documentation
- **Then** no references to "edit XML → deploy to `.cursor/rules`" exist
- **And** all guidance points to skills-only workflow
- **And** SYSTEM-PROMPTS-JAVA.md is updated or replaced

## ADDED Requirements

### Requirement: Migration Documentation
The system SHALL provide clear migration guidance for users transitioning from rules to skills.

#### Scenario: Migration guidance available
- **Given** rules support is removed
- **When** users need to migrate
- **Then** comprehensive migration documentation exists
- **And** clear mapping from rules to skills is provided
- **And** cross-tool compatibility guidance is available