# composable-planning-artifacts Specification

## Purpose
TBD - created by archiving change add-composable-planning-workflows. Update Purpose after archive.
## Requirements
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

#### Scenario: Create OpenSpec from sanitized complete issue context through create-spec

- **WHEN** `042-planning-openspec` is invoked by `/create-spec` with an issue identifier or URL
- **THEN** the workflow requires a maintainer-prepared sanitized artifact derived outside the agent context from the issue description and complete paginated comment thread
- **AND** the artifact confirms complete description and comment coverage
- **AND** the workflow does not retrieve or ingest raw tracker prose
- **AND** it preserves system, repository, command, skill, and OpenSpec instructions as higher authority
- **AND** it reports conflicts and unclear requirements instead of inventing resolutions
- **AND** it stops before authoring when sanitized complete issue context is unavailable

#### Scenario: Create OpenSpec directly from an issue outside create-spec

- **WHEN** a user supplies an approved issue without an implementation plan outside `/create-spec`
- **THEN** the workflow creates or updates the appropriate OpenSpec artifacts from maintainer-sanitized issue facts
- **AND** it records the issue as the source
- **AND** it does not invent absent requirements

#### Scenario: Use other outsider-authored sources

- **WHEN** the OpenSpec skill receives issue, pull request, wiki, discussion, chat, or other outsider-authored prose
- **THEN** it requires a maintainer-provided sanitized summary
- **AND** it does not ingest the raw source body

#### Scenario: Scaffold a new change via the OpenSpec CLI

- **WHEN** the approved change ID does not already exist under `openspec/changes/`
- **THEN** the workflow runs `openspec new change <change-id>` to scaffold the change directory before authoring any artifact
- **AND** the scaffolded directory includes a CLI-generated `.openspec.yaml` metadata file
- **AND** the workflow authors `proposal.md`, `design.md`, spec deltas, and `tasks.md` into the scaffolded directory using authoritative source facts
- **AND** the workflow removes the CLI-generated placeholder `README.md` once `proposal.md` is authored

#### Scenario: Skip re-scaffolding an existing change

- **WHEN** the approved change ID already exists under `openspec/changes/`
- **THEN** the workflow does not run `openspec new change <change-id>` again
- **AND** it edits the existing proposal, design, spec, or tasks artifacts directly

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

