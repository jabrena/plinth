# Phase 1: Maven Consolidation Specification

## REMOVED Requirements

### Requirement: Separate Generator Modules
The system SHALL no longer maintain separate `system-prompts-generator` and `skills-generator` Maven modules.

#### Scenario: Single module structure
- **Given** the generators are being merged
- **When** examining the Maven project structure
- **Then** only one generator module exists
- **And** no inter-module dependencies exist between generators
- **And** the parent pom.xml references only the unified module

### Requirement: Rules Copy Execution
The system SHALL no longer copy generated rules to `.cursor/rules` directory.

#### Scenario: No rules copying
- **Given** the Maven configuration is updated
- **When** the build executes
- **Then** no `copy-cursor-rules` execution occurs
- **And** no content is written to `../.cursor/rules`

## MODIFIED Requirements

### Requirement: Build Process
The system SHALL use a unified Maven build process for all content generation.

#### Scenario: Unified build execution
- **Given** the modules are merged
- **When** running `./mvnw clean verify`
- **Then** the build completes successfully
- **And** all Java sources compile without errors
- **And** all tests pass
- **And** skills generation works correctly