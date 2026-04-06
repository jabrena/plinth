# compatibility-planning Specification

## Purpose
TBD - created by archiving change phase-0-decision-compatibility. Update Purpose after archive.
## Requirements
### Requirement: Breaking Change Policy
The project SHALL establish a clear breaking change policy for the rules-to-skills migration.

#### Scenario: Version bump planning
- **Given** the migration removes `.cursor/rules` support
- **When** planning the release
- **Then** a major version bump to v0.14.0 is planned
- **And** breaking change documentation is prepared
- **And** migration guidance is created for users

### Requirement: Dependency Inventory
The project SHALL maintain a complete inventory of systems dependent on the current architecture.

#### Scenario: Complete dependency mapping
- **Given** the migration is being planned
- **When** conducting the dependency audit
- **Then** all references to `system-prompts-generator` are identified
- **And** all references to `.cursor/rules` are catalogued
- **And** all site templates and CI dependencies are mapped
- **And** all example projects are inventoried

### Requirement: Artifact Naming Strategy
The project SHALL define a clear naming strategy for the merged generator module.

#### Scenario: Naming decisions documented
- **Given** the generators will be merged
- **When** making naming decisions
- **Then** the final artifact name is decided
- **And** Maven coordinates are confirmed
- **And** backward compatibility implications are understood

