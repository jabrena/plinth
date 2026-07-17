## ADDED Requirements

### Requirement: Project benchmark harness root

The repository SHALL provide a project benchmark harness at `plinth-benchmark/` with a root `README.md` that documents layout, the four scenarios, and the metrics scorecard.

#### Scenario: Harness root is present

- **GIVEN** the change is implemented
- **WHEN** a maintainer inspects the repository root
- **THEN** `plinth-benchmark/README.md` exists
- **AND** it describes scenarios `scenario1` through `scenario4`
- **AND** it documents the minimal v1 metrics scorecard fields

### Requirement: Four scenario folders with specs and Gherkin

The harness SHALL provide `plinth-benchmark/scenario1/` through `plinth-benchmark/scenario4/`, each containing `README.md`, a `specs/` folder with scenario requirements, and a `gherkin/` folder with acceptance criteria.

#### Scenario: Each scenario owns specs and gherkin

- **GIVEN** the harness is implemented
- **WHEN** a maintainer inspects each scenario folder
- **THEN** `specs/` contains at least one Markdown scenario specification
- **AND** `gherkin/` contains at least one `.feature` file
- **AND** that feature includes exactly one scenario tagged `@acceptance-test`

### Requirement: Scenario 1 README-only input boundary

Scenario 1 SHALL use only `examples/openspec/god-analysis-api/requirements/problem1/README.md` as requirements input and SHALL label runs `case-1-readme-only`.

#### Scenario: Scenario 1 excludes richer notes and OpenSpec

- **GIVEN** a Scenario 1 benchmark run
- **WHEN** requirements input is prepared
- **THEN** other files under `examples/openspec/god-analysis-api/requirements/problem1/` are not provided
- **AND** `examples/openspec/god-analysis-api/openspec/changes/` is not provided
- **AND** the run case id is `case-1-readme-only`

### Requirement: Scenario 2 full requirements notes

Scenario 2 SHALL use the full directory `examples/openspec/god-analysis-api/requirements/problem1/` without the OpenSpec changes tree and SHALL label runs `case-2-all-requirements-notes`.

#### Scenario: Scenario 2 excludes OpenSpec changes tree

- **GIVEN** a Scenario 2 benchmark run
- **WHEN** requirements input is prepared
- **THEN** `examples/openspec/god-analysis-api/openspec/changes/` is not provided
- **AND** the run case id is `case-2-all-requirements-notes`

### Requirement: Scenario 3 raw OpenSpec with official skill

Scenario 3 SHALL use `examples/openspec/god-analysis-api/openspec/changes/` as primary input, implement with the official OpenSpec skill or OPSX workflow, MUST NOT use Plinth `/create-spec`, `/implement-spec`, or `@042-planning-openspec`, and SHALL label runs `case-3-raw-openspec-official-skill`.

#### Scenario: Scenario 3 forbids Plinth OpenSpec command path

- **GIVEN** a Scenario 3 benchmark run
- **WHEN** the agent implements from the raw OpenSpec change
- **THEN** Plinth `/create-spec` and `/implement-spec` are not used
- **AND** Plinth `@042-planning-openspec` is not used
- **AND** the run case id is `case-3-raw-openspec-official-skill`

### Requirement: Scenario 4 Plinth refinement path

Scenario 4 SHALL require Full Plinth, seed from `examples/openspec/god-analysis-api/requirements/problem1/README.md`, MUST NOT use `examples/openspec/god-analysis-api/openspec/changes/` as input, follow `/update-issue` → `/create-spec` → `/implement-spec` (optional `/explore-design`, `/create-adr`, `/review-alignment`), and SHALL label runs `case-4-plinth-commands-openspec-refinement`.

#### Scenario: Scenario 4 generates OpenSpec separately from checked-in tree

- **GIVEN** a Scenario 4 benchmark run
- **WHEN** OpenSpec artifacts are created and implementation completes
- **THEN** a generated OpenSpec change exists as a run artifact
- **AND** that artifact is separate from `examples/openspec/god-analysis-api/openspec/changes/`
- **AND** the run case id is `case-4-plinth-commands-openspec-refinement`

### Requirement: Metrics scorecard

The harness README SHALL define efficiency, outcome-quality, and process metrics, including the minimal v1 required fields, and MUST instruct operators to rank cost or tokens among runs where `acceptance_pass` is true.

#### Scenario: Minimal v1 fields are documented

- **GIVEN** `plinth-benchmark/README.md`
- **WHEN** a maintainer prepares a scenario execution record
- **THEN** the documented required fields include `wall_clock_s`, `tokens_total`, `cost_usd`, `acceptance_pass`, `rework_turns`
- **AND** labels include `scenario`, `case_id`, `tool`, `model`, `plinth_config`, `commit`

### Requirement: Out of scope boundaries

The project benchmark harness MUST NOT define JVM `/benchmark` (JMeter, Gatling, JMH) workloads as part of this capability.

#### Scenario: JVM performance testing is excluded

- **GIVEN** the project benchmark harness documentation
- **WHEN** a maintainer reviews scope
- **THEN** JMeter, Gatling, and JMH application performance workloads are listed as out of scope for this harness
