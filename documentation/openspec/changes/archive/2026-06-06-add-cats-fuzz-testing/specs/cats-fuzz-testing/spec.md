# CATS Fuzz Testing Specification

## ADDED Requirements

### Requirement: New Skill Identifier
The repository SHALL define the fuzz testing skill identifier as `134-java-testing-fuzzing-testing`.

#### Scenario: Skill identifier is standardized
- **Given** maintainers implement fuzz testing guidance
- **When** they create or reference the skill in generator sources and documentation
- **Then** the identifier used is `134-java-testing-fuzzing-testing`
- **And** references are consistent across OpenSpec artifacts and GitHub issue tracking

### Requirement: CATS Execution in CI
The repository CI workflow SHALL execute CATS fuzz tests as part of automated quality checks and provide reproducible failure evidence.

#### Scenario: CI runs CATS and reports failures
- **Given** a pull request updates APIs or API contracts
- **When** the CI pipeline runs the CATS stage
- **Then** CATS fuzz tests are executed automatically
- **And** failing cases include request/response evidence that can be reproduced by contributors

### Requirement: Negative and Boundary Validation
The CATS configuration SHALL validate negative and boundary input scenarios against declared API contracts.

#### Scenario: Invalid inputs are validated by CATS
- **Given** an API contract with required fields and constraints
- **When** CATS runs generated negative and edge-case requests
- **Then** invalid payloads are rejected with expected error behavior
- **And** contract violations are reported as actionable test failures

### Requirement: Contributor Local Workflow
Project documentation SHALL define how contributors execute CATS checks locally.

#### Scenario: Contributor runs CATS locally
- **Given** a contributor wants to verify fuzz behavior before opening a pull request
- **When** they follow the documented local setup and command steps
- **Then** they can run CATS locally with the same baseline configuration used in CI
- **And** they can interpret and troubleshoot the resulting report output
