# Agile User Story Quality Specification

## ADDED Requirements

### Requirement: INVEST Criteria Guidance
The `014-agile-user-story` skill SHALL include explicit INVEST quality criteria guidance for user story authoring.

#### Scenario: INVEST guidance is present
- **Given** a user is creating a story with the `014-agile-user-story` skill
- **When** the skill instructions are presented
- **Then** the skill includes a dedicated INVEST section
- **And** each INVEST dimension is defined in practical terms

### Requirement: INVEST Validation Step
The `014-agile-user-story` skill SHALL require an INVEST validation step before final story output.

#### Scenario: Story is validated before finalization
- **Given** a story draft has been created
- **When** the workflow reaches finalization
- **Then** the skill prompts validation against INVEST
- **And** the final output is confirmed against all INVEST dimensions

### Requirement: INVEST-Compliant Example
The `014-agile-user-story` skill SHALL provide an example showing how a user story satisfies INVEST.

#### Scenario: Example demonstrates criteria
- **Given** a user reviews the skill documentation
- **When** they reach examples
- **Then** at least one example references INVEST compliance
- **And** the example is traceable to Independent, Negotiable, Valuable, Estimable, Small, and Testable qualities
