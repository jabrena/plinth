## ADDED Requirements

### Requirement: Project benchmark harness root

The repository SHALL provide a project benchmark harness at `plinth-benchmark/` with a root `README.md` that documents layout, the four scenarios, and the metrics scorecard.

#### Scenario: Harness root is present

- **GIVEN** the change is implemented
- **WHEN** a maintainer inspects the repository root
- **THEN** `plinth-benchmark/README.md` exists
- **AND** it describes scenarios `scenario1` through `scenario4`
- **AND** it documents that Case 3 is pending
- **AND** it documents the minimal v1 metrics scorecard fields

### Requirement: Four scenario folders with specs and Gherkin

The harness SHALL provide `plinth-benchmark/scenario1/` through `plinth-benchmark/scenario4/`, each containing `README.md`, a `specs/` folder with scenario requirements, and a `gherkin/` folder with acceptance criteria.

#### Scenario: Each scenario owns specs and gherkin

- **GIVEN** the harness is implemented
- **WHEN** a maintainer inspects each scenario folder
- **THEN** `specs/` contains at least one Markdown scenario specification
- **AND** `gherkin/` contains at least one `.feature` file
- **AND** that feature includes exactly one scenario tagged `@acceptance-test`

### Requirement: Case 1 README-only input boundary

Case 1 (`scenario1`) SHALL use only `examples/openspec/god-analysis-api/requirements/problem1/README.md` as requirements input and SHALL label runs `case-1-readme-only`.

#### Scenario: Case 1 excludes richer notes and OpenSpec

- **GIVEN** a Case 1 benchmark run
- **WHEN** requirements input is prepared
- **THEN** other files under `examples/openspec/god-analysis-api/requirements/problem1/` are not provided
- **AND** `examples/openspec/god-analysis-api/openspec/changes/` is not provided
- **AND** the run case id is `case-1-readme-only`

### Requirement: Case 2 all problem1 requirements files

Case 2 (`scenario2`) SHALL use all files under `examples/openspec/god-analysis-api/requirements/problem1/` without the OpenSpec changes tree and SHALL label runs `case-2-all-problem1-requirements`.

Case 2 MUST include at least these files:

- `examples/openspec/god-analysis-api/requirements/problem1/README.md`
- `examples/openspec/god-analysis-api/requirements/problem1/US-001_God_Analysis_API.md`
- `examples/openspec/god-analysis-api/requirements/problem1/US-001_god_analysis_api.feature`
- `examples/openspec/god-analysis-api/requirements/problem1/US-001-god-analysis-api.openapi.yaml`
- `examples/openspec/god-analysis-api/requirements/problem1/my-json-server-oas.yaml`
- `examples/openspec/god-analysis-api/requirements/problem1/ADR-001-God-Analysis-API-Functional-Requirements.md`
- `examples/openspec/god-analysis-api/requirements/problem1/ADR-002-God-Analysis-API-Non-Functional-Requirements.md`
- `examples/openspec/god-analysis-api/requirements/problem1/ADR-003-God-Analysis-API-Technology-Stack.md`

#### Scenario: Case 2 excludes OpenSpec changes tree

- **GIVEN** a Case 2 benchmark run
- **WHEN** requirements input is prepared
- **THEN** all listed problem1 requirement files are provided
- **AND** `examples/openspec/god-analysis-api/openspec/changes/` is not provided
- **AND** the run case id is `case-2-all-problem1-requirements`

### Requirement: Case 3 pending

Case 3 (`scenario3`) SHALL exist as a harness placeholder with case id `case-3-pending` and MUST document that its input and workflow are pending definition.

#### Scenario: Case 3 is not runnable yet

- **GIVEN** the Case 3 harness folder
- **WHEN** a maintainer reviews Case 3 specs and Gherkin
- **THEN** the case is labeled `case-3-pending`
- **AND** the documentation states that the input/workflow contract is TBD
- **AND** Case 3 is excluded from campaign ranking until the contract is defined

### Requirement: Case 4 current OpenSpec for problem1

Case 4 (`scenario4`) SHALL use the current checked-in OpenSpec change under `examples/openspec/god-analysis-api/openspec/changes/` (problem1-derived change, for example `add-god-analysis-api/`) as primary input and SHALL label runs `case-4-current-openspec-problem1`.

#### Scenario: Case 4 uses examples OpenSpec changes tree

- **GIVEN** a Case 4 benchmark run
- **WHEN** implementation input is prepared
- **THEN** the primary input is `examples/openspec/god-analysis-api/openspec/changes/`
- **AND** outcome assessment uses the OpenSpec proposal, design, tasks, and specs in that change tree
- **AND** the run case id is `case-4-current-openspec-problem1`

### Requirement: Metrics scorecard

The harness README SHALL define efficiency, outcome-quality, and process metrics, including the minimal v1 required fields, and MUST instruct operators to rank cost or tokens among runs where `acceptance_pass` is true, excluding pending Case 3 until defined.

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
