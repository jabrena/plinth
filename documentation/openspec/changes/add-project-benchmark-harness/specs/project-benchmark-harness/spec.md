## ADDED Requirements

### Requirement: Project benchmark harness root

The repository SHALL provide a project benchmark harness at `benchmarks/` with a root `README.md` that documents layout, the information-richness progression across scenarios, and the metrics scorecard.

#### Scenario: Harness root is present

- **GIVEN** the change is implemented
- **WHEN** a maintainer inspects the repository root
- **THEN** `benchmarks/README.md` exists
- **AND** it describes scenarios `scenario1` through `scenario4`
- **AND** it documents that information richness increases from Scenario 1 to Scenario 4
- **AND** it documents that Case 3 is pending
- **AND** it documents the minimal v1 metrics scorecard fields

### Requirement: Four scenario folders with specs and Gherkin

The harness SHALL provide `benchmarks/scenario1/` through `benchmarks/scenario4/`, each containing `README.md`, a `specs/` folder with scenario requirements, and a `gherkin/` folder with acceptance criteria.

#### Scenario: Each scenario owns specs and gherkin

- **GIVEN** the harness is implemented
- **WHEN** a maintainer inspects each scenario folder
- **THEN** `specs/` contains the scenario requirement artifacts for that case
- **AND** `gherkin/` contains at least one `.feature` file
- **AND** that feature includes exactly one scenario tagged `@acceptance-test`

### Requirement: Case 1 minimal functional notes

Case 1 (`scenario1`) SHALL use only `benchmarks/scenario1/specs/functional-requirements/README.md` as requirements input, MUST run in a skill-agnostic workspace without `.agents/skills` and without `.cursor/skills` (including skill references under `.cursor/skills`), SHALL label runs `case-1-readme-only`, and MUST persist a result JSON under `benchmarks/scenario1/results/` with the minimal v1 metrics fields.

#### Scenario: Case 1 excludes full FR package, OpenSpec, and skills

- **GIVEN** a Case 1 benchmark run
- **WHEN** requirements input is prepared
- **THEN** only `benchmarks/scenario1/specs/functional-requirements/README.md` is provided
- **AND** a full `functional-requirements/problem1/` package is not provided
- **AND** `technical-requirements/openspec/` is not provided
- **AND** `.agents/skills` is not present or is empty
- **AND** `.cursor/skills` is not present or is empty
- **AND** no skill references under `.cursor/skills` are provided to the agent
- **AND** the run case id is `case-1-readme-only`

#### Scenario: Case 1 run records minimal v1 metrics as JSON

- **GIVEN** a Case 1 benchmark run for scenario `scenario1`
- **AND** the run case id is `case-1-readme-only`
- **WHEN** the agent implements in `benchmarks/scenario1/demo/` and the run completes
- **THEN** a result JSON file exists under `benchmarks/scenario1/results/`
- **AND** the result JSON includes `wall_clock_s`, `tokens_total`, `cost_usd`, `acceptance_pass`, and `rework_turns`
- **AND** the result JSON includes labels `scenario`, `case_id`, `tool`, `model`, `plinth_config`, and `commit`
- **AND** the result JSON includes `skills_count`, `agents_count`, `skills`, and `agents`
- **AND** `scenario` equals `scenario1`
- **AND** `case_id` equals `case-1-readme-only`

### Requirement: Case 2 full functional requirements package

Case 2 (`scenario2`) SHALL use the full harness-local tree `benchmarks/scenario2/specs/functional-requirements/problem1/` without a technical OpenSpec tree and SHALL label runs `case-2-all-problem1-requirements`.

Case 2 MUST include at least these files under that `problem1/` directory:

- `README.md`
- `US-001_God_Analysis_API.md`
- `US-001_god_analysis_api.feature`
- `US-001-god-analysis-api.openapi.yaml`
- `my-json-server-oas.yaml`
- `ADR-001-God-Analysis-API-Functional-Requirements.md`
- `ADR-002-God-Analysis-API-Non-Functional-Requirements.md`
- `ADR-003-God-Analysis-API-Technology-Stack.md`

#### Scenario: Case 2 excludes technical OpenSpec

- **GIVEN** a Case 2 benchmark run
- **WHEN** requirements input is prepared
- **THEN** the functional requirements input is `benchmarks/scenario2/specs/functional-requirements/problem1/`
- **AND** all listed problem1 requirement files are provided
- **AND** `technical-requirements/openspec/` is not provided
- **AND** the run case id is `case-2-all-problem1-requirements`

### Requirement: Case 3 pending richness step

Case 3 (`scenario3`) SHALL exist as a harness placeholder with case id `case-3-pending`, MUST document that its input and workflow are pending definition, and MUST be positioned as the reserved richness step between Case 2 and Case 4.

#### Scenario: Case 3 is not runnable yet

- **GIVEN** the Case 3 harness folder
- **WHEN** a maintainer reviews Case 3 specs and Gherkin
- **THEN** the case is labeled `case-3-pending`
- **AND** the documentation states that the input/workflow contract is TBD
- **AND** Case 3 is excluded from campaign ranking until the contract is defined

### Requirement: Case 4 functional and technical requirements

Case 4 (`scenario4`) SHALL provide harness-local functional and technical requirement trees and SHALL label runs `case-4-current-openspec-problem1`.

Functional requirements MUST live under `benchmarks/scenario4/specs/functional-requirements/problem1/` and MUST include the same problem1 file inventory as Case 2.

Technical requirements MUST live under `benchmarks/scenario4/specs/technical-requirements/openspec/` and MUST include the OpenSpec project (`config.yaml`, README, and `changes/add-god-analysis-api/` with proposal, design, tasks, and specs).

OpenSpec Source and Derivation links MUST resolve to the co-located functional-requirements files under `benchmarks/scenario4/specs/functional-requirements/problem1/`.

#### Scenario: Case 4 uses linked functional and technical trees

- **GIVEN** a Case 4 benchmark run
- **WHEN** implementation input is prepared
- **THEN** the functional requirements input is `benchmarks/scenario4/specs/functional-requirements/problem1/`
- **AND** the technical requirements input is `benchmarks/scenario4/specs/technical-requirements/openspec/`
- **AND** OpenSpec source links resolve under `functional-requirements/problem1/`
- **AND** the run case id is `case-4-current-openspec-problem1`

### Requirement: Metrics scorecard

The harness README SHALL define efficiency, outcome-quality, and process metrics, including the minimal v1 required fields, and MUST instruct operators to rank cost or tokens among runs where `acceptance_pass` is true, excluding pending Case 3 until defined, and to prefer same-tool/same-model cells when comparing Scenario 1 → 2 → 4 richness steps.

#### Scenario: Minimal v1 fields are documented

- **GIVEN** `benchmarks/README.md`
- **WHEN** a maintainer prepares a scenario execution record
- **THEN** the documented required fields include `wall_clock_s`, `tokens_total`, `cost_usd`, `acceptance_pass`, `rework_turns`
- **AND** labels include `scenario`, `case_id`, `tool`, `model`, `plinth_config`, `commit`
- **AND** Plinth usage fields include `skills_count`, `agents_count`, `skills`, and `agents`

### Requirement: Out of scope boundaries

The project benchmark harness MUST NOT define JVM `/benchmark` (JMeter, Gatling, JMH) workloads as part of this capability.

#### Scenario: JVM performance testing is excluded

- **GIVEN** the project benchmark harness documentation
- **WHEN** a maintainer reviews scope
- **THEN** JMeter, Gatling, and JMH application performance workloads are listed as out of scope for this harness
