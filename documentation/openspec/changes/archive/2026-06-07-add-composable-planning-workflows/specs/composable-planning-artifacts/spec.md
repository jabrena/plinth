## ADDED Requirements

### Requirement: Design discovery

The design-discovery skill SHALL inspect relevant repository context, clarify material ambiguity, compare feasible approaches, recommend a direction, obtain approval, and identify ADR candidates before deriving technical artifacts.

#### Scenario: Approve one approach

- **WHEN** an issue has multiple feasible technical solutions
- **THEN** the workflow reports goals, constraints, assumptions, unknowns, success criteria, alternatives, and trade-offs
- **AND** it records the approved direction and unresolved questions

### Requirement: Independent plan creation

The planning skill SHALL create or refine an implementation plan from an issue, approved design, ADRs, OpenSpec change, or a valid combination without requiring OpenSpec creation first.

#### Scenario: Create a plan directly from an issue and ADR

- **WHEN** a user supplies an approved issue and relevant ADR
- **THEN** the workflow produces a plan with technical approach, sequence, dependencies, verification, and source references

### Requirement: Independent OpenSpec creation

The OpenSpec skill SHALL create or update proposal, specification, design, and task artifacts from an issue, plan, approved design, ADRs, existing OpenSpec artifacts, or a valid combination without requiring a plan.

#### Scenario: Create OpenSpec directly from an issue

- **WHEN** a user supplies an approved issue without an implementation plan
- **THEN** the workflow creates or updates the appropriate OpenSpec artifacts
- **AND** it records the issue as the source
- **AND** it does not invent absent requirements

### Requirement: Change scope assessment

The OpenSpec skill MUST assess whether inputs represent one reviewable change or multiple independently valuable or deployable changes.

#### Scenario: Broad input requires multiple changes

- **WHEN** outcomes differ by value, release timing, ownership, dependency, risk, approval, rollback, or deployment boundary
- **THEN** the workflow proposes a change map with scopes and dependency order
- **AND** it waits for user approval before creating changes

#### Scenario: One outcome affects several capabilities

- **WHEN** one atomic outcome modifies several capability specifications
- **THEN** the workflow keeps those deltas in one OpenSpec change

### Requirement: Controlled derivation and authority

Planning and OpenSpec workflows MUST preserve concern-specific authority, record source artifacts and derivation direction, and MUST NOT perform automatic two-way synchronization.

#### Scenario: Source and derived artifacts conflict

- **WHEN** a derived plan or OpenSpec artifact conflicts with its sources
- **THEN** the workflow leaves sources unchanged
- **AND** it requires alignment review and an explicit user decision before propagation
