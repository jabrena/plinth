## ADDED Requirements

### Requirement: Problem framing skill reference

The skill bundle SHALL provide `021-problem-framing`, documenting how to establish Problem statement, Current state, Desired state, Stakeholders, and Success criteria for a problem under exploration.

#### Scenario: Skill documents the required problem-framing fields

- **WHEN** an agent applies `021-problem-framing` to a problem or issue
- **THEN** the skill guides production of a Problem statement, Current state, Desired state, Stakeholders, and Success criteria
- **AND** the skill is registered in the generator skill inventory so local skill generation emits `.agents/skills/021-problem-framing`

### Requirement: Root cause analysis skill reference

The skill bundle SHALL provide `022-root-cause-analysis`, documenting Five Whys, Fishbone (Ishikawa), Current Reality Tree, and constraint identification as techniques for surfacing root causes rather than symptoms.

#### Scenario: Skill documents multiple root-cause techniques

- **WHEN** an agent applies `022-root-cause-analysis` to a problem or issue
- **THEN** the skill guides application of Five Whys, Fishbone, and Current Reality Tree
- **AND** the skill guides identification of constraints limiting the current state
- **AND** the skill is registered in the generator skill inventory so local skill generation emits `.agents/skills/022-root-cause-analysis`

### Requirement: Assumption analysis skill reference

The skill bundle SHALL provide `023-assumption-analysis`, documenting how to make Assumptions explicit, list Unknowns, and define a Validation plan for resolving them.

#### Scenario: Skill documents assumptions, unknowns, and validation

- **WHEN** an agent applies `023-assumption-analysis` to a problem or issue
- **THEN** the skill guides production of explicit Assumptions, a list of Unknowns, and a Validation plan
- **AND** the skill is registered in the generator skill inventory so local skill generation emits `.agents/skills/023-assumption-analysis`

### Requirement: Context mapping skill reference

The skill bundle SHALL provide `024-context-mapping`, documenting how to identify Existing systems, Integrations, Ownership, and External dependencies relevant to a problem under exploration.

#### Scenario: Skill documents the required context-mapping fields

- **WHEN** an agent applies `024-context-mapping` to a problem or issue
- **THEN** the skill guides identification of Existing systems, Integrations, Ownership, and External dependencies
- **AND** the skill is registered in the generator skill inventory so local skill generation emits `.agents/skills/024-context-mapping`

### Requirement: Quality attribute discovery skill reference

The skill bundle SHALL provide `025-quality-attribute-discovery`, documenting how to identify and prioritize the quality attributes a future solution must satisfy before architecture and design begin.

#### Scenario: Skill documents quality-attribute identification and prioritization

- **WHEN** an agent applies `025-quality-attribute-discovery` to a problem or issue
- **THEN** the skill guides identification of candidate quality attributes relevant to the problem
- **AND** the skill guides prioritization of those quality attributes
- **AND** the skill explicitly positions this discovery as preceding architecture and design decisions, not replacing `030`-`032` ADR skills or `/explore-design`
- **AND** the skill is registered in the generator skill inventory so local skill generation emits `.agents/skills/025-quality-attribute-discovery`
