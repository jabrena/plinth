## MODIFIED Requirements

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
